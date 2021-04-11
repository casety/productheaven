package com.productheaven.user.service;

import java.util.List;

import com.productheaven.user.persistence.entity.User;
import com.productheaven.user.service.exception.NoUsersFoundException;
import com.productheaven.user.service.exception.UserAlreadyExistsException;
import com.productheaven.user.service.exception.UserNotFoundException;

public interface UserService {

	List<User> getAllUsers() throws NoUsersFoundException;
	
	User getUser(String id) throws UserNotFoundException;
	
	User getUserByUsername(String username) throws UserNotFoundException;

	User registerNewUser(User entity) throws UserAlreadyExistsException;

}