package com.productheaven.catalog.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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

import com.productheaven.catalog.api.schema.response.ProductResponseDTO;
import com.productheaven.catalog.persistence.entity.Product;
import com.productheaven.catalog.persistence.repository.ProductRepository;
import com.productheaven.catalog.util.TestUtils;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ProductControllerIntegrationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;
	
	@Autowired
	private ProductRepository repository;
	
	
	@BeforeEach
	void initDb () {
		repository.deleteAll();
	}
	
	@Test
	void productsShouldBeFetchedByIdSuccessfully() throws Exception {

		//given
		Product savedProduct = TestUtils.createProductEntity();
		repository.save(savedProduct);
		
		//when
		ResponseEntity<ProductResponseDTO> createdEntity = restTemplate
				.getForEntity(new URL("http://localhost:" + port + "/product/"+savedProduct.getId()).toString(), ProductResponseDTO.class);
		ProductResponseDTO response = createdEntity.getBody();
		
		//then
		assertEquals(HttpStatus.OK, createdEntity.getStatusCode());
		assertNull(response.getErrorMessage());
		assertEquals(savedProduct.getName(), response.getProduct().getName());
		assertEquals(savedProduct.getId(), response.getProduct().getId());
		assertEquals(savedProduct.getDescription(), response.getProduct().getDescription());
	}
}
