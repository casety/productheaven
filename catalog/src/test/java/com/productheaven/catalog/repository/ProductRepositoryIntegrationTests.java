package com.productheaven.catalog.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLIntegrityConstraintViolationException;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import com.productheaven.catalog.persistence.entity.Product;
import com.productheaven.catalog.persistence.repository.ProductRepository;
import com.productheaven.catalog.util.TestUtils;

@SpringBootTest
class ProductRepositoryIntegrationTests {
	
	@Autowired
	ProductRepository repo;
	
	@Test
	void givenUserShouldBeSaved() {
		Product entity = TestUtils.createProductEntity();
		Product savedProduct = repo.save(entity);
		assertEquals(entity.getId(), savedProduct.getId());
	}
	
	@Test
	void givenNullPriceField_repositoryThrowsDataIntegrityViolationException() {
		Product entity = TestUtils.createProductEntity();
		entity.setPrice(null);
		Exception thrown = assertThrows(DataIntegrityViolationException.class, () -> {
			repo.save(entity);
		});
		Throwable rootCause = thrown.getCause().getCause();
		assertTrue(rootCause instanceof SQLIntegrityConstraintViolationException);		
		String message =((SQLIntegrityConstraintViolationException)rootCause).getMessage();
		assertEquals("Column 'price' cannot be null", message);
	}
	
	@Test
	void givenNullNameField_repositoryThrowsDataIntegrityViolationException() {
		Product entity = TestUtils.createProductEntity();
		entity.setName(null);
		Exception thrown = assertThrows(DataIntegrityViolationException.class, () -> {
			repo.save(entity);
		});
		Throwable rootCause = thrown.getCause().getCause();
		assertTrue(rootCause instanceof SQLIntegrityConstraintViolationException);		
		String message =((SQLIntegrityConstraintViolationException)rootCause).getMessage();
		assertEquals("Column 'name' cannot be null", message);
	}
}
