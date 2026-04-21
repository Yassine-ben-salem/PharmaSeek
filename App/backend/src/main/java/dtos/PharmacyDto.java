package dtos;

import entities.PharmacyApprovalStatus;
import lombok.Value;

@Value
public class PharmacyDto {
    Long id;
    String pharmacyName;
    String taxId;
    String email;
    String address;
    PharmacyApprovalStatus approvalStatus;
}
