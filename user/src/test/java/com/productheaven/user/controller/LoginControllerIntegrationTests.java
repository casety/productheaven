package com.productheaven.user.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestClientException;

import com.productheaven.user.api.schema.request.UserCreateRequestDTO;
import com.productheaven.user.api.schema.request.UserLoginRequestDTO;
import com.productheaven.user.api.schema.response.BaseResponseDTO;
import com.productheaven.user.api.schema.response.UserResponseDTO;
import com.productheaven.user.persistence.entity.User;
import com.productheaven.user.persistence.repository.UserRepository;
import com.productheaven.user.util.TestUtils;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class LoginControllerIntegrationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;
	
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private PasswordEncoder encoder;
	
	
	@BeforeEach
	void initDb () {
		repository.deleteAll();
	}
	
	@Test
	void registeredUsersShouldLoginSuccessfully() throws Exception {

		//given
		User savedUser = TestUtils.createUserEntity();
		String encodedPass = encoder.encode(savedUser.getPassword());
		savedUser.setPasswordHashed(encodedPass);
		repository.save(savedUser);
		
		UserLoginRequestDTO userLoginRequest = new UserLoginRequestDTO(savedUser.getUsername(), savedUser.getPassword()); 
		
		//when	
		ResponseEntity<BaseResponseDTO> createdEntity = restTemplate.postForEntity(new URI("http://localhost:" + port + "/login"), userLoginRequest, BaseResponseDTO.class);
		BaseResponseDTO response = createdEntity.getBody();
		
		//then
		assertEquals(HttpStatus.OK, createdEntity.getStatusCode());
		assertNull(response.getErrorMessage());
	}
	
	@Test
	void registeredUsersShouldFailIfPasswordIsNotValid() throws Exception {

		//given
		User savedUser = TestUtils.createUserEntity();
		String encodedPass = encoder.encode(savedUser.getPassword());
		savedUser.setPasswordHashed(encodedPass);
		repository.save(savedUser);
		
		UserLoginRequestDTO userLoginRequest = new UserLoginRequestDTO(savedUser.getUsername(), savedUser.getPassword()+"."); 
		
		//when	
		ResponseEntity<BaseResponseDTO> createdEntity = restTemplate.postForEntity(new URI("http://localhost:" + port + "/login"), userLoginRequest, BaseResponseDTO.class);
		
		//then
		assertEquals(HttpStatus.UNAUTHORIZED, createdEntity.getStatusCode());

	}
	
	@Test
	void registeredUsersShouldFailIfUsernameIsNotValid() throws Exception {

		//given
		User savedUser = TestUtils.createUserEntity();
		String encodedPass = encoder.encode(savedUser.getPassword());
		savedUser.setPasswordHashed(encodedPass);
		repository.save(savedUser);
		
		UserLoginRequestDTO userLoginRequest = new UserLoginRequestDTO(savedUser.getUsername()+".", savedUser.getPassword()); 
		
		//when	
		ResponseEntity<BaseResponseDTO> createdEntity = restTemplate.postForEntity(new URI("http://localhost:" + port + "/login"), userLoginRequest, BaseResponseDTO.class);
		
		//then
		assertEquals(HttpStatus.UNAUTHORIZED, createdEntity.getStatusCode());

	}
	
	@Test
	void nonRegisteredUsersShouldFail() throws Exception {
		
		//given
		UserLoginRequestDTO userLoginRequest = new UserLoginRequestDTO("non", "registered"); 
		
		//when	
		ResponseEntity<BaseResponseDTO> createdEntity = restTemplate.postForEntity(new URI("http://localhost:" + port + "/login"), userLoginRequest, BaseResponseDTO.class);
		
		//then
		assertEquals(HttpStatus.UNAUTHORIZED, createdEntity.getStatusCode());

	}
	
	@Test
	void usersShouldRegisterAndLoginSuccessfully () throws RestClientException, URISyntaxException {
		
		// given
		UserCreateRequestDTO userRequestDTO = new UserCreateRequestDTO("sampleUsername","test@sampleemail.com","samplename","samplesurname","samplepass");
		
		//when
		restTemplate.postForEntity(new URI("http://localhost:" + port + "/user"), userRequestDTO, UserResponseDTO.class);
		
		//given
		UserLoginRequestDTO userLoginRequest = new UserLoginRequestDTO(userRequestDTO.getUsername(), userRequestDTO.getPassword()); 
		
		//when	
		ResponseEntity<BaseResponseDTO> createdEntity = restTemplate.postForEntity(new URI("http://localhost:" + port + "/login"), userLoginRequest, BaseResponseDTO.class);
		BaseResponseDTO response = createdEntity.getBody();
		
		//then
		assertEquals(HttpStatus.OK, createdEntity.getStatusCode());
		assertNull(response.getErrorMessage());

		
	}
	
}
