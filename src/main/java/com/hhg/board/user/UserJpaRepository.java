package com.hhg.board.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User,Long> {
    @Query("SELECT u FROM User u WHERE u.userLoginId = :userLoginId AND u.userPassword = :password")
    Optional<User> findByUsernameAndPassword(@Param("userLoginId") String username,
                                             @Param("password") String password);
}
