package com.ase.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ase.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {



}
