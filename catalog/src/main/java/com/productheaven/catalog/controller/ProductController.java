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

import com.productheaven.catalog.api.schema.request.DeleteProductRequestDTO;
import com.productheaven.catalog.api.schema.request.ProductRequestDTO;
import com.productheaven.catalog.api.schema.response.BaseResponseDTO;
import com.productheaven.catalog.api.schema.response.ProductDTO;
import com.productheaven.catalog.api.schema.response.ProductResponseDTO;
import com.productheaven.catalog.api.schema.response.ProductsResponseDTO;
import com.productheaven.catalog.persistence.entity.Product;
import com.productheaven.catalog.service.ProductService;
import com.productheaven.catalog.service.RequestValidationService;
import com.productheaven.catalog.service.exception.CategoryNotFoundException;
import com.productheaven.catalog.service.exception.InvalidRequestException;
import com.productheaven.catalog.service.exception.ProductNotFoundException;

@RestController
public class ProductController {

	
	private ProductService productService;
	
	private ModelMapper modelMapper;
	
	private RequestValidationService validationService;

	
	public ProductController(ProductService productService,ModelMapper modelMapper,RequestValidationService validationService) {
		this.productService = productService;
		this.modelMapper = modelMapper;
		this.validationService = validationService;
	}
	
	@GetMapping("/product")
	public ResponseEntity<ProductsResponseDTO>  getProducts() throws ProductNotFoundException {
		ProductsResponseDTO responseDTO = new ProductsResponseDTO();
		List<Product> allProducts = productService.getAllProducts();
		responseDTO.setProducts(
				allProducts
				.stream()
				.map(this::convertToDto)
				.collect(Collectors.toList()));
		return new ResponseEntity<>(responseDTO,HttpStatus.OK);
	}
	
	@GetMapping("/product/{id}")
	public ResponseEntity<ProductResponseDTO> getProductByProductId(@PathVariable("id") String id) throws ProductNotFoundException, InvalidRequestException {
		validationService.validateProductId(id);
		Product product = productService.getProductById(id);
		ProductDTO productDto = modelMapper.map(product, ProductDTO.class);
		return new ResponseEntity<>(new ProductResponseDTO(productDto),HttpStatus.OK);
	}
	
	@GetMapping("/products-by-category/{category-id}")
	public ResponseEntity<ProductsResponseDTO> getProductsByCategoryId(@PathVariable("category-id") String categoryId) throws ProductNotFoundException, InvalidRequestException {
		validationService.validateCategoryId(categoryId);
		ProductsResponseDTO responseDTO = new ProductsResponseDTO();
		List<Product> products = productService.getProductsByCategoryId(categoryId);
		responseDTO.setProducts(
				products
				.stream()
				.map(this::convertToDto)
				.collect(Collectors.toList()));
		return new ResponseEntity<>(responseDTO,HttpStatus.OK);	
	}
	
	@PostMapping("/product")
	public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO productRequest) throws CategoryNotFoundException {
		Product entity = convertToEntityFromCreateRequest(productRequest);
		entity = productService.saveNewProduct(entity);
		ProductDTO productDto = convertToDto(entity);
		ProductResponseDTO responseDTO = new ProductResponseDTO(productDto);
		return new ResponseEntity<>(responseDTO,HttpStatus.CREATED);
	}
	
	@PutMapping("/product/{id}")
	public ResponseEntity<ProductResponseDTO> updateProduct(@Valid @RequestBody ProductRequestDTO productRequest,
			@PathVariable("id") String id) throws InvalidRequestException, ProductNotFoundException, CategoryNotFoundException {
		validationService.validateProductId(id);
		validationService.validateCategoryId(productRequest.getCategoryId());
		Product entity = convertToEntityFromUpdateRequest(productRequest,id);
		entity = productService.updateProduct(entity);
		ProductDTO productDto = convertToDto(entity);
		ProductResponseDTO responseDTO = new ProductResponseDTO(productDto);
		return new ResponseEntity<>(responseDTO,HttpStatus.OK);
	}
	
	@DeleteMapping("/product/{id}")
	public ResponseEntity<BaseResponseDTO> deleteProduct(@PathVariable("id") String id, @Valid @RequestBody DeleteProductRequestDTO productRequest) throws InvalidRequestException, ProductNotFoundException {
		validationService.validateProductId(id);
		productService.deleteProduct(id,productRequest.getActionUser());
		return new ResponseEntity<>(new BaseResponseDTO(),HttpStatus.OK);
	}
	
	private ProductDTO convertToDto(Product product) {
		return modelMapper.map(product, ProductDTO.class);
	}
	
	private Product convertToEntityFromUpdateRequest(ProductRequestDTO productRequestDTO,String id) {
		Product entity = modelMapper.map(productRequestDTO,Product.class);
		entity.setId(id);
		entity.setLastUpdatedBy(productRequestDTO.getActionUser());
		entity.setLastUpdateTime(new Date());
		return entity;
	}
	
	private Product convertToEntityFromCreateRequest(ProductRequestDTO productRequestDTO) {
		Product entity = modelMapper.map(productRequestDTO,Product.class);
		entity.setCreatedBy(productRequestDTO.getActionUser());
		entity.setCreateTime(new Date());
		return entity;
	}
}
