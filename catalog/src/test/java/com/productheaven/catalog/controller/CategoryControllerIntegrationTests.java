package com.productheaven.catalog.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.net.URL;
import java.util.Optional;
import java.util.UUID;

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

import com.productheaven.catalog.api.schema.request.CategoryRequestDTO;
import com.productheaven.catalog.api.schema.request.DeleteCategoryRequestDTO;
import com.productheaven.catalog.api.schema.response.BaseResponseDTO;
import com.productheaven.catalog.api.schema.response.CategoryResponseDTO;
import com.productheaven.catalog.persistence.entity.Category;
import com.productheaven.catalog.persistence.repository.CategoryRepository;
import com.productheaven.catalog.util.TestUtils;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CategoryControllerIntegrationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;
	
	@Autowired
	private CategoryRepository repository;
	
	
	@BeforeEach
	void initDb () {
		repository.deleteAll();
	}
	
	private static final int STATUS_ACTIVE = 1;
	private static final int STATUS_DELETED = 0;
	
	@Test
	void categoriesShouldBeFetchedByIdSuccessfully() throws Exception {
		
		//given
		Category savedCategory = TestUtils.createCategoryEntity();
		repository.save(savedCategory);
		
		//when
		ResponseEntity<CategoryResponseDTO> createdEntity = restTemplate
				.getForEntity(new URL("http://localhost:" + port + "/category/"+savedCategory.getId()).toString(), CategoryResponseDTO.class);
		CategoryResponseDTO response = createdEntity.getBody();
		
		//then
		assertEquals(HttpStatus.OK, createdEntity.getStatusCode());
		assertNull(response.getErrorMessage());
		assertEquals(savedCategory.getName(), response.getCategory().getName());
		assertEquals(savedCategory.getId(), response.getCategory().getId());
		assertEquals(savedCategory.getDescription(), response.getCategory().getDescription());
	}
	
	@Test
	void givenCategoryShouldBeCreatedSuccessfully() throws Exception {
		// given
		CategoryRequestDTO categoryRequestDTO = TestUtils.createCategoryRequestDTO();
		//when
		ResponseEntity<CategoryResponseDTO> createdEntity = restTemplate.postForEntity(new URI("http://localhost:" + port + "/category"), categoryRequestDTO, CategoryResponseDTO.class);
		CategoryResponseDTO response = createdEntity.getBody();
		//then
		assertNotNull(response);
		assertEquals(HttpStatus.CREATED, createdEntity.getStatusCode());
		assertNull(response.getErrorMessage());
		assertEquals(categoryRequestDTO.getName(), response.getCategory().getName());
		assertEquals(categoryRequestDTO.getDescription(), response.getCategory().getDescription());
		assertNotNull(response.getCategory().getId());
		Optional<Category> dbRecord = repository.findById(response.getCategory().getId());
		assertTrue(dbRecord.isPresent());
		assertEquals(STATUS_ACTIVE, dbRecord.get().getStatus());
	}
	
	
	@Test
	void categoryShouldBeUpdatedSuccessfully() throws Exception {
		// given
		Category savedCategory = TestUtils.createCategoryEntity();
		repository.save(savedCategory);

		CategoryRequestDTO categoryRequestDTO = TestUtils.createCategoryUpdateRequestDTO();
		RequestEntity<CategoryRequestDTO> requestEntity = new RequestEntity<CategoryRequestDTO>(categoryRequestDTO, HttpMethod.PUT, new URI("http://localhost:" + port + "/category/"+savedCategory.getId())); 
		//when
		ResponseEntity<CategoryResponseDTO> updatedEntity = restTemplate.exchange(requestEntity, CategoryResponseDTO.class);
		CategoryResponseDTO response = updatedEntity.getBody();
		//then
		assertNotNull(response);
		assertEquals(HttpStatus.OK, updatedEntity.getStatusCode());
		assertNull(response.getErrorMessage());
		assertEquals(categoryRequestDTO.getName(), response.getCategory().getName());
		assertNotNull(response.getCategory().getId());
		Optional<Category> dbRecord = repository.findById(response.getCategory().getId());
		assertTrue(dbRecord.isPresent());		
		Category updatedRecord = dbRecord.get();
		assertEquals(STATUS_ACTIVE, updatedRecord.getStatus());
		assertEquals(categoryRequestDTO.getName(), updatedRecord.getName());
		assertEquals(categoryRequestDTO.getActionUser(), updatedRecord.getLastUpdatedBy());
		assertEquals(categoryRequestDTO.getDescription(), updatedRecord.getDescription());
	}
	
	@Test
	void categoryShouldBeDeletedSuccessfully() throws Exception {
		// given
		Category savedCategory = TestUtils.createCategoryEntity();
		repository.save(savedCategory);

		final String actionUser = "Deleter!";
		DeleteCategoryRequestDTO categoryRequestDTO = new DeleteCategoryRequestDTO(actionUser);
		RequestEntity<DeleteCategoryRequestDTO> requestEntity = new RequestEntity<DeleteCategoryRequestDTO>(categoryRequestDTO, HttpMethod.DELETE, new URI("http://localhost:" + port + "/category/"+savedCategory.getId()));
		
		//when
		ResponseEntity<BaseResponseDTO> responseEntity = restTemplate.exchange(requestEntity, BaseResponseDTO.class);
		BaseResponseDTO response = responseEntity.getBody();
		//then
		assertNotNull(response);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNull(response.getErrorMessage());
		
		Optional<Category> dbRecord = repository.findById(savedCategory.getId());
		assertTrue(dbRecord.isPresent());		
		Category updatedRecord = dbRecord.get();
		assertEquals(STATUS_DELETED, updatedRecord.getStatus());
		assertEquals(actionUser, updatedRecord.getLastUpdatedBy());

	}
	
	@Test
	void whenThereIsNoCategoryToBeDeleted_HTTP404ShouldBeReturned() throws Exception {
		// given
		String productId = UUID.randomUUID().toString();
		final String actionUser = "Deleter!";
		DeleteCategoryRequestDTO categoryRequestDTO = new DeleteCategoryRequestDTO(actionUser);
		RequestEntity<DeleteCategoryRequestDTO> requestEntity = new RequestEntity<DeleteCategoryRequestDTO>(categoryRequestDTO, HttpMethod.DELETE, new URI("http://localhost:" + port + "/category/"+productId));
		
		//when
		ResponseEntity<BaseResponseDTO> responseEntity = restTemplate.exchange(requestEntity, BaseResponseDTO.class);
		BaseResponseDTO response = responseEntity.getBody();
		
		//then
		assertNotNull(response);
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}
}
