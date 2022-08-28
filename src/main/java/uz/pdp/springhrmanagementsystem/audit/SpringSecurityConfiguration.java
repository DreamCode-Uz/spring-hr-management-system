package uz.pdp.springhrmanagementsystem.audit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import uz.pdp.springhrmanagementsystem.service.AuthService;

import java.util.UUID;

@Configuration
@EnableWebSecurity
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class SpringSecurityConfiguration {

    private final AuthService authService;

    @Autowired
    public SpringSecurityConfiguration(AuthService authService) {
        this.authService = authService;
    }

    @Bean
    public AuditorAware<UUID> auditorAware() {
        return new SpringSecurityAuditingAware();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

//    @Bean
//    AuthenticationManager authenticationManager(AuthenticationManagerBuilder builder, PasswordEncoder passwordEncoder) throws Exception {
//        return builder.userDetailsService(authService).passwordEncoder(passwordEncoder).and().build();
//    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/auth/login", "/api/auth/verify").permitAll()
                .anyRequest()
                .authenticated()
                .and()
//                .addFilterBefore(null, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return http.build();
    }
}
