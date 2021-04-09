package com.productheaven.user.persistence.repository;

import org.springframework.data.repository.CrudRepository;

import com.productheaven.user.persistence.entity.UserAccess;

public interface UserAccessRepository extends CrudRepository<UserAccess, String>  {

}
