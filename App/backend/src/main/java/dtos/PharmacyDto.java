package dtos;

import lombok.Value;

@Value
public class PharmacyDto {
    Long id;
    String pharmacyName;
    String matriculeFiscale;
    String address;
    String email;
    String phone;
}
