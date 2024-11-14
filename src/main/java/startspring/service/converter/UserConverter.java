package startspring.service.converter;

import startspring.entity.User;
import startspring.service.dto.UserDto;

public class UserConverter {
    private static UserConverter userConverter;

    private UserConverter() {
    }

    public static UserConverter of() {
        if (userConverter == null)
            userConverter = new UserConverter();
        return userConverter;
    }


    public User userDtoToUser(UserDto userDto) {
        return new User(userDto.userId(), userDto.password(), userDto.name(), userDto.email(), userDto.phoneNumber());
    }

    public UserDto userToUserDto(User userDto) {
        return new UserDto(userDto.getUserId(), userDto.getPassword(), userDto.getName(), userDto.getEmail(), userDto.getPhone());
    }
}
