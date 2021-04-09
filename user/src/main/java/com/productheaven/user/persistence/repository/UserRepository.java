package com.productheaven.user.persistence.repository;

import org.springframework.data.repository.CrudRepository;

import com.productheaven.user.persistence.entity.User;

public interface UserRepository extends CrudRepository<User,String> {

}
