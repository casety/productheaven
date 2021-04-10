package com.productheaven.user.service;

import java.util.List;

import com.productheaven.user.persistence.entity.User;
import com.productheaven.user.service.exception.NoUsersFoundException;
import com.productheaven.user.service.exception.UserNotFoundException;

public interface UserService {

	List<User> getAllUsers() throws NoUsersFoundException;
	
	User getUser(String id) throws UserNotFoundException;

}