package com.tasc.blogging.repository;

import com.tasc.blogging.entity.user.User;
import com.tasc.blogging.entity.enums.BaseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query(value = "SELECT u FROM user u WHERE u.email = ?1 AND u.status = ?2", nativeQuery = true)
    Optional<User> findByEmailAndStatus(String username, BaseStatus status);

    Boolean existsByEmail(String username);

    Optional<User> findByVerificationCode(String verificationCode);
}