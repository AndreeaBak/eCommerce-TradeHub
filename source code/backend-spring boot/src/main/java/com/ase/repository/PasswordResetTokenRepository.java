package com.ase.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ase.model.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Integer> {
	PasswordResetToken findByToken(String token);
}
