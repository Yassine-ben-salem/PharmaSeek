package mappers;

import dtos.PharmacyStockDto;
import entities.Drug;
import entities.Pharmacy;
import entities.PharmacyStock;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-05T14:09:19+0100",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
)
@Component
public class PharmacyStockMapperImpl implements PharmacyStockMapper {

    @Override
    public PharmacyStockDto toPharmacyStockDto(PharmacyStock pharmacyStock) {
        if ( pharmacyStock == null ) {
            return null;
        }

        PharmacyStockDto pharmacyStockDto = new PharmacyStockDto();

        pharmacyStockDto.setPharmacyId( pharmacyStockPharmacyId( pharmacyStock ) );
        pharmacyStockDto.setDrugId( pharmacyStockDrugId( pharmacyStock ) );
        pharmacyStockDto.setId( pharmacyStock.getId() );
        pharmacyStockDto.setQuantity( pharmacyStock.getQuantity() );
        pharmacyStockDto.setPrice( pharmacyStock.getPrice() );
        pharmacyStockDto.setReservationDelayMinutes( pharmacyStock.getReservationDelayMinutes() );

        return pharmacyStockDto;
    }

    @Override
    public PharmacyStock toPharmacyStock(PharmacyStockDto pharmacyStockDto) {
        if ( pharmacyStockDto == null ) {
            return null;
        }

        PharmacyStock pharmacyStock = new PharmacyStock();

        pharmacyStock.setId( pharmacyStockDto.getId() );
        pharmacyStock.setQuantity( pharmacyStockDto.getQuantity() );
        pharmacyStock.setPrice( pharmacyStockDto.getPrice() );
        pharmacyStock.setReservationDelayMinutes( pharmacyStockDto.getReservationDelayMinutes() );

        return pharmacyStock;
    }

    private Long pharmacyStockPharmacyId(PharmacyStock pharmacyStock) {
        Pharmacy pharmacy = pharmacyStock.getPharmacy();
        if ( pharmacy == null ) {
            return null;
        }
        return pharmacy.getId();
    }

    private Long pharmacyStockDrugId(PharmacyStock pharmacyStock) {
        Drug drug = pharmacyStock.getDrug();
        if ( drug == null ) {
            return null;
        }
        return drug.getId();
    }
}
