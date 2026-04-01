package mappers;

import dtos.ReservationDto;
import entities.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservationMapper {
    @Mapping(target = "clientId", source = "client.id")
    @Mapping(target = "pharmacyId", source = "pharmacy.id")
    ReservationDto toReservationDto(Reservation reservation);
}

