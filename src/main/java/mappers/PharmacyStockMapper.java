package mappers;

import dtos.PharmacyStockDto;
import entities.PharmacyStock;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface PharmacyStockMapper {
    @Mapping(target = "pharmacyId", source = "pharmacy.id")
    @Mapping(target = "drugId", source = "drug.id")
    PharmacyStockDto toPharmacyStockDto(PharmacyStock pharmacyStock);
    PharmacyStock toPharmacyStock(PharmacyStockDto pharmacyStockDto);
}