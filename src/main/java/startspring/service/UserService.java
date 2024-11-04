package startspring.service;

import jakarta.servlet.http.HttpServletRequest;
import startspring.dto.LoginDto;
import startspring.dto.request.LoginUserRequestDto;
import startspring.dto.response.TokenResponse;
import startspring.service.dto.UserDto;

public interface UserService {

    UserDto registerUser(UserDto userDto);

    TokenResponse login(LoginDto loginDto, HttpServletRequest request);
}
