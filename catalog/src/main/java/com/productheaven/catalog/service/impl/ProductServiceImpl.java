package com.productheaven.catalog.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.productheaven.catalog.persistence.entity.Product;
import com.productheaven.catalog.persistence.repository.ProductRepository;
import com.productheaven.catalog.service.CategoryService;
import com.productheaven.catalog.service.ProductService;
import com.productheaven.catalog.service.exception.CategoryNotFoundException;
import com.productheaven.catalog.service.exception.ProductNotFoundException;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	private static final int STATUS_ACTIVE = 1;
	private static final int STATUS_DELETED = 0; 
	
	private ProductRepository repository;
	
	private CategoryService categoryService;

	public ProductServiceImpl(ProductRepository repository,CategoryService categoryService) {
		this.repository = repository;
		this.categoryService = categoryService;
	}
	
	@Override
	public List<Product> getAllProducts() throws ProductNotFoundException {
		Iterable<Product> users = repository.findAll();
		Iterator<Product> iterator = users.iterator();
		if (!iterator.hasNext()) {
			throw new ProductNotFoundException();
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

	@Override
	public List<Product> getProductsByCategoryId(String categoryId) throws ProductNotFoundException {
		List<Product> products = repository.findByCategoryIdAndStatus(categoryId, STATUS_ACTIVE);
		if (products==null || products.isEmpty()) {
			throw new ProductNotFoundException();
		}
		return products;
	}

	@Override
	public Product saveNewProduct(Product entity) throws CategoryNotFoundException{
		categoryService.getCategoryById(entity.getCategoryId());
		entity.setId(UUID.randomUUID().toString());
		entity.setCreateTime(new Date());
		entity.setStatus(STATUS_ACTIVE);
		return repository.save(entity);
	}

	@Override
	public Product updateProduct(Product entity) throws ProductNotFoundException, CategoryNotFoundException {
		categoryService.getCategoryById(entity.getCategoryId());
		//preserve orj values
		Product orjProduct = getProductById(entity.getId());
		entity.setCreatedBy(orjProduct.getCreatedBy());
		entity.setStatus(orjProduct.getStatus());
		entity.setCreateTime(orjProduct.getCreateTime());
		return repository.save(entity);
	}

	@Override
	public void deleteProduct(String id,String actionUser) throws ProductNotFoundException {
		Product orjProduct = getProductById(id);
		orjProduct.setStatus(STATUS_DELETED);
		orjProduct.setLastUpdateTime(new Date());
		orjProduct.setLastUpdatedBy(actionUser);
		repository.save(orjProduct);
	}
}
