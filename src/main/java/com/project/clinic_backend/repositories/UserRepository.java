package com.project.clinic_backend.repositories;

import com.project.clinic_backend.models.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(@Email(message = "Invalid email format") @NotBlank(message = "Email is required") String email);

    Optional<User> findByEmail(String email);
}
