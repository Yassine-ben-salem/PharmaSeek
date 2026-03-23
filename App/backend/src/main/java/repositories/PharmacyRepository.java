package repositories;

import entities.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {
    boolean existsByMatriculeFiscale(String matriculeFiscale);
}
