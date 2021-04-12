package com.productheaven.catalog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.productheaven.catalog.persistence.entity.Product;
import com.productheaven.catalog.persistence.repository.ProductRepository;
import com.productheaven.catalog.service.exception.CategoryNotFoundException;
import com.productheaven.catalog.service.exception.ProductNotFoundException;
import com.productheaven.catalog.service.impl.ProductServiceImpl;
import com.productheaven.catalog.util.TestUtils;

@ExtendWith(SpringExtension.class)
class ProductServiceImplTests {

	@InjectMocks
	private ProductServiceImpl productService;
	
	@Mock
	private ProductRepository productRepo;
	
	@Mock
	private CategoryService categoryService;
	
	private static final int STATUS_ACTIVE = 1;
	

	@Test
	void productsShouldBeFetchedByIdSuccessfully() throws ProductNotFoundException {
		Product givenProduct = TestUtils.createProductEntity();
		when(productRepo.findByIdAndStatus(givenProduct.getId(),STATUS_ACTIVE)).thenReturn(Optional.of(givenProduct));
		Product serviceResult = productService.getProductById(givenProduct.getId());
		assertNotNull(serviceResult);
		assertEquals(givenProduct,serviceResult);
	}
	
	
	@Test
	void givenId_whenNoProductFound_throwsProductNotFoundException() throws ProductNotFoundException {
		final String sampleId = "sampleId";
		when(productRepo.findById(sampleId)).thenReturn(Optional.ofNullable(null));
		assertThrows(ProductNotFoundException.class, () -> {
			productService.getProductById(sampleId);
		});
	}

	@Test
	void givenProduct_shouldBeSavedSuccessfully() throws CategoryNotFoundException  {
		Product testEntity = TestUtils.createProductEntity();
		when(productRepo.save(any())).thenReturn(testEntity);
		Product newProduct = productService.saveNewProduct(testEntity);
		assertEquals(testEntity, newProduct);
	}
	
	@Test
	void givenProduct_shouldBeUpdatedSuccessfully() throws ProductNotFoundException, CategoryNotFoundException  {
		Product testEntity = TestUtils.createProductEntity();
		when(productRepo.findByIdAndStatus(testEntity.getId(), STATUS_ACTIVE)).thenReturn(Optional.ofNullable(testEntity));
		when(productRepo.save(any())).thenReturn(testEntity);
		Product newProduct = productService.updateProduct(testEntity);
		assertEquals(testEntity, newProduct);
	}
}
