package startspring.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import startspring.common.exceptions.BadRequestException;
import startspring.common.exceptions.NotFoundResourceException;
import startspring.dto.LoginDto;
import startspring.entity.User;
import startspring.jwt.JwtTokenProvider;
import startspring.repository.UserRepository;
import startspring.service.converter.UserConverter;
import startspring.service.dto.TokenDto;
import startspring.service.dto.UserDto;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    // 회원가입
    @Override
    public UserDto signUp(UserDto userDto) {
        // 가입 아이디 유무 확인
        if(userRepository.existsByUserId(userDto.userId())) {
            throw new BadRequestException("User already exists");
        }
        // user Entity 변환
        User newUser = UserConverter.of().userDtoToUser(userDto);
        // 패스워드 암호화 처리
        newUser.setEncodePassword(passwordEncoder.encode(newUser.getPassword()));
        User savedUser = userRepository.save(newUser);

        return UserConverter.of().userToUserDto(savedUser);
    }

    @Override
    public TokenDto login(LoginDto loginDto, HttpServletRequest request) {
        // 유저 확인
        User user = userRepository.findByUserId(loginDto.id()).orElseThrow(() -> new NotFoundResourceException("Not Found User"));

        // 비밀번호 확인
        if(!passwordEncoder.matches(loginDto.password(), user.getPassword())) {
            throw new BadRequestException("Not Matched password");
        }

        // 액세스토큰 발급 추후 리프레시토큰 추가 예정
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(user.getUserId());

        // redis.save(tokenDto.getAccessToken(), tokenDto.getRefreshToken());

        return tokenDto;
    }
}
