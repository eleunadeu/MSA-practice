package com.sparta.msa_eaxm.auth.repository;

import com.sparta.msa_eaxm.auth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
