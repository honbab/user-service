package startspring.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import startspring.dto.LoginDto;
import startspring.dto.response.TokenResponse;
import startspring.entity.User;
import startspring.repository.UserRepository;
import startspring.service.converter.UserConverter;
import startspring.service.dto.UserDto;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public TokenResponse login(LoginDto loginDto, HttpServletRequest request) {
        return null;
    }

    // 회원가입
    @Override
    public UserDto registerUser(UserDto userDto) {
        // user Entity 변환
        User newUser = UserConverter.of().userDtoToUser(userDto);
        // 패스워드 암호화 처리
        newUser.setEncodePassword(passwordEncoder.encode(newUser.getPassword()));
        User savedUser = userRepository.save(newUser);

        return UserConverter.of().userToUserDto(savedUser);
    }
}
