package com.modugarden.domain.user.repository;

import com.modugarden.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository2 extends JpaRepository<User, Long>, CustomUserRepository2 {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);

    Boolean existsByNickname(String nickname);
}
