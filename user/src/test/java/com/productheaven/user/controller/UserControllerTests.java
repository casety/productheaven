package com.productheaven.user.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import com.productheaven.user.api.schema.response.UserDTO;
import com.productheaven.user.persistence.entity.User;
import com.productheaven.user.service.RequestValidationService;
import com.productheaven.user.service.UserService;
import com.productheaven.user.service.exception.InvalidRequestException;
import com.productheaven.user.service.exception.NoUsersFoundException;
import com.productheaven.user.service.exception.UserNotFoundException;

/**
 * @author mkaman
 * 
 **/

@SpringBootTest(webEnvironment = WebEnvironment.MOCK, classes = { UserController.class })
class UserControllerTests {
	
	private MockMvc mockMvc;

	@MockBean
	private UserService mockUserService;

	@MockBean
	private ModelMapper mockModelMapper;
	
	@MockBean
	private RequestValidationService mockValidationService;
	

	ModelMapper testMapper = new ModelMapper();
	
	@BeforeEach
	void init () {
		MockitoAnnotations.openMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(new UserController(mockUserService, mockModelMapper,mockValidationService)).build();
	}

	@Test
	void whenServiceReturnsData_usersShouldBeListedSuccessfully() throws Exception {
		//given
		List<User> result = new ArrayList<>();
		String randomId = UUID.randomUUID().toString();
		User user = new User(randomId);
		result.add(user);
		
		//when
		when(mockUserService.getAllUsers()).thenReturn(result);
		when(mockModelMapper.map(user, UserDTO.class)).thenReturn(testMapper.map(user, UserDTO.class));

		this.mockMvc.perform(get("/user")).andDo(print())
		//expect
		.andExpect(status().isOk())
		.andExpect(content().string(containsString(randomId)));
	}

	@Test
	void whenServiceReturnsNoData_NoUsersFoundExceptionShouldBeThrown() throws Exception {
		when(mockUserService.getAllUsers()).thenThrow(new NoUsersFoundException());
	    Exception thrown= assertThrows(NestedServletException.class, () -> {
	    	this.mockMvc.perform(get("/user"));
	    });
	    assertTrue(thrown.getCause() instanceof NoUsersFoundException);
	}

	@Test
	void usersShouldBeFetchedSuccessfully_forAGivenUserId() throws Exception {
		//given
		User user = new User();
		String randomId = UUID.randomUUID().toString();
		user.setId(randomId);
		
		//when
		when(mockUserService.getUser(randomId)).thenReturn(user);
		when(mockModelMapper.map(user, UserDTO.class)).thenReturn(testMapper.map(user, UserDTO.class));
		
		this.mockMvc.perform(get("/user/"+randomId)).andDo(print())
		
		//expect
		.andExpect(status().isOk())
		.andExpect(content().string(containsString(randomId)));
	}
	
	@Test 
	void whenServiceReturnsNoDataForAGivenUserId_UserNotFoundExceptionExceptionShouldBeThrown () throws UserNotFoundException {
		
		//given
		String randomId = UUID.randomUUID().toString();
		
		//when
		when(mockUserService.getUser(randomId)).thenThrow(new UserNotFoundException());

	    Exception thrown= assertThrows(NestedServletException.class, () -> {
	    	this.mockMvc.perform(get("/user/"+randomId));
	    });
	    assertTrue(thrown.getCause() instanceof UserNotFoundException);
	}
	
	@Test
	void whenRequestValidationFails_InvalidRequestExceptionShouldBeThrown() throws Exception {
		//given
		final String invalidId = "invalid-id";
		doThrow(new InvalidRequestException()).when(mockValidationService).validateUserId(invalidId);
		
		//then
	    Exception thrown= assertThrows(NestedServletException.class, () -> {
	    	this.mockMvc.perform(get("/user/"+invalidId));
	    });
	    assertTrue(thrown.getCause() instanceof InvalidRequestException);
	}
	
}
