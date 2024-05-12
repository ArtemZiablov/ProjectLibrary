package ua.karazin.interfaces.ProjectLibrary.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

import static org.springframework.security.config.Customizer.withDefaults;

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
                .csrf(csrf->csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login**", "/book/search**", "/book/info**"/*novelties*/).permitAll()
                        .requestMatchers("/book-copy/get-readers-books**"/*<-hasAnyRole*/, "book-reservation/reserve-book**", "reader/info**"/*<-hasAnyRole*/).hasRole("READER")
                        .requestMatchers("/book/add-book**", "/book-copy/add-book-copies**", "/book-copy/delete-book-copy", "/book-copy/assign-book-copy", "/book-copy/release-book-copy**", "/reader/search**").hasRole("LIBRARIAN")

                        //TODO: .requestMatchers("/auth/registration/reader**", "/auth/registration/librarian**").hasRole("ADMIN")

                        .anyRequest().hasAnyRole("READER", "LIBRARIAN"/*, "ADMIN"*/)// Require authentication for all other requests
                )
                /*.formLogin(form -> form
                        .loginPage("/auth/login")
                        .defaultSuccessUrl("/hello", true)
                        .failureUrl("/auth/login?error")
                        .permitAll() // Allow access to any user for default login page
                )*/
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
