package startspring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import startspring.dto.LoginUserDto;
import startspring.dto.ResponseRequestDto;
import startspring.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 로그인
    public ResponseEntity<ResponseRequestDto> login(LoginUserDto loginUserDto) {
        return ResponseEntity.ok().body(userService.login(loginUserDto));
    }

}
