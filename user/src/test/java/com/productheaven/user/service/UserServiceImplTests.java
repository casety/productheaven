package com.productheaven.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.productheaven.user.persistence.entity.User;
import com.productheaven.user.persistence.repository.UserRepository;
import com.productheaven.user.service.exception.UserAlreadyExistsException;
import com.productheaven.user.service.exception.UserNotFoundException;
import com.productheaven.user.service.impl.UserServiceImpl;
import com.productheaven.user.util.TestUtils;

@ExtendWith(SpringExtension.class)
class UserServiceImplTests {

	@InjectMocks
	private UserServiceImpl userService;
	
	@Mock
	private UserRepository userRepo;

	@Test
	void usersShouldBeFetchedByIdSuccessfully() throws UserNotFoundException {
		User givenUser = TestUtils.createUserEntity();
		when(userRepo.findById(givenUser.getId())).thenReturn(Optional.of(givenUser));
		User serviceResult = userService.getUser(givenUser.getId());
		assertNotNull(serviceResult);
		assertEquals(givenUser,serviceResult);
	}
	
	@Test
	void usersShouldBeFetchedByUsernameSuccessfully() throws UserNotFoundException {
		User givenUser = TestUtils.createUserEntity();
		List<User> mockList = List.of(givenUser);
		when(userRepo.findByUsername(givenUser.getUsername())).thenReturn(mockList);
		User serviceResult = userService.getUserByUsername(givenUser.getUsername());
		assertNotNull(serviceResult);
		assertEquals(givenUser,serviceResult);
	}
	
	@Test
	void givenId_whenNoUserFound_throwsUserNotFoundException() throws UserNotFoundException {
		final String sampleId = "sampleId";
		when(userRepo.findById(sampleId)).thenReturn(Optional.ofNullable(null));
		assertThrows(UserNotFoundException.class, () -> {
			userService.getUser(sampleId);
		});
	}
	
	@Test
	void givenUserName_whenNoUserFound_throwsUserNotFoundException() throws UserNotFoundException {
		final String sampleUsername = "sampleUsername";
		when(userRepo.findByUsername(sampleUsername)).thenReturn(List.of());
		assertThrows(UserNotFoundException.class, () -> {
			userService.getUserByUsername(sampleUsername);
		});
	}
	
	@Test
	void givenUser_userRegistrationCompletesSuccessfully() throws UserAlreadyExistsException {
		User testUserEntity = TestUtils.createUserEntity();
		when(userRepo.save(any())).thenReturn(testUserEntity);
		User registeredNewUser = userService.registerNewUser(testUserEntity);
		assertEquals(testUserEntity, registeredNewUser);
	}
	
	@Test
	void givenUser_whenUserAlreadyRegistered_throwsUserAlreadyExistsException() throws UserAlreadyExistsException {
		User testUserEntity = TestUtils.createUserEntity();
		when(userRepo.findByUsernameOrEmail(testUserEntity.getUsername(), testUserEntity.getEmail())).thenReturn(List.of(new User()));
		assertThrows(UserAlreadyExistsException.class, () -> {
			userService.registerNewUser(testUserEntity);
		});

	}
}
