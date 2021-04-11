package com.productheaven.catalog.persistence.repository;

import org.springframework.data.repository.CrudRepository;

import com.productheaven.catalog.persistence.entity.Category;

public interface CategoryRepository extends CrudRepository<Category, String> {

}
