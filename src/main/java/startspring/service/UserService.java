package startspring.service;

import startspring.dto.LoginUserDto;
import startspring.dto.ResponseRequestDto;

public interface UserService {
    ResponseRequestDto login(LoginUserDto loginUserDto);
}
