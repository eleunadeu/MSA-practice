package com.sparta.msa_eaxm.auth.service;

import com.sparta.msa_eaxm.auth.domain.SignInRequest;
import com.sparta.msa_eaxm.auth.domain.SignUpRequest;
import com.sparta.msa_eaxm.auth.domain.User;
import com.sparta.msa_eaxm.auth.repository.AuthRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Service
public class AuthService {

    @Value("${spring.application.name}")
    private String issuer;

    @Value("${service.jwt.access-expiration}")
    private Long accessExpiration;

    private final SecretKey secretKey;
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(@Value("${service.jwt.secret-key}") String secretKey,
                       AuthRepository authRepository, PasswordEncoder passwordEncoder) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void signUp(SignUpRequest signUpRequest) {
        if (authRepository.findByUsername(signUpRequest.getUsername()).isPresent()) {
            throw new IllegalArgumentException("존재하는 사용자입니다.");
        }

        String password = passwordEncoder.encode(signUpRequest.getPassword());
        User user = new User(signUpRequest.getUsername(), password);
        authRepository.save(user);
    }

    public String signIn(SignInRequest signInRequest) {
       User user = authRepository.findByUsername(signInRequest.getUsername())
               .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

       if (!passwordEncoder.matches(signInRequest.getPassword(), user.getPassword())) {
           throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
       }

       return createAccessToken(user.getUsername());
    }

    private String createAccessToken(String userId) {
        return Jwts.builder()
                .claim("user_id", userId)
                .claim("role", "USER")
                .issuer(issuer)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessExpiration))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }
}
