package repositories;

import entities.PharmacyStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PharmacyStockRepository extends JpaRepository<PharmacyStock, Long> {
    Optional<PharmacyStock> findByPharmacyIdAndDrugId(Long pharmacyId, Long drugId);
    List<PharmacyStock> findByDrugId(Long drugId);
    List<PharmacyStock> findByPharmacyId(Long pharmacyId);
}