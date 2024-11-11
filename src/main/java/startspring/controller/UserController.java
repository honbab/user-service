package startspring.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import startspring.common.GlobalURI;
import startspring.dto.LoginDto;
import startspring.dto.request.LoginUserRequestDto;
import startspring.dto.request.RegisterUserRequestDto;
import startspring.dto.response.RegisterResponse;
import startspring.dto.response.TokenResponse;
import startspring.service.UserService;
import startspring.service.dto.UserDto;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 가입
    @PostMapping(GlobalURI.USER_ROOT)
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterUserRequestDto registerUserRequest) {
        UserDto userDto = UserDtoMapper.of().registerRequestDto(registerUserRequest);
        UserDto savedUser = userService.signUp(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserDtoMapper.of().RegisterResponse(savedUser));
    }

    // 로그인
    @PostMapping(GlobalURI.USER_LOGIN_URI)
    public ResponseEntity<TokenResponse> login(@RequestBody LoginUserRequestDto loginUserDto, HttpServletRequest request) {
        LoginDto loginDto = UserDtoMapper.of().loginRequestDto(loginUserDto);
        TokenResponse loginSuccessToken = userService.login(loginDto, request);
        return ResponseEntity.ok().body(loginSuccessToken);
    }
}
