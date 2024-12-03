package com.sparta.msa_eaxm.auth.domain;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

    private String username;
    private String password;
}
