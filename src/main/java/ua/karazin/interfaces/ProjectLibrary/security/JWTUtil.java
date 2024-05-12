package ua.karazin.interfaces.ProjectLibrary.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JWTUtil {

    @Value("jwtSecret")
    private String jwtSecret;

    public String generateToken(String fullName){
        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());

        return JWT.create()
                .withSubject("Reader details")
                .withClaim("username", fullName) // add role, id and change fullName to email
                .withIssuedAt(new Date())
                .withIssuer("ProjectLibrary")
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(jwtSecret));
    }

    public String verifyToken(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(jwtSecret))
                .withSubject("Reader details")
                .withIssuer("ProjectLibrary")
                .build();

        DecodedJWT jwt = verifier.verify(token);

        return jwt.getClaim("username").asString();
    }
}
