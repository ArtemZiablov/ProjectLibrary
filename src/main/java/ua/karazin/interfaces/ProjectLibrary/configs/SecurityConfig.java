package ua.karazin.interfaces.ProjectLibrary.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ua.karazin.interfaces.ProjectLibrary.services.CompositeUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CompositeUserDetailsService compositeUserDetailsService;

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
    SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(authenticationProvider())
                .csrf(csrf->csrf.disable())
                .authorizeHttpRequests(auth -> auth
                                .anyRequest().permitAll() // TODO: temporally
                        /*.requestMatchers("/book/search**", "/book/info**"*//*novelties*//*).permitAll()
                        .requestMatchers("/book-copy/get-readers-books**"*//*<-hasAnyRole*//*, "book-reservation/reserve-book**", "reader/info**"*//*<-hasAnyRole*//*).hasRole("READER")
                        .requestMatchers("/book/add-book**", "/book-copy/add-book-copies**", "/book-copy/delete-book-copy", "/book-copy/assign-book-copy", "/book-copy/release-book-copy**", "/reader/search**").hasRole("LIBRARIAN")


                        .anyRequest().hasAnyRole("READER", "LIBRARIAN"*//*, "ADMIN"*//*)// Require authentication for all other requests*/
                )
                .formLogin(form -> form
                        .permitAll() // Allow access to any user for default login page
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login?logout=true")
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                        .permitAll()
                );


        return http.build();
    }
}
