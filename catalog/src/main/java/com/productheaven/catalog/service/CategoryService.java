package com.productheaven.catalog.service;

import java.util.List;

import com.productheaven.catalog.persistence.entity.Category;
import com.productheaven.catalog.service.exception.CategoryNotFoundException;
import com.productheaven.catalog.service.exception.InvalidRequestException;

public interface CategoryService {

	List<Category> getAllCategories() throws CategoryNotFoundException;

	Category getCategoryById(String id) throws CategoryNotFoundException;
	
	Category saveNewCategory(Category entity);

	Category updateCategory(Category entity) throws CategoryNotFoundException, InvalidRequestException;

	void deleteCategory(String id, String actionUser) throws CategoryNotFoundException;
}
