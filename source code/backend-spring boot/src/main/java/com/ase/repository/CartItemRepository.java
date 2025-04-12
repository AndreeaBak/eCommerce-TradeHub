package com.ase.repository;

import com.ase.model.Cart;
import com.ase.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ase.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {


    CartItem findByCartAndProductAndSize(Cart cart, Product product, String size);


}
