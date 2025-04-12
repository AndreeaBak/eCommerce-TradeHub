package com.ase.service;


import com.ase.exception.WishlistNotFoundException;
import com.ase.model.Product;
import com.ase.model.User;
import com.ase.model.Wishlist;

import java.util.Optional;

public interface WishlistService {

    Wishlist createWishlist(User user);

    Wishlist getWishlistByUserId(User user);

    Wishlist addProductToWishlist(User user, Product product) throws WishlistNotFoundException;

}

