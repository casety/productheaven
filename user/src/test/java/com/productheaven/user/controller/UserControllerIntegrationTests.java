package com.productheaven.user.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.net.URI;
import java.net.URL;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.productheaven.user.api.schema.request.UserCreateRequestDTO;
import com.productheaven.user.api.schema.response.UserResponseDTO;
import com.productheaven.user.persistence.entity.User;
import com.productheaven.user.persistence.repository.UserRepository;
import com.productheaven.user.util.TestUtils;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class UserControllerIntegrationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;
	
	@Autowired
	private UserRepository repository;
	
	
	@BeforeEach
	void initDb () {
		repository.deleteAll();
	}
	
	@Test
	void registeredUsersShouldBeFetchedByIdSuccessfully() throws Exception {

		//given
		User savedUser = TestUtils.createUserEntity();
		repository.save(savedUser);
		
		//when
		ResponseEntity<UserResponseDTO> createdEntity = restTemplate
				.getForEntity(new URL("http://localhost:" + port + "/user/"+savedUser.getId()).toString(), UserResponseDTO.class);
		UserResponseDTO response = createdEntity.getBody();
		
		//then
		assertEquals(HttpStatus.OK, createdEntity.getStatusCode());
		assertNull(response.getErrorMessage());
		assertEquals(savedUser.getUsername(), response.getUser().getUsername());
		assertEquals(savedUser.getEmail(), response.getUser().getEmail());
		assertEquals(savedUser.getSurname(), response.getUser().getSurname());
		assertEquals(savedUser.getName(), response.getUser().getName());
	}
	
	@Test
	void registeredUsersShouldBeFetchedByUsernameSuccessfully() throws Exception {
		User savedUser = TestUtils.createUserEntity();
		repository.save(savedUser);
		ResponseEntity<UserResponseDTO> createdEntity = restTemplate
				.getForEntity(new URL("http://localhost:" + port + "/user-by-username/"+savedUser.getUsername()).toString(), UserResponseDTO.class);
		assertEquals(HttpStatus.OK, createdEntity.getStatusCode());
		UserResponseDTO response = createdEntity.getBody();
		assertNull(response.getErrorMessage());
		assertEquals(savedUser.getUsername(), response.getUser().getUsername());
		assertEquals(savedUser.getEmail(), response.getUser().getEmail());
		assertEquals(savedUser.getSurname(), response.getUser().getSurname());
		assertEquals(savedUser.getName(), response.getUser().getName());
	}
	
	@Test
	void givenUserShouldBeCreatedSuccessfully() throws Exception {
		// given
		UserCreateRequestDTO userRequestDTO = new UserCreateRequestDTO("sampleUsername","test@sampleemail.com","samplename","samplesurname","samplepass");
		//when
		ResponseEntity<UserResponseDTO> createdEntity = restTemplate.postForEntity(new URI("http://localhost:" + port + "/user"), userRequestDTO, UserResponseDTO.class);
		UserResponseDTO response = createdEntity.getBody();
		//then
		assertNotNull(response);
		assertEquals(HttpStatus.CREATED, createdEntity.getStatusCode());
		assertNull(response.getErrorMessage());
		assertEquals(userRequestDTO.getUsername(), response.getUser().getUsername());
		assertEquals(userRequestDTO.getEmail(), response.getUser().getEmail());
		assertEquals(userRequestDTO.getSurname(), response.getUser().getSurname());
		assertNotNull(response.getUser().getId());
		assertNotNull(repository.findById(response.getUser().getId()).get());
	}
}
