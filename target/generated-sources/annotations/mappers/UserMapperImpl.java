package mappers;

import dtos.UserDto;
import entities.Roles;
import entities.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-05T14:09:20+0100",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto toUserDto(User user) {
        if ( user == null ) {
            return null;
        }

        Long id = null;
        String email = null;
        Roles role = null;

        id = user.getId();
        email = user.getEmail();
        role = user.getRole();

        UserDto userDto = new UserDto( id, email, role );

        return userDto;
    }
}
