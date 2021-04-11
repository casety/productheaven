package com.productheaven.user.persistence.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.productheaven.user.persistence.entity.User;

public interface UserRepository extends CrudRepository<User,String> {

	List<User> findByUsernameOrEmail(String username,String email);
	
	List<User> findByUsername(String username);
	
}
