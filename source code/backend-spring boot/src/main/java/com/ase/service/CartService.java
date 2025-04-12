package com.ase.service;

import com.ase.exception.ProductException;
import com.ase.model.Cart;
import com.ase.model.CartItem;
import com.ase.model.Product;
import com.ase.model.User;

public interface CartService {
	
	public CartItem addCartItem(User user,
								Product product,
								String size,
								int quantity) throws ProductException;
	
	public Cart findUserCart(User user);

}
