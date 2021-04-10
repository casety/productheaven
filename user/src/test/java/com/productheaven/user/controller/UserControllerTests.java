package com.productheaven.user.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import com.productheaven.user.api.schema.response.UserDTO;
import com.productheaven.user.api.schema.response.UserResponseDTO;
import com.productheaven.user.persistence.entity.User;
import com.productheaven.user.service.UserService;
import com.productheaven.user.service.exception.NoUsersFoundException;

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

	ModelMapper testMapper = new ModelMapper();
	
	@BeforeEach
	void init () {
		MockitoAnnotations.openMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(new UserController(mockUserService, mockModelMapper)).build();
	}

	@Test
	void whenServiceReturnsData_usersShouldBeListedSuccessfully() throws Exception {
		//prepare mock user
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
		User user = new User();
		String randomId = UUID.randomUUID().toString();
		user.setId(randomId);
		when(mockModelMapper.map(user, UserResponseDTO.class)).thenReturn(testMapper.map(user, UserResponseDTO.class));
		this.mockMvc.perform(get("/user/"+randomId)).andDo(print())
		
		//then
		.andExpect(status().isOk())
		.andExpect(content().string(containsString(randomId)));
	}
}
