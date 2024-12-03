package com.sparta.msa_eaxm.gateway;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "auth-service")
public interface AuthClient {

    @PostMapping("/auth/validate")
    boolean validateToken(String token);
}
