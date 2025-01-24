package com.potential.api.repository;

import com.potential.api.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUserName(String userName);

    boolean existsByEmail(String email);

    Optional<User> findByUserName(String userName);

    Optional<User> findByEmail(String email);
}
