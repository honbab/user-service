package startspring.service;

import jakarta.servlet.http.HttpServletRequest;
import startspring.dto.LoginDto;
import startspring.dto.response.TokenResponse;
import startspring.service.dto.UserDto;

public interface UserService {
    TokenResponse login(LoginDto loginDto, HttpServletRequest request);

    UserDto registerUser(UserDto userDto);
}
