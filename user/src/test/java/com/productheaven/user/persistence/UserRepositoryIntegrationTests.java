package com.productheaven.user.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLIntegrityConstraintViolationException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import com.productheaven.user.persistence.entity.User;
import com.productheaven.user.persistence.repository.UserRepository;
import com.productheaven.user.util.TestUtils;

@SpringBootTest
class UserRepositoryIntegrationTests {
	
	@Autowired
	UserRepository repo;
	
	@Test
	void givenUserShouldBeSaved() {
		User entity = TestUtils.createUserEntity();
		User savedUser = repo.save(entity);
		assertEquals(entity.getId(), savedUser.getId());
	}
	
	@Test
	void givenNullPasswordField_repositoryThrowsDataIntegrityViolationException() {
		User entity = TestUtils.createUserEntity();
		entity.setPasswordHashed(null);
		Exception thrown = assertThrows(DataIntegrityViolationException.class, () -> {
			repo.save(entity);
		});
		Throwable rootCause = thrown.getCause().getCause();
		assertTrue(rootCause instanceof SQLIntegrityConstraintViolationException);		
		String message =((SQLIntegrityConstraintViolationException)rootCause).getMessage();
		assertEquals("Column 'password_hashed' cannot be null", message);
	}
	
	@Test
	void givenNullNameField_repositoryThrowsDataIntegrityViolationException() {
		User entity = TestUtils.createUserEntity();
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
