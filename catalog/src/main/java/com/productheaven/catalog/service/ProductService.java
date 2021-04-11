package com.productheaven.catalog.service;

import java.util.List;

import com.productheaven.catalog.persistence.entity.Product;
import com.productheaven.catalog.service.exception.NoProductsFoundException;
import com.productheaven.catalog.service.exception.ProductNotFoundException;

public interface ProductService {

	List<Product> getAllProducts() throws NoProductsFoundException;

	Product getProductById(String id) throws ProductNotFoundException;
}
