package com.productheaven.user.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.productheaven.user.persistence.entity.User;
import com.productheaven.user.persistence.repository.UserRepository;
import com.productheaven.user.service.UserService;
import com.productheaven.user.service.exception.NoUsersFoundException;
import com.productheaven.user.service.exception.UserNotFoundException;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository repository;
	
	public UserServiceImpl(UserRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public List<User> getAllUsers() throws NoUsersFoundException {
		Iterable<User> users = repository.findAll();
		Iterator<User> iterator = users.iterator();
		if (!iterator.hasNext()) {
			throw new NoUsersFoundException();
		}
		List<User> returnList = new ArrayList<>();
		iterator.forEachRemaining(returnList::add);
		return returnList;
	}

	@Override
	public User getUser(String id) throws UserNotFoundException {
		Optional<User> result = repository.findById(id);
		if (!result.isPresent()) {
			throw new UserNotFoundException();
		}
		return result.get();
	}
}
