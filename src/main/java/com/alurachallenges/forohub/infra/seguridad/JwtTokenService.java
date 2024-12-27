package com.alurachallenges.forohub.infra.seguridad;

import com.alurachallenges.forohub.modelo.usuario.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class JwtTokenService {

    @Value("${api.security.secret}")
    private String apiSecret;

    public String generarToken(Usuario usuario) {
       try {
           Algorithm algorithm = Algorithm.HMAC256(apiSecret);
           return JWT.create()
                   .withIssuer("foro hub")
                   .withSubject(usuario.getEmail())
                   .withClaim("id",usuario.getId())
                   .withExpiresAt(generarFechaDeExpiracion())
                   .sign(algorithm);
       } catch (JWTCreationException exception){
           // Invalid Signing configuration / Couldn't convert Claims.
           throw new RuntimeException();
       }
    }

    public String obtenerUsuarioToken(String token){
        if (token == null){
            throw new RuntimeException();
        }
        DecodedJWT verifier = null;
        try {
            // Validando la firma
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            verifier = JWT.require(algorithm)
                    // specify any specific claim validations
                    .withIssuer("foro hub")
                    // reusable verifier instance
                    .build()
                    .verify(token);
            verifier.getSubject();
        } catch (JWTVerificationException exception){
            // Invalid signature/claims
            System.out.println(exception.toString());
        }

        if (verifier.getSubject() == null){
            throw new RuntimeException("Verifier invalido");
        }
        return verifier.getSubject();
    }

    public Long obtenerIdToken(String token){
        if (token == null){
            throw new RuntimeException();
        }

        DecodedJWT decodedToken = null;
        try {
            // Validando la firma
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            decodedToken= verifier.verify(token);

//            Long idUsuario = decodedToken.getClaim("id").asLong();
//            String email = decodedToken.getSubject();

        } catch (JWTVerificationException exception){
            // Invalid signature/claims
            System.out.println(exception.toString());
        }
        return decodedToken.getClaim("id").asLong();

    }

    private Instant generarFechaDeExpiracion() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-05:00"));
    }
}
