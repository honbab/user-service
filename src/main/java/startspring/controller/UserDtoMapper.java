package startspring.controller;

import org.modelmapper.ModelMapper;
import startspring.dto.LoginDto;
import startspring.dto.request.LoginUserRequestDto;
import startspring.dto.request.RegisterUserRequestDto;
import startspring.dto.response.RegisterResponse;
import startspring.service.dto.UserDto;

public class UserDtoMapper {

    private static UserDtoMapper userDtoMapper;
    private final ModelMapper modelMapper;

    public UserDtoMapper() {
        modelMapper = new ModelMapper();
    }

    public static UserDtoMapper of() {
        if (userDtoMapper == null)  userDtoMapper = new UserDtoMapper();
        return userDtoMapper;
    }

    public LoginDto loginRequestDto(LoginUserRequestDto loginUserDto) {
       return new LoginDto(loginUserDto.id(), loginUserDto.password());
    }

    public UserDto registerRequestDto(RegisterUserRequestDto registerUserRequest) {
        return new UserDto(registerUserRequest.userId(), registerUserRequest.password(), registerUserRequest.name(), registerUserRequest.email(), registerUserRequest.phoneNumber());
    }

    public RegisterResponse RegisterResponse(UserDto savedUser) {
        return new RegisterResponse(savedUser.userId());
    }
}
