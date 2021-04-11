package com.productheaven.user.persistence;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.productheaven.user.persistence.entity.User;
import com.productheaven.user.persistence.repository.UserRepository;

@SpringBootTest
class UserRepositoryIntegrationTests {
	
	@Autowired
	UserRepository repo;
	
	@Test
	void tobewritten() {
		User entity = new User();
		
		repo.save(entity);
	}
}
