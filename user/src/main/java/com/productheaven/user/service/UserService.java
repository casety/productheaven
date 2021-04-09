package com.productheaven.user.service;

import java.util.List;

import com.productheaven.user.persistence.entity.User;
import com.productheaven.user.service.exception.NoUsersFoundException;

public interface UserService {

	List<User> getAllUsers() throws NoUsersFoundException;

}