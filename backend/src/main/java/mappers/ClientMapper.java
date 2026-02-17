package mappers;

import dtos.ClientDto;
import entities.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    @Mapping(target = "name", source = "fullName")
    @Mapping(target = "email", source = "users.email")
    ClientDto toClientDto(Client client);
}
