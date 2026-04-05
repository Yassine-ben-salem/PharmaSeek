package services;

import dtos.PharmacyStockDto;
import entities.Drug;
import entities.Pharmacy;
import entities.PharmacyStock;
import lombok.AllArgsConstructor;
import mappers.PharmacyStockMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import repositories.DrugRepository;
import repositories.PharmacyRepository;
import repositories.PharmacyStockRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PharmacyStockService {

    private final PharmacyStockRepository pharmacyStockRepository;
    private final PharmacyRepository pharmacyRepository;
    private final DrugRepository drugRepository;
    private final PharmacyStockMapper pharmacyStockMapper;

    public List<PharmacyStockDto> getAllPharmacyStock() {
        return pharmacyStockRepository.findAll().stream()
                .map(pharmacyStockMapper::toPharmacyStockDto)
                .collect(Collectors.toList());
    }

    public Optional<PharmacyStockDto> getPharmacyStockById(Long id) {
        return pharmacyStockRepository.findById(id)
                .map(pharmacyStockMapper::toPharmacyStockDto);
    }

    public PharmacyStockDto createPharmacyStock(PharmacyStockDto pharmacyStockDto) {
        pharmacyStockRepository.findByPharmacyIdAndDrugId(pharmacyStockDto.getPharmacyId(), pharmacyStockDto.getDrugId())
                .ifPresent(existing -> {
                    throw new IllegalStateException(
                            "Stock already exists for pharmacyId=" + pharmacyStockDto.getPharmacyId()
                                    + " and drugId=" + pharmacyStockDto.getDrugId()
                                    + ". Use update endpoint instead."
                    );
                });

        Pharmacy pharmacy = pharmacyRepository.findById(pharmacyStockDto.getPharmacyId())
                .orElseThrow(() -> new IllegalArgumentException("Pharmacy with ID " + pharmacyStockDto.getPharmacyId() + " not found."));
        Drug drug = drugRepository.findById(pharmacyStockDto.getDrugId())
                .orElseThrow(() -> new IllegalArgumentException("Drug with ID " + pharmacyStockDto.getDrugId() + " not found."));

        PharmacyStock pharmacyStock = pharmacyStockMapper.toPharmacyStock(pharmacyStockDto);
        pharmacyStock.setPharmacy(pharmacy);
        pharmacyStock.setDrug(drug);
        pharmacyStock.setCreatedAt(Instant.now());
        pharmacyStock.setUpdatedAt(Instant.now());
        PharmacyStock savedPharmacyStock = pharmacyStockRepository.save(pharmacyStock);
        return pharmacyStockMapper.toPharmacyStockDto(savedPharmacyStock);
    }

    public Optional<PharmacyStockDto> updatePharmacyStock(Long id, PharmacyStockDto pharmacyStockDto) {
        return pharmacyStockRepository.findById(id)
                .map(existingStock -> {
                    Long targetPharmacyId = pharmacyStockDto.getPharmacyId() != null ? pharmacyStockDto.getPharmacyId() : existingStock.getPharmacy().getId();
                    Long targetDrugId = pharmacyStockDto.getDrugId() != null ? pharmacyStockDto.getDrugId() : existingStock.getDrug().getId();

                    pharmacyStockRepository.findByPharmacyIdAndDrugId(targetPharmacyId, targetDrugId)
                            .ifPresent(duplicate -> {
                                if (!duplicate.getId().equals(id)) {
                                    throw new IllegalStateException(
                                            "Stock already exists for pharmacyId=" + targetPharmacyId
                                                    + " and drugId=" + targetDrugId
                                    );
                                }
                            });

                    Pharmacy pharmacy = pharmacyRepository.findById(targetPharmacyId)
                            .orElseThrow(() -> new IllegalArgumentException("Pharmacy with ID " + targetPharmacyId + " not found."));
                    Drug drug = drugRepository.findById(targetDrugId)
                            .orElseThrow(() -> new IllegalArgumentException("Drug with ID " + targetDrugId + " not found."));

                    existingStock.setPharmacy(pharmacy);
                    existingStock.setDrug(drug);
                    existingStock.setQuantity(pharmacyStockDto.getQuantity());
                    existingStock.setPrice(pharmacyStockDto.getPrice());
                    existingStock.setReservationDelayMinutes(pharmacyStockDto.getReservationDelayMinutes());
                    existingStock.setUpdatedAt(Instant.now());
                    PharmacyStock updatedStock = pharmacyStockRepository.save(existingStock);
                    return pharmacyStockMapper.toPharmacyStockDto(updatedStock);
                });
    }

    public boolean deletePharmacyStock(Long id) {
        if (pharmacyStockRepository.existsById(id)) {
            pharmacyStockRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<PharmacyStockDto> getPharmacyStockByPharmacyId(Long pharmacyId) {
        return pharmacyStockRepository.findByPharmacyId(pharmacyId).stream()
                .map(pharmacyStockMapper::toPharmacyStockDto)
                .collect(Collectors.toList());
    }

    public List<PharmacyStockDto> getPharmacyStockByDrugId(Long drugId) {
        return pharmacyStockRepository.findByDrugId(drugId).stream()
                .map(pharmacyStockMapper::toPharmacyStockDto)
                .collect(Collectors.toList());
    }
}