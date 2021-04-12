package com.productheaven.catalog.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.productheaven.catalog.persistence.entity.Category;
import com.productheaven.catalog.persistence.repository.CategoryRepository;
import com.productheaven.catalog.service.CategoryService;
import com.productheaven.catalog.service.exception.CategoryAlreadyExistsException;
import com.productheaven.catalog.service.exception.CategoryNotFoundException;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

	private static final int STATUS_ACTIVE = 1;
	private static final int STATUS_DELETED = 0; 
	
	private CategoryRepository repository;

	public CategoryServiceImpl(CategoryRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public List<Category> getAllCategories() throws CategoryNotFoundException {
		Iterable<Category> users = repository.findAll();
		Iterator<Category> iterator = users.iterator();
		if (!iterator.hasNext()) {
			throw new CategoryNotFoundException();
		}
		List<Category> returnList = new ArrayList<>();
		iterator.forEachRemaining(returnList::add);
		return returnList;
	}

	@Override
	public Category getCategoryById(String id) throws CategoryNotFoundException {
		Optional<Category> product = repository.findByIdAndStatus(id,STATUS_ACTIVE);
		if (!product.isPresent()) {
			throw new CategoryNotFoundException();
		}
		return product.get();
	}


	@Override
	public Category saveNewCategory(Category entity) throws CategoryAlreadyExistsException {
		List<Category> result = repository.findByNameAndStatus(entity.getName(), STATUS_ACTIVE);
		if (result!=null && !result.isEmpty()) {
			throw new CategoryAlreadyExistsException();
		}
		entity.setId(UUID.randomUUID().toString());
		entity.setCreateTime(new Date());
		entity.setStatus(STATUS_ACTIVE);
		return repository.save(entity);
	}

	@Override
	public Category updateCategory(Category entity) throws CategoryNotFoundException {
		//preserve orj values
		Category orjCategory = getCategoryById(entity.getId());
		entity.setCreatedBy(orjCategory.getCreatedBy());
		entity.setStatus(orjCategory.getStatus());
		entity.setCreateTime(orjCategory.getCreateTime());
		return repository.save(entity);
	}

	@Override
	public void deleteCategory(String id,String actionUser) throws CategoryNotFoundException {
		Category orjCategory = getCategoryById(id);
		orjCategory.setStatus(STATUS_DELETED);
		orjCategory.setLastUpdateTime(new Date());
		orjCategory.setLastUpdatedBy(actionUser);
		repository.save(orjCategory);
	}
}
