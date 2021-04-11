package com.productheaven.user.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import com.productheaven.user.api.schema.request.UserCreateRequestDTO;
import com.productheaven.user.api.schema.response.UserDTO;
import com.productheaven.user.persistence.entity.User;
import com.productheaven.user.service.RequestValidationService;
import com.productheaven.user.service.UserService;
import com.productheaven.user.service.exception.InvalidRequestException;
import com.productheaven.user.service.exception.NoUsersFoundException;
import com.productheaven.user.service.exception.UserNotFoundException;
import com.productheaven.user.util.TestUtils;

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

	private ModelMapper testMapper = new ModelMapper();

	@BeforeEach
	void init() {
		MockitoAnnotations.openMocks(this);
		this.mockMvc = MockMvcBuilders
				.standaloneSetup(new UserController(mockUserService, mockModelMapper, mockValidationService)).build();
	}

	@Test
	void whenServiceReturnsData_usersToBeListedSuccessfully() throws Exception {
		// given
		List<User> result = new ArrayList<>();
		String randomId = UUID.randomUUID().toString();
		User user = new User(randomId);
		result.add(user);

		// when
		when(mockUserService.getAllUsers()).thenReturn(result);
		when(mockModelMapper.map(user, UserDTO.class)).thenReturn(testMapper.map(user, UserDTO.class));

		// expect
		this.mockMvc
		.perform(get("/user")).andDo(print())
		.andExpect(status().isOk())
		
		//.andExpect(MockMvcResultMatchers.jsonPath("$.user.id",is(mockId)))

		.andExpect(content().string(containsString(randomId)));
	}

	@Test
	void whenServiceReturnsNoData_throwsNoUsersFoundException() throws Exception {
		when(mockUserService.getAllUsers()).thenThrow(new NoUsersFoundException());
		Exception thrown = assertThrows(NestedServletException.class, () -> {
			this.mockMvc.perform(get("/user"));
		});
		assertTrue(thrown.getCause() instanceof NoUsersFoundException);
	}

	@Test
	void givenUserId_usersShouldBeFetchedSuccessfully() throws Exception {
		// given
		User user = new User();
		String randomId = UUID.randomUUID().toString();
		user.setId(randomId);

		// when
		when(mockUserService.getUser(randomId)).thenReturn(user);
		when(mockModelMapper.map(user, UserDTO.class)).thenReturn(testMapper.map(user, UserDTO.class));

		// expect
		this.mockMvc
		.perform(get("/user/" + randomId))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(content().string(containsString(randomId)));
	}
	
	@Test
	void givenUsername_usersShouldBeFetchedSuccessfully() throws Exception {
		// given
		User user = new User();
		String randomId = UUID.randomUUID().toString();
		String sampleusername = "sampleusername";
		user.setId(randomId);
		user.setUsername(sampleusername);

		// when
		when(mockUserService.getUserByUsername(sampleusername)).thenReturn(user);
		when(mockModelMapper.map(user, UserDTO.class)).thenReturn(testMapper.map(user, UserDTO.class));

		// expect
		this.mockMvc
		.perform(get("/user-by-username/" + sampleusername))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.user.id",is(randomId)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.user.username",is(sampleusername)));
	}

	@Test
	void givenUserId_whenServiceReturnsNoData_throwsUserNotFoundException()	throws UserNotFoundException {
		// given
		String randomId = UUID.randomUUID().toString();

		// when
		when(mockUserService.getUser(randomId)).thenThrow(new UserNotFoundException());

		Exception thrown = assertThrows(NestedServletException.class, () -> {
			this.mockMvc.perform(get("/user/" + randomId));
		});
		assertTrue(thrown.getCause() instanceof UserNotFoundException);
	}
	
	@Test
	void givenUsername_whenServiceReturnsNoData_throwsUserNotFoundException()	throws UserNotFoundException {

		// given
		String sampleusername = "sampleusername";

		// when
		when(mockUserService.getUserByUsername(sampleusername)).thenThrow(new UserNotFoundException());

		Exception thrown = assertThrows(NestedServletException.class, () -> {
			this.mockMvc.perform(get("/user-by-username/" + sampleusername));
		});
		assertTrue(thrown.getCause() instanceof UserNotFoundException);
	}

	@Test
	void whenRequestValidationFails_throwsInvalidRequestException() throws Exception {
		// given
		final String invalidId = "invalid-id";
		doThrow(new InvalidRequestException()).when(mockValidationService).validateUserId(invalidId);

		// then
		Exception thrown = assertThrows(NestedServletException.class, () -> {
			this.mockMvc.perform(get("/user/" + invalidId));
		});
		assertTrue(thrown.getCause() instanceof InvalidRequestException);
	}

	@Test
	void givenUserShouldBeCreatedSuccessfully() throws Exception {
		// given
		UserCreateRequestDTO userRequestDTO = new UserCreateRequestDTO("sampleUsername","test@sampleemail.com","samplename","samplesurname","samplepasshashed");
		String mockId = UUID.randomUUID().toString();
		User mappedMockUser = testMapper.map(userRequestDTO, User.class);
		mappedMockUser.setId(mockId);
		UserDTO mappedMockUserDTO = testMapper.map(mappedMockUser, UserDTO.class);
		
		// when
		when(mockModelMapper.map(userRequestDTO, User.class)).thenReturn(mappedMockUser);
		when(mockUserService.registerNewUser(mappedMockUser)).thenReturn(mappedMockUser);
		when(mockModelMapper.map(mappedMockUser, UserDTO.class)).thenReturn(mappedMockUserDTO);

		//expect
		this.mockMvc
				.perform(post("/user").content(TestUtils.objectToJsonString(userRequestDTO))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.user.id",is(mockId)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.user.username",is("sampleUsername")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.user.name",is("samplename")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.user.email",is("test@sampleemail.com")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.user.surname",is("samplesurname")));
	}

}
