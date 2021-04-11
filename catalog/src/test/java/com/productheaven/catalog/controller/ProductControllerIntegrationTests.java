package com.productheaven.catalog.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.net.URL;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import com.productheaven.catalog.api.schema.request.DeleteProductRequestDTO;
import com.productheaven.catalog.api.schema.request.ProductRequestDTO;
import com.productheaven.catalog.api.schema.response.BaseResponseDTO;
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
	
	private static final int STATUS_ACTIVE = 1;
	private static final int STATUS_DELETED = 0;
	
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
		assertEquals(savedProduct.getPrice(), response.getProduct().getPrice());
	}
	
	@Test
	void givenProductShouldBeCreatedSuccessfully() throws Exception {
		// given
		ProductRequestDTO productRequestDTO = TestUtils.createProductRequestDTO();
		//when
		ResponseEntity<ProductResponseDTO> createdEntity = restTemplate.postForEntity(new URI("http://localhost:" + port + "/product"), productRequestDTO, ProductResponseDTO.class);
		ProductResponseDTO response = createdEntity.getBody();
		//then
		assertNotNull(response);
		assertEquals(HttpStatus.CREATED, createdEntity.getStatusCode());
		assertNull(response.getErrorMessage());
		assertEquals(productRequestDTO.getName(), response.getProduct().getName());
		assertEquals(productRequestDTO.getPrice(), response.getProduct().getPrice());
		assertEquals(productRequestDTO.getActionUser(), response.getProduct().getCreatedBy());
		assertEquals(productRequestDTO.getCategoryId(), response.getProduct().getCategoryId());
		assertNotNull(response.getProduct().getId());
		Optional<Product> dbRecord = repository.findById(response.getProduct().getId());
		assertTrue(dbRecord.isPresent());
		assertEquals(STATUS_ACTIVE, dbRecord.get().getStatus());
	}
	
	
	@Test
	void productShouldBeUpdatedSuccessfully() throws Exception {
		// given
		Product savedProduct = TestUtils.createProductEntity();
		repository.save(savedProduct);

		ProductRequestDTO productRequestDTO = TestUtils.createProductUpdateRequestDTO();
		RequestEntity<ProductRequestDTO> requestEntity = new RequestEntity<ProductRequestDTO>(productRequestDTO, HttpMethod.PUT, new URI("http://localhost:" + port + "/product/"+savedProduct.getId())); 
		//when
		ResponseEntity<ProductResponseDTO> updatedEntity = restTemplate.exchange(requestEntity, ProductResponseDTO.class);
		ProductResponseDTO response = updatedEntity.getBody();
		//then
		assertNotNull(response);
		assertEquals(HttpStatus.OK, updatedEntity.getStatusCode());
		assertNull(response.getErrorMessage());
		assertEquals(productRequestDTO.getName(), response.getProduct().getName());
		assertEquals(productRequestDTO.getPrice(), response.getProduct().getPrice());
		assertEquals(productRequestDTO.getActionUser(), response.getProduct().getLastUpdatedBy());
		assertEquals(productRequestDTO.getCategoryId(), response.getProduct().getCategoryId());
		assertNotNull(response.getProduct().getId());
		Optional<Product> dbRecord = repository.findById(response.getProduct().getId());
		assertTrue(dbRecord.isPresent());		
		Product updatedRecord = dbRecord.get();
		assertEquals(STATUS_ACTIVE, updatedRecord.getStatus());
		assertEquals(productRequestDTO.getName(), updatedRecord.getName());
		assertEquals(productRequestDTO.getPrice(), updatedRecord.getPrice());
		assertEquals(productRequestDTO.getActionUser(), updatedRecord.getLastUpdatedBy());
		assertEquals(productRequestDTO.getCategoryId(), updatedRecord.getCategoryId());
	}
	
	@Test
	void productShouldBeDeletedSuccessfully() throws Exception {
		// given
		Product savedProduct = TestUtils.createProductEntity();
		repository.save(savedProduct);

		final String actionUser = "Deleter!";
		DeleteProductRequestDTO productRequestDTO = new DeleteProductRequestDTO(actionUser);
		RequestEntity<DeleteProductRequestDTO> requestEntity = new RequestEntity<DeleteProductRequestDTO>(productRequestDTO, HttpMethod.DELETE, new URI("http://localhost:" + port + "/product/"+savedProduct.getId()));
		
		//when
		ResponseEntity<BaseResponseDTO> responseEntity = restTemplate.exchange(requestEntity, BaseResponseDTO.class);
		BaseResponseDTO response = responseEntity.getBody();
		//then
		assertNotNull(response);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNull(response.getErrorMessage());
		
		Optional<Product> dbRecord = repository.findById(savedProduct.getId());
		assertTrue(dbRecord.isPresent());		
		Product updatedRecord = dbRecord.get();
		assertEquals(STATUS_DELETED, updatedRecord.getStatus());
		assertEquals(actionUser, updatedRecord.getLastUpdatedBy());

	}
}
