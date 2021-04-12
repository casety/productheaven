package com.productheaven.catalog.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.productheaven.catalog.persistence.entity.Category;

public interface CategoryRepository extends CrudRepository<Category, String> {

	Optional<Category> findByIdAndStatus(String id, int status);
	
	List<Category> findByNameAndStatus(String name, int status);

}
