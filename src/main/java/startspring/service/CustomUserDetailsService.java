package startspring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.jaas.AuthorityGranter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import startspring.repository.UserRepository;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        startspring.entity.User user = userRepository.findByUserId(userId).orElseThrow(() -> new UsernameNotFoundException(userId));
        return userDetails(user);
    }

    private UserDetails userDetails(startspring.entity.User user) {
        Set<SimpleGrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority("ROLE_USER"));
        return new User(user.getUserId(), user.getPassword(), authorities);
    }

//    private AuthorityGranter getAuthorityGranter(startspring.entity.User user) {
//
//    }

}
