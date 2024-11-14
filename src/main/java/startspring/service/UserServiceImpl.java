package startspring.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ValidationException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import startspring.common.exceptions.BadRequestException;
import startspring.common.exceptions.NotFoundResourceException;
import startspring.dto.LoginDto;
import startspring.dto.response.TokenResponse;
import startspring.entity.User;
import startspring.jwt.JwtTokenProvider;
import startspring.repository.UserRepository;
import startspring.service.converter.UserConverter;
import startspring.service.dto.TokenDto;
import startspring.service.dto.UserDto;
import startspring.service.redis.RedisDao;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;
    private final RedisDao redisDao;

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

        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(user.getUserId());

        // Redis TOKEN 저장
        // RedisDao 메서드로 대체 -> 이상 없는 경우 아래 주석 처리 삭제하겠습니다
        redisDao.setValues(tokenDto.getAccessToken(), tokenDto.getRefreshToken());
//        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
//        valueOperations.set(tokenDto.getAccessToken(), tokenDto.getRefreshToken());

        // 로그인시에 로그인한 아이디 기준으로 redis token 유무 확인하여
        // 재발급 처리하고 싶은데 이런 로직도 괜찮을까요?
        // 굳이 api를 따로 만들어야하나 싶어서요!

        return tokenDto;
    }

//    @Override
//    public TokenResponse reissue(TokenDto tokenDto) {
//        // 리프레시 토큰으로
//
//        // accessToken 가져오기
//        Boolean hasRefreshToken = redisTemplate.hasKey(tokenDto.getAccessToken());
//
//        // redis refreshToken 존재 여부 확인
//        if (Boolean.FALSE.equals(hasRefreshToken)) {
//            throw new NotFoundResourceException("Not Found access token");
//        }
//
//        // get(tokenDto.getAccessToken()) 했는데 없거나(null이거나) 가져온 refresh token이
//        //jwtTokenProvider.validateToken(refreshToken) 이 false거나 실패면 exception 처리 해야함.
//        if (isValidRefreshToken(tokenDto.getRefreshToken())) {
//            String accessToken = jwtTokenProvider.createAccessToken(tokenDto.getRefreshToken());
//
//            // 새로운 accessToken redis 저장
//            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
//            valueOperations.set(accessToken, tokenDto.getRefreshToken());
//
//            return new TokenResponse(accessToken);
//        }
//        throw new BadRequestException("Invalid access token");
//
//
//        /*
//         * Access token이 만료됐는지는 중요치 않음.
//         * Access token으로 refresh token이 존재하는 지 확인하고
//
//         * refresh token이 존재하며 아직 유요기간이 남아있다면
//         * Access token을 재 발급해서 response 줘야함.
//         *
//         * 안남아 있으면 Exception처리해서 재로그인 필요를 알려야함.
//         *
//         * get(tokenDto.getAccessToken()) 했는데 없거나(null이거나) 가져온 refresh token이
//         * jwtTokenProvider.validateToken(refreshToken) 이 false거나 실패면 exception 처리 해야함.
//         * 20241114 진행예정!
//         * 그리고 refresh token의 redis key를 새로 발급받은 access token으로 변경해준다.
//         *
//         * ps ) Token은 만료되면 안에있는 Subject를 못꺼냄. 그렇기 때문에 refresh token에서 Subject를 꺼내서 userId를 조회해야함.
//         * 그걸로 다시 access token 발급
//         */
//
//        return null;
//    }

//    private boolean isValidRefreshToken(String refreshToken) {
//        try {
//            return jwtTokenProvider.validateToken(refreshToken);
//        } catch (ValidationException e) {
//            return false;
//        }
//    }
}

