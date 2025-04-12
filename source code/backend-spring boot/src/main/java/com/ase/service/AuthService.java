package com.ase.service;

import com.ase.exception.SellerException;
import com.ase.exception.UserException;
import com.ase.request.LoginRequest;
import com.ase.request.SignupRequest;
import com.ase.response.AuthResponse;
import jakarta.mail.MessagingException;

public interface AuthService {

    void sentLoginOtp(String email) throws UserException, MessagingException;
    String createUser(SignupRequest req) throws SellerException;
    AuthResponse signin(LoginRequest req) throws SellerException;

}