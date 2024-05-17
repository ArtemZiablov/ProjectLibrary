package ua.karazin.interfaces.ProjectLibrary.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ua.karazin.interfaces.ProjectLibrary.services.CompositeUserDetailsService;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CompositeUserDetailsService compositeUserDetailsService;
    private final JWTFilter jwtFilter;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(compositeUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(authenticationProvider())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/email/send**", "/auth/login**", "/book/search**", "/book/info**", "/book/novelties", "auth/registration/admin",
                                "/auth/registration/multiple-readers**", "/auth/registration/multiple-librarians**").permitAll()

                        .requestMatchers("/book/add-book**", "/book-copy/add-book-copies**", "/book-copy/delete-book-copy",
                                "/book-copy/assign-book-copy", "/book-copy/release-book-copy**", "/reader/search**",
                                "/librarian/info**", "/librarian/photo**", "/book-operation**").hasRole("LIBRARIAN")

                        .requestMatchers("/book-copy/get-readers-books**", "/book-reservation/reserve-book**", "/reader/photo**").hasRole("READER")

                        .requestMatchers("/auth/registration/reader**", "/auth/registration/librarian**").hasRole("ADMIN")

                        .anyRequest().hasAnyRole("READER", "LIBRARIAN", "ADMIN")// Require authentication for all other requests
                )

                .rememberMe(rememberMe -> rememberMe.userDetailsService(compositeUserDetailsService))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login")
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
