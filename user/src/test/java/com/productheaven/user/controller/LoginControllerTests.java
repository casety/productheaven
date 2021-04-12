package com.productheaven.user.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import com.productheaven.user.api.schema.request.UserLoginRequestDTO;
import com.productheaven.user.service.LoginService;
import com.productheaven.user.service.exception.LoginAttemptFailedException;
import com.productheaven.user.util.TestUtils;

/**
 * @author mkaman
 * 
 **/

@SpringBootTest(webEnvironment = WebEnvironment.MOCK, classes = { LoginController.class })
class LoginControllerTests {

	private MockMvc mockMvc;

	@MockBean
	private LoginService loginService;

	@BeforeEach
	void init() {
		MockitoAnnotations.openMocks(this);
		this.mockMvc = MockMvcBuilders
				.standaloneSetup(new LoginController(loginService)).build();
	}

	@Test
	void whenServiceCompletesSuccessfully_loginOperationEndsSuccessfully() throws Exception  {
		
		UserLoginRequestDTO userLoginRequestDTO = new UserLoginRequestDTO("test", "test");
		
		//expect
		this.mockMvc
				.perform(post("/login")
						.content(TestUtils.objectToJsonString(userLoginRequestDTO))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		
	}
	
	@Test
	void whenServiceThrowsLoginAttemptFailedException_loginOperationThrowsLoginAttemptFailedException() throws Exception  {
		
		UserLoginRequestDTO userLoginRequestDTO = new UserLoginRequestDTO("test", "test");

		Mockito.doThrow(LoginAttemptFailedException.class)
	       .when(loginService).loginWith("test", "test");
		
		Exception thrown = assertThrows(NestedServletException.class, () -> {
			this.mockMvc
			.perform(post("/login")
					.content(TestUtils.objectToJsonString(userLoginRequestDTO))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON));

		});
		assertTrue(thrown.getCause() instanceof LoginAttemptFailedException);
	}


}
