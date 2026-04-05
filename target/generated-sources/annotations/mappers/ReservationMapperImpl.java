package mappers;

import dtos.ReservationDto;
import entities.Client;
import entities.Pharmacy;
import entities.Reservation;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-05T14:09:20+0100",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
)
@Component
public class ReservationMapperImpl implements ReservationMapper {

    @Override
    public ReservationDto toReservationDto(Reservation reservation) {
        if ( reservation == null ) {
            return null;
        }

        ReservationDto reservationDto = new ReservationDto();

        reservationDto.setClientId( reservationClientId( reservation ) );
        reservationDto.setPharmacyId( reservationPharmacyId( reservation ) );
        reservationDto.setId( reservation.getId() );
        reservationDto.setStatus( reservation.getStatus() );
        reservationDto.setTotalPrice( reservation.getTotalPrice() );
        reservationDto.setReservedAt( reservation.getReservedAt() );
        reservationDto.setExpirationTime( reservation.getExpirationTime() );

        return reservationDto;
    }

    private Long reservationClientId(Reservation reservation) {
        Client client = reservation.getClient();
        if ( client == null ) {
            return null;
        }
        return client.getId();
    }

    private Long reservationPharmacyId(Reservation reservation) {
        Pharmacy pharmacy = reservation.getPharmacy();
        if ( pharmacy == null ) {
            return null;
        }
        return pharmacy.getId();
    }
}
