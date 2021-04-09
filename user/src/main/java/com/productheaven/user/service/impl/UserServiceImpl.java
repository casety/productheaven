package com.productheaven.user.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.productheaven.user.persistence.entity.User;
import com.productheaven.user.service.UserService;
import com.productheaven.user.service.exception.NoUsersFoundException;

@Service
public class UserServiceImpl implements UserService {

	@Override
	public List<User> getAllUsers() throws NoUsersFoundException {
		return new ArrayList<>();
	}

}
