package com.productheaven.catalog.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLIntegrityConstraintViolationException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import com.productheaven.catalog.persistence.entity.Category;
import com.productheaven.catalog.persistence.repository.CategoryRepository;
import com.productheaven.catalog.util.TestUtils;

@SpringBootTest
class CategoryRepositoryIntegrationTests {
	
	@Autowired
	CategoryRepository repo;
	
	@Test
	void givenCategoryShouldBeSaved() {
		Category entity = TestUtils.createCategoryEntity();
		Category savedCategory = repo.save(entity);
		assertEquals(entity.getId(), savedCategory.getId());
	}
	
	@Test
	void givenNullCreatedByField_repositoryThrowsDataIntegrityViolationException() {
		Category entity = TestUtils.createCategoryEntity();
		entity.setCreatedBy(null);
		Exception thrown = assertThrows(DataIntegrityViolationException.class, () -> {
			repo.save(entity);
		});
		Throwable rootCause = thrown.getCause().getCause();
		assertTrue(rootCause instanceof SQLIntegrityConstraintViolationException);		
		String message =((SQLIntegrityConstraintViolationException)rootCause).getMessage();
		assertEquals("Column 'created_by' cannot be null", message);
	}
	
	@Test
	void givenNullNameField_repositoryThrowsDataIntegrityViolationException() {
		Category entity = TestUtils.createCategoryEntity();
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
