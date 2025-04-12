package com.ase.service;

import java.util.List;

import com.ase.exception.UserException;
import com.ase.model.User;

public interface UserService {

	public User findUserProfileByJwt(String jwt) throws UserException;
	
	public User findUserByEmail(String email) throws UserException;


}
