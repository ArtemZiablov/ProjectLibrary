package ua.karazin.interfaces.ProjectLibrary.configs;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ua.karazin.interfaces.ProjectLibrary.security.JWTUtil;
import ua.karazin.interfaces.ProjectLibrary.services.CompositeUserDetailsService;

import java.io.IOException;

@Slf4j(topic = "JWTFilter")
@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final CompositeUserDetailsService compositeUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);

            if (jwt.isBlank())
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT token in Bearer header");
            else {
                try {
                    String username = jwtUtil.verifyToken(jwt);
                    log.info("username {}", username);
                    UserDetails userDetails = compositeUserDetailsService.loadUserByUsername(username);

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails,
                                    userDetails.getPassword(),
                                    userDetails.getAuthorities());

                    if (SecurityContextHolder.getContext().getAuthentication() == null)
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                } catch (JWTVerificationException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                            "Invalid JWT Token");
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
