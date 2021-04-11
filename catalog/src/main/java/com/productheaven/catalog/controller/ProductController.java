package com.productheaven.catalog.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.productheaven.catalog.api.schema.request.ProductCreateRequestDTO;
import com.productheaven.catalog.api.schema.response.ProductDTO;
import com.productheaven.catalog.api.schema.response.ProductResponseDTO;
import com.productheaven.catalog.api.schema.response.ProductsResponseDTO;
import com.productheaven.catalog.persistence.entity.Product;
import com.productheaven.catalog.service.ProductService;
import com.productheaven.catalog.service.RequestValidationService;
import com.productheaven.catalog.service.exception.InvalidRequestException;
import com.productheaven.catalog.service.exception.NoProductsFoundException;
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
	public ResponseEntity<ProductsResponseDTO>  getProducts() throws NoProductsFoundException {
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
	
	private ProductDTO convertToDto(Product product) {
		return modelMapper.map(product, ProductDTO.class);
	}
	
	private Product convertToEntity(ProductCreateRequestDTO productCreateRequestDTO) {
		return modelMapper.map(productCreateRequestDTO,Product.class);
	}
}
