package mappers;

import dtos.PharmacyDto;
import entities.Pharmacy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PharmacyMapper {
    @Mapping(target = "email", source = "users.email")
    PharmacyDto toPharmacyDto(Pharmacy pharmacy);
}
