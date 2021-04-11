package com.productheaven.user.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.productheaven.user.persistence.entity.User;
import com.productheaven.user.persistence.repository.UserRepository;
import com.productheaven.user.service.UserService;
import com.productheaven.user.service.exception.NoUsersFoundException;
import com.productheaven.user.service.exception.UserAlreadyExistsException;
import com.productheaven.user.service.exception.UserNotFoundException;

@Service
@Transactional
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
	
	@Override
	public User registerNewUser(User entity) throws UserAlreadyExistsException {
		String id = UUID.randomUUID().toString();
		List<User> currentUserRecords = repository.findByUsernameOrEmail(entity.getUsername(), entity.getEmail());
		if (currentUserRecords != null && !currentUserRecords.isEmpty()) {
			throw new UserAlreadyExistsException();
		}
		entity.setCreateTime(new Date());
		entity.setId(id);
		// work factor
//		final int strength = 10; 
//		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(strength, new SecureRandom());
//		entity.setPassword(bCryptPasswordEncoder.encode(entity.getPassword()));
		return repository.save(entity);
		
	}

	@Override
	public User getUserByUsername(String username) throws UserNotFoundException {
		List<User> userList = repository.findByUsername(username);
		if (userList==null || userList.isEmpty()) {
			throw new UserNotFoundException();
		}
		return userList.get(0);
	}
}
