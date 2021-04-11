package com.productheaven.catalog.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.productheaven.catalog.persistence.entity.Product;

public interface ProductRepository extends CrudRepository<Product, String> {

	Optional<Product> findByIdAndStatus(String id, int status);
	
	List<Product> findByCategoryIdAndStatus (String categoryId, int status);
	
}
