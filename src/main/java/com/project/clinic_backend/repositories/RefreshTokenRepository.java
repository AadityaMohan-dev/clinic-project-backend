package com.project.clinic_backend.repositories;

import com.project.clinic_backend.models.entities.RefreshToken;
import com.project.clinic_backend.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByUser(User user);
}