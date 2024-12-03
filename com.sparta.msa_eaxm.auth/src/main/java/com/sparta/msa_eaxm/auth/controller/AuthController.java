package com.sparta.msa_eaxm.auth.controller;

import com.sparta.msa_eaxm.auth.domain.SignInRequest;
import com.sparta.msa_eaxm.auth.domain.SignUpRequest;
import com.sparta.msa_eaxm.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@RequestBody SignUpRequest signUpRequest) {
        authService.signUp(signUpRequest);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }

    @PostMapping("/signIn")
    public ResponseEntity<String> signIn(@RequestBody SignInRequest signInRequest) {
        String token = authService.signIn(signInRequest);

        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + token)
                .body("로그인 완료");
    }

    @PostMapping("/validate")
    public boolean validateAuthToken(@RequestParam("token") String token) {
        return authService.validateToken(token);
    }
}