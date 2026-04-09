package mappers;

import dtos.ClientDto;
import entities.Client;
import entities.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-07T12:43:04+0100",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.2 (Oracle Corporation)"
)
@Component
public class ClientMapperImpl implements ClientMapper {

    @Override
    public ClientDto toClientDto(Client client) {
        if ( client == null ) {
            return null;
        }

        String name = null;
        String email = null;
        String phone = null;
        Long id = null;

        name = clientUserName( client );
        email = clientUserEmail( client );
        phone = clientUserPhone( client );
        id = client.getId();

        ClientDto clientDto = new ClientDto( id, name, email, phone );

        return clientDto;
    }

    private String clientUserName(Client client) {
        User user = client.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getName();
    }

    private String clientUserEmail(Client client) {
        User user = client.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getEmail();
    }

    private String clientUserPhone(Client client) {
        User user = client.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getPhone();
    }
}
