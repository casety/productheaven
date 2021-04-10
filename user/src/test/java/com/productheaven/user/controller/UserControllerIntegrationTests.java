package com.productheaven.user.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URL;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.productheaven.user.api.schema.response.UsersResponseDTO;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class UserControllerIntegrationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;
	
	@Test
	void registeredUsersShouldBeListedSuccessfully() throws Exception {
		ResponseEntity<UsersResponseDTO> response = restTemplate
				.getForEntity(new URL("http://localhost:" + port + "/user").toString(), UsersResponseDTO.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
}
