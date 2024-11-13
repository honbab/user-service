package startspring.service;

import jakarta.servlet.http.HttpServletRequest;
import startspring.dto.LoginDto;
import startspring.dto.response.TokenResponse;
import startspring.service.dto.TokenDto;
import startspring.service.dto.UserDto;

public interface UserService {

    UserDto signUp(UserDto userDto);

    TokenDto login(LoginDto loginDto, HttpServletRequest request);

    TokenResponse reissue(TokenDto tokenDto);
}
