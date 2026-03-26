package controllers;

import dtos.PharmacyStockDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import services.PharmacyStockService;

import java.util.List;

@RestController
@RequestMapping("/pharmacy-stock")
@AllArgsConstructor
@Tag(name="Pharmacy Stock")
public class PharmacyStockController {

    private final PharmacyStockService pharmacyStockService;

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN','PHARMACY')")
    public ResponseEntity<List<PharmacyStockDto>> getAllPharmacyStock() {
        return ResponseEntity.ok(pharmacyStockService.getAllPharmacyStock());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PHARMACY')")
    public ResponseEntity<PharmacyStockDto> getPharmacyStockById(@PathVariable Long id) {
        return pharmacyStockService.getPharmacyStockById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("")
    @PreAuthorize("hasAnyRole('PHARMACY','ADMIN')")
    public ResponseEntity<PharmacyStockDto> createPharmacyStock(@RequestBody PharmacyStockDto pharmacyStockDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pharmacyStockService.createPharmacyStock(pharmacyStockDto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PHARMACY')")
    public ResponseEntity<PharmacyStockDto> updatePharmacyStock(@PathVariable Long id, @RequestBody PharmacyStockDto pharmacyStockDto) {
        return pharmacyStockService.updatePharmacyStock(id, pharmacyStockDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PHARMACY')")
    public ResponseEntity<Void> deletePharmacyStock(@PathVariable Long id) {
        if (pharmacyStockService.deletePharmacyStock(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/pharmacy/{pharmacyId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PHARMACY')")
    public ResponseEntity<List<PharmacyStockDto>> getPharmacyStockByPharmacyId(@PathVariable Long pharmacyId) {
        return ResponseEntity.ok(pharmacyStockService.getPharmacyStockByPharmacyId(pharmacyId));
    }

    @GetMapping("/drug/{drugId}")
    @PreAuthorize("hasAnyRole('ADMIN','PHARMACY')")
    public ResponseEntity<List<PharmacyStockDto>> getPharmacyStockByDrugId(@PathVariable Long drugId) {
        return ResponseEntity.ok(pharmacyStockService.getPharmacyStockByDrugId(drugId));
    }
}