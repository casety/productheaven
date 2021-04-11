package com.productheaven.catalog.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.productheaven.catalog.persistence.entity.Product;
import com.productheaven.catalog.persistence.repository.ProductRepository;
import com.productheaven.catalog.service.ProductService;
import com.productheaven.catalog.service.exception.NoProductsFoundException;
import com.productheaven.catalog.service.exception.ProductNotFoundException;

@Service
public class ProductServiceImpl implements ProductService {

	private static final int STATUS_ACTIVE = 1;
	private static final int STATUS_DELETED = 0; 
	
	private ProductRepository repository;

	public ProductServiceImpl(ProductRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public List<Product> getAllProducts() throws NoProductsFoundException {
		
		Iterable<Product> users = repository.findAll();
		Iterator<Product> iterator = users.iterator();
		if (!iterator.hasNext()) {
			throw new NoProductsFoundException();
		}
		List<Product> returnList = new ArrayList<>();
		iterator.forEachRemaining(returnList::add);
		return returnList;
	}

	@Override
	public Product getProductById(String id) throws ProductNotFoundException {
		Optional<Product> product = repository.findByIdAndStatus(id,STATUS_ACTIVE);
		if (!product.isPresent()) {
			throw new ProductNotFoundException();
		}
		return product.get();
	}
}
