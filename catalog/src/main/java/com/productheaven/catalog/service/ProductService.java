package com.productheaven.catalog.service;

import java.util.List;

import com.productheaven.catalog.persistence.entity.Product;
import com.productheaven.catalog.service.exception.CategoryNotFoundException;
import com.productheaven.catalog.service.exception.InvalidRequestException;
import com.productheaven.catalog.service.exception.ProductNotFoundException;

public interface ProductService {

	List<Product> getAllProducts() throws ProductNotFoundException;

	Product getProductById(String id) throws ProductNotFoundException;
	
	List<Product> getProductsByCategoryId(String categoryId) throws ProductNotFoundException;

	Product saveNewProduct(Product entity) throws CategoryNotFoundException;

	Product updateProduct(Product entity) throws ProductNotFoundException, InvalidRequestException, CategoryNotFoundException;

	void deleteProduct(String id, String actionUser) throws ProductNotFoundException;
}
