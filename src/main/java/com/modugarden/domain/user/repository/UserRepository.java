package com.modugarden.domain.user.repository;

import com.modugarden.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long>, CustomUserRepository {

    Slice<User> findByNicknameLike(@Param("nickname") String nickname, Pageable pageable);
}

