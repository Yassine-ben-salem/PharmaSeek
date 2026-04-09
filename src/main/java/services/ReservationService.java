package services;

import dtos.ReservationCreateRequest;
import dtos.ReservationDto;
import dtos.ReservationItemDto;
import dtos.ReservationStatusUpdateRequest;
import entities.Client;
import entities.PharmacyStock;
import entities.Pharmacy;
import entities.Reservation;
import entities.ReservationItem;
import exceptions.ReservationNotFoundException;
import lombok.AllArgsConstructor;
import mappers.ReservationMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repositories.ClientRepository;
import repositories.PharmacyStockRepository;
import repositories.PharmacyRepository;
import repositories.ReservationItemRepository;
import repositories.ReservationRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReservationService {
    private static final Set<String> ALLOWED_STATUSES = Set.of("PENDING", "CONFIRMED", "CANCELLED", "EXPIRED");

    private final ReservationRepository reservationRepository;
    private final ReservationItemRepository reservationItemRepository;
    private final PharmacyStockRepository pharmacyStockRepository;
    private final ClientRepository clientRepository;
    private final PharmacyRepository pharmacyRepository;
    private final ReservationMapper reservationMapper;

    public List<ReservationDto> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(this::mapWithItems)
                .collect(Collectors.toList());
    }

    public Optional<ReservationDto> getReservationById(Long id) {
        return reservationRepository.findById(id)
                .map(this::mapWithItems);
    }

    public ReservationDto getReservationByIdForRequester(Long id, Authentication authentication) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));

        if (isClient(authentication)) {
            Long authenticatedUserId = resolveAuthenticatedUserId(authentication);
            if (!authenticatedUserId.equals(reservation.getClient().getId())) {
                throw new AccessDeniedException("You can only access your own reservations.");
            }
        }

        return mapWithItems(reservation);
    }

    @Transactional
    public ReservationDto createReservation(ReservationCreateRequest request) {
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new IllegalArgumentException("Client with ID " + request.getClientId() + " not found."));

        Pharmacy pharmacy = pharmacyRepository.findById(request.getPharmacyId())
                .orElseThrow(() -> new IllegalArgumentException("Pharmacy with ID " + request.getPharmacyId() + " not found."));

        Instant now = Instant.now();

        Reservation reservation = new Reservation();
        reservation.setClient(client);
        reservation.setPharmacy(pharmacy);
        reservation.setStatus("PENDING");
        reservation.setReservedAt(now);
        reservation.setUpdatedAt(now);
        reservation.setTotalPrice(BigDecimal.ZERO);
        reservation.setNotes(request.getNotes());

        Reservation savedReservation = reservationRepository.save(reservation);

        BigDecimal totalPrice = BigDecimal.ZERO;
        Integer minDelayMinutes = null;

        for (var itemRequest : request.getItems()) {
            PharmacyStock stock = pharmacyStockRepository
                    .findByPharmacyIdAndDrugId(request.getPharmacyId(), itemRequest.getDrugId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Drug " + itemRequest.getDrugId() + " is not available in pharmacy " + request.getPharmacyId() + "."
                    ));

            if (stock.getQuantity() < itemRequest.getQuantity()) {
                throw new IllegalStateException(
                        "Not enough stock for drugId=" + itemRequest.getDrugId() + ". Available=" + stock.getQuantity()
                                + ", requested=" + itemRequest.getQuantity()
                );
            }

            stock.setQuantity(stock.getQuantity() - itemRequest.getQuantity());
            stock.setUpdatedAt(now);
            pharmacyStockRepository.save(stock);

            BigDecimal itemSubtotal = stock.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));

            ReservationItem item = new ReservationItem();
            item.setReservation(savedReservation);
            item.setStock(stock);
            item.setQuantity(itemRequest.getQuantity());
            item.setPriceAtReservation(stock.getPrice());
            item.setSubtotal(itemSubtotal);
            item.setCreatedAt(now);
            reservationItemRepository.save(item);

            totalPrice = totalPrice.add(itemSubtotal);

            if (minDelayMinutes == null || stock.getReservationDelayMinutes() < minDelayMinutes) {
                minDelayMinutes = stock.getReservationDelayMinutes();
            }
        }

        savedReservation.setTotalPrice(totalPrice);
        if (minDelayMinutes != null) {
            savedReservation.setExpirationTime(now.plusSeconds(minDelayMinutes.longValue() * 60));
        }

        Reservation updatedReservation = reservationRepository.save(savedReservation);
        return mapWithItems(updatedReservation);
    }

    public Optional<ReservationDto> updateReservation(Long id, ReservationDto reservationDto) {


        return reservationRepository.findById(id)
                .map(existingReservation -> {
                    Client client = clientRepository.findById(reservationDto.getClientId())
                            .orElseThrow(() -> new IllegalArgumentException("Client with ID " + reservationDto.getClientId() + " not found."));

                    Pharmacy pharmacy = pharmacyRepository.findById(reservationDto.getPharmacyId())
                            .orElseThrow(() -> new IllegalArgumentException("Pharmacy with ID " + reservationDto.getPharmacyId() + " not found."));

                    existingReservation.setClient(client);
                    existingReservation.setPharmacy(pharmacy);
                    existingReservation.setStatus(reservationDto.getStatus() == null
                            ? existingReservation.getStatus()
                            : reservationDto.getStatus().toUpperCase(Locale.ROOT));
                    existingReservation.setTotalPrice(reservationDto.getTotalPrice());
                    existingReservation.setReservedAt(reservationDto.getReservedAt());
                    existingReservation.setExpirationTime(reservationDto.getExpirationTime());

                    Reservation updatedReservation = reservationRepository.save(existingReservation);
                    return mapWithItems(updatedReservation);
                });
    }

    @Transactional
    public Optional<ReservationDto> updateReservationStatus(Long id, ReservationStatusUpdateRequest request) {
        return reservationRepository.findById(id)
                .map(existingReservation -> {
                    String currentStatus = normalizeStatus(existingReservation.getStatus());
                    String targetStatus = normalizeStatus(request.getStatus());

                    validateStatus(targetStatus);

                    if (!isTransitionAllowed(currentStatus, targetStatus)) {
                        throw new IllegalStateException(
                                "Invalid reservation status transition from " + currentStatus + " to " + targetStatus + "."
                        );
                    }

                    existingReservation.setStatus(targetStatus);
                    Reservation updatedReservation = reservationRepository.save(existingReservation);
                    return mapWithItems(updatedReservation);
                });
    }

    public boolean deleteReservation(Long id) {
        if (reservationRepository.existsById(id)) {
            reservationRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<ReservationDto> getReservationsByClientId(Long clientId) {
        if (!clientRepository.existsById(clientId)) {
            throw new IllegalArgumentException("Client with ID " + clientId + " not found.");
        }
        return reservationRepository.findByClientId(clientId).stream()
                .map(this::mapWithItems)
                .collect(Collectors.toList());
    }

    public List<ReservationDto> getReservationsByClientIdForRequester(Long clientId, Authentication authentication) {
        if (isClient(authentication)) {
            Long authenticatedUserId = resolveAuthenticatedUserId(authentication);
            if (!authenticatedUserId.equals(clientId)) {
                throw new AccessDeniedException("You can only access your own reservations.");
            }
        }
        return getReservationsByClientId(clientId);
    }

    public List<ReservationDto> getMyReservations(Authentication authentication) {
        Long authenticatedUserId = resolveAuthenticatedUserId(authentication);
        return getReservationsByClientId(authenticatedUserId);
    }

    public ReservationDto getMyReservationById(Long reservationId, Authentication authentication) {
        Long authenticatedUserId = resolveAuthenticatedUserId(authentication);
        Reservation reservation = reservationRepository.findByIdAndClientId(reservationId, authenticatedUserId)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));
        return mapWithItems(reservation);
    }

    public List<ReservationDto>getReservationsByPharmacyId(Long pharmacyId) {
        if (!pharmacyRepository.existsById(pharmacyId)) {
            throw new IllegalArgumentException("Pharmacy with ID " + pharmacyId + " not found.");
        }
        return reservationRepository.findByPharmacyId(pharmacyId).stream()
                .map(this::mapWithItems)
                .collect(Collectors.toList());
    }

    private ReservationDto mapWithItems(Reservation reservation) {
        ReservationDto dto = reservationMapper.toReservationDto(reservation);
        dto.setStatus(dto.getStatus() == null ? null : dto.getStatus().toUpperCase(Locale.ROOT));

        List<ReservationItemDto> itemDtos = reservationItemRepository.findByReservationId(reservation.getId()).stream()
                .map(item -> {
                    ReservationItemDto itemDto = new ReservationItemDto();
                    itemDto.setId(item.getId());
                    itemDto.setDrugId(item.getDrug().getId());
                    itemDto.setQuantity(item.getQuantity());
                    itemDto.setPriceAtReservation(item.getPriceAtReservation());
                    return itemDto;
                })
                .collect(Collectors.toList());

        dto.setItems(itemDtos);
        return dto;
    }

    private String normalizeStatus(String value) {
        return value == null ? "" : value.trim().toUpperCase(Locale.ROOT);
    }

    private void validateStatus(String status) {
        if (!ALLOWED_STATUSES.contains(status)) {
            throw new IllegalArgumentException("Unsupported reservation status: " + status + ".");
        }
    }

    private boolean isTransitionAllowed(String currentStatus, String targetStatus) {
        if (currentStatus.equals(targetStatus)) {
            return true;
        }

        if ("PENDING".equals(currentStatus)) {
            return "CONFIRMED".equals(targetStatus)
                    || "CANCELLED".equals(targetStatus)
                    || "EXPIRED".equals(targetStatus);
        }

        return false;
    }

    private boolean isClient(Authentication authentication) {
        return authentication != null
                && authentication.getAuthorities().stream()
                .anyMatch(auth -> "ROLE_CLIENT".equals(auth.getAuthority()));
    }

    private Long resolveAuthenticatedUserId(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Authentication required.");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof Long userId) {
            return userId;
        }
        if (principal instanceof String userIdValue) {
            try {
                return Long.parseLong(userIdValue);
            } catch (NumberFormatException ex) {
                throw new AccessDeniedException("Invalid authenticated user identity.");
            }
        }

        throw new AccessDeniedException("Invalid authenticated user identity.");
    }
}
