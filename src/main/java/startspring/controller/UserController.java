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
import startspring.service.dto.TokenDto;
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
    public ResponseEntity<TokenDto> login(@RequestBody LoginUserRequestDto loginUserDto, HttpServletRequest request) {
        LoginDto loginDto = UserDtoMapper.of().loginRequestDto(loginUserDto);
        TokenDto loginSuccessToken = userService.login(loginDto, request);
        return ResponseEntity.ok().body(loginSuccessToken);
    }

    // 토큰 reissue
    @PostMapping(GlobalURI.USER_REISSUE_TOKEN_URI) // Access token이 만료됐을때 클라이언트 (프런트 개발자)가 요청할 url : Access Token 재발급
    public ResponseEntity<TokenResponse> reissueToken (@RequestBody TokenDto tokenDto) {
        return ResponseEntity.ok(userService.reissue(tokenDto));
    }


}
