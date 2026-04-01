package repositories;

import entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByClientId(Long clientId);

    List<Reservation> findByPharmacyId(Long pharmacyId);

    Optional<Reservation> findByIdAndClientId(Long id, Long clientId);
}

