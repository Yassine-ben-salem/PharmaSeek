package dtos;

import lombok.Value;

@Value
public class PharmacyDto {
    Long id;
    String pharmacyName;
    String taxId;
    String email;
    String address;
}
