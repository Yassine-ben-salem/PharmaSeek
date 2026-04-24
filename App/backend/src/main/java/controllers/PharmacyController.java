package controllers;

import dtos.PharmacyDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import services.PharmacyService;

@RestController
@RequestMapping("/pharmacies")
@AllArgsConstructor
@Tag(name="Pharmacy")
public class PharmacyController {

    private final PharmacyService pharmacyService;

    @PutMapping("/me")
    @PreAuthorize("hasRole('PHARMACY')")
    public ResponseEntity<PharmacyDto> updateMyPharmacy(
            @RequestBody PharmacyDto pharmacyDto,
            Authentication authentication
    ) {
        return pharmacyService.updatePharmacy(authentication, pharmacyDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}