package mappers;

import dtos.PharmacyDto;
import entities.Pharmacy;
import entities.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-05T14:09:20+0100",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
)
@Component
public class PharmacyMapperImpl implements PharmacyMapper {

    @Override
    public PharmacyDto toPharmacyDto(Pharmacy pharmacy) {
        if ( pharmacy == null ) {
            return null;
        }

        String taxId = null;
        String email = null;
        Long id = null;
        String pharmacyName = null;
        String address = null;

        taxId = pharmacy.getTaxId();
        email = pharmacyUserEmail( pharmacy );
        id = pharmacy.getId();
        pharmacyName = pharmacy.getPharmacyName();
        address = pharmacy.getAddress();

        PharmacyDto pharmacyDto = new PharmacyDto( id, pharmacyName, taxId, email, address );

        return pharmacyDto;
    }

    private String pharmacyUserEmail(Pharmacy pharmacy) {
        User user = pharmacy.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getEmail();
    }
}
