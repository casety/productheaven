package com.productheaven.catalog.controller;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.productheaven.catalog.api.schema.request.CategoryRequestDTO;
import com.productheaven.catalog.api.schema.request.DeleteCategoryRequestDTO;
import com.productheaven.catalog.api.schema.response.BaseResponseDTO;
import com.productheaven.catalog.api.schema.response.CategoriesResponseDTO;
import com.productheaven.catalog.api.schema.response.CategoryDTO;
import com.productheaven.catalog.api.schema.response.CategoryResponseDTO;
import com.productheaven.catalog.persistence.entity.Category;
import com.productheaven.catalog.service.CategoryService;
import com.productheaven.catalog.service.RequestValidationService;
import com.productheaven.catalog.service.exception.CategoryNotFoundException;
import com.productheaven.catalog.service.exception.InvalidRequestException;

@RestController
public class CategoryController {

	
	private CategoryService categoryService;
	
	private ModelMapper modelMapper;
	
	private RequestValidationService validationService;

	
	public CategoryController(CategoryService categoryService,ModelMapper modelMapper,RequestValidationService validationService) {
		this.categoryService = categoryService;
		this.modelMapper = modelMapper;
		this.validationService = validationService;
	}
	
	@GetMapping("/category")
	public ResponseEntity<CategoriesResponseDTO>  getCategories() throws CategoryNotFoundException {
		CategoriesResponseDTO responseDTO = new CategoriesResponseDTO();
		List<Category> allCategories = categoryService.getAllCategories();
		responseDTO.setCategories(
				allCategories
				.stream()
				.map(this::convertToDto)
				.collect(Collectors.toList()));
		return new ResponseEntity<>(responseDTO,HttpStatus.OK);
	}
	
	@GetMapping("/category/{id}")
	public ResponseEntity<CategoryResponseDTO> getCategoryByCategoryId(@PathVariable("id") String id) throws CategoryNotFoundException, InvalidRequestException {
		validationService.validateCategoryId(id);
		Category category = categoryService.getCategoryById(id);
		CategoryDTO categoryDto = modelMapper.map(category, CategoryDTO.class);
		return new ResponseEntity<>(new CategoryResponseDTO(categoryDto),HttpStatus.OK);
	}
	
	
	@PostMapping("/category")
	public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CategoryRequestDTO categoryRequest) {
		Category entity = convertToEntityFromCreateRequest(categoryRequest);
		entity = categoryService.saveNewCategory(entity);
		CategoryDTO categoryDto = convertToDto(entity);
		CategoryResponseDTO responseDTO = new CategoryResponseDTO(categoryDto);
		return new ResponseEntity<>(responseDTO,HttpStatus.CREATED);
	}
	
	@PutMapping("/category/{id}")
	public ResponseEntity<CategoryResponseDTO> updateCategory(
			@Valid @RequestBody CategoryRequestDTO categoryRequest,
			@PathVariable("id") String id) throws InvalidRequestException, CategoryNotFoundException {
		validationService.validateCategoryId(id);
		Category entity = convertToEntityFromUpdateRequest(categoryRequest,id);
		entity = categoryService.updateCategory(entity);
		CategoryDTO categoryDto = convertToDto(entity);
		CategoryResponseDTO responseDTO = new CategoryResponseDTO(categoryDto);
		return new ResponseEntity<>(responseDTO,HttpStatus.OK);
	}
	
	@DeleteMapping("/category/{id}")
	public ResponseEntity<BaseResponseDTO> deleteCategory(@PathVariable("id") String id, @Valid @RequestBody DeleteCategoryRequestDTO categoryRequest) throws InvalidRequestException, CategoryNotFoundException {
		validationService.validateCategoryId(id);
		categoryService.deleteCategory(id,categoryRequest.getActionUser());
		return new ResponseEntity<>(new BaseResponseDTO(),HttpStatus.OK);
	}
	
	private CategoryDTO convertToDto(Category category) {
		return modelMapper.map(category, CategoryDTO.class);
	}
	
	private Category convertToEntityFromUpdateRequest(com.productheaven.catalog.api.schema.request.CategoryRequestDTO categoryRequestDTO,String id) {
		Category entity = modelMapper.map(categoryRequestDTO,Category.class);
		entity.setId(id);
		entity.setLastUpdatedBy(categoryRequestDTO.getActionUser());
		entity.setLastUpdateTime(new Date());
		return entity;
	}
	
	private Category convertToEntityFromCreateRequest(CategoryRequestDTO categoryRequestDTO) {
		Category entity = modelMapper.map(categoryRequestDTO,Category.class);
		entity.setCreatedBy(categoryRequestDTO.getActionUser());
		entity.setCreateTime(new Date());
		return entity;
	}
}
