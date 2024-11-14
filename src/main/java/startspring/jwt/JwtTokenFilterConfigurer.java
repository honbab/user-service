//package startspring.jwt;
//
//import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.web.DefaultSecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//public class JwtTokenFilterConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
//
//    private TokenProvider tokenProvider;
//
//    public JwtTokenFilterConfigurer(TokenProvider tokenProvider) {
//        this.tokenProvider = this.tokenProvider;
//    }
//
//    @Override
//    public void configure(HttpSecurity builder) throws Exception {
//        JwtTokenFilter customFilter = new JwtTokenFilter(tokenProvider);
//        builder.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
//    }
//}
//package startspring.jwt;
//
//import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.web.DefaultSecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//public class JwtTokenFilterConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
//
//    private TokenProvider tokenProvider;
//
//    public JwtTokenFilterConfigurer(TokenProvider tokenProvider) {
//        this.tokenProvider = this.tokenProvider;
//    }
//
//    @Override
//    public void configure(HttpSecurity builder) throws Exception {
//        JwtTokenFilter customFilter = new JwtTokenFilter(tokenProvider);
//        builder.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
//    }
//}
