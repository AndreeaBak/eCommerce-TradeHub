package com.ase.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ase.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
