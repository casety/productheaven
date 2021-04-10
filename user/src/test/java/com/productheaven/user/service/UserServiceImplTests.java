package com.productheaven.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.productheaven.user.persistence.entity.User;
import com.productheaven.user.persistence.repository.UserRepository;
import com.productheaven.user.service.exception.NoUsersFoundException;
import com.productheaven.user.service.impl.UserServiceImpl;

@ExtendWith(SpringExtension.class)
public class UserServiceImplTests {

	@InjectMocks
	private UserServiceImpl userService;
	
	@Mock
	private UserRepository userRepo;
	
	@Test
	void whenUserRepositoryReturnsData_shouldGetAllUsersBeSuccessfull() throws NoUsersFoundException {
		final String userNameGiven = "sample";
		List<User> serviceResult = new ArrayList<>();
		User user = new User();
		user.setName(userNameGiven);
		serviceResult.add(user);
		
		when(userRepo.findAll()).thenReturn(serviceResult);
		
		List<User> allUsers = userService.getAllUsers();
		assertNotNull(allUsers);
		assertEquals(1,allUsers.size());
		User returnedUser = allUsers.get(0);
		assertEquals(userNameGiven,returnedUser.getName());
	}
	
	@Test
	void whenUserRepositoryReturnsNoData_shouldGetAllUsersThrowNoUsersFoundException() throws NoUsersFoundException {
		when(userRepo.findAll()).thenReturn(new ArrayList<>());
	    assertThrows(NoUsersFoundException.class, () -> {
	    	userService.getAllUsers();
	    });
	}
}
