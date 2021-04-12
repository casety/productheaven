package com.productheaven.catalog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.productheaven.catalog.persistence.entity.Category;
import com.productheaven.catalog.persistence.repository.CategoryRepository;
import com.productheaven.catalog.service.exception.CategoryAlreadyExistsException;
import com.productheaven.catalog.service.exception.CategoryNotFoundException;
import com.productheaven.catalog.service.impl.CategoryServiceImpl;
import com.productheaven.catalog.util.TestUtils;

@ExtendWith(SpringExtension.class)
class CategoryServiceImplTests {

	@InjectMocks
	private CategoryServiceImpl categoryService;
	
	@Mock
	private CategoryRepository categoryRepo;
	
	private static final int STATUS_ACTIVE = 1;
	

	@Test
	void categorieshouldBeFetchedByIdSuccessfully() throws CategoryNotFoundException {
		Category givenCategory = TestUtils.createCategoryEntity();
		when(categoryRepo.findByIdAndStatus(givenCategory.getId(),STATUS_ACTIVE)).thenReturn(Optional.of(givenCategory));
		Category serviceResult = categoryService.getCategoryById(givenCategory.getId());
		assertNotNull(serviceResult);
		assertEquals(givenCategory,serviceResult);
	}
	
	
	@Test
	void givenId_whenNoCategoryFound_throwsCategoryNotFoundException() throws CategoryNotFoundException {
		final String sampleId = "sampleId";
		when(categoryRepo.findById(sampleId)).thenReturn(Optional.ofNullable(null));
		assertThrows(CategoryNotFoundException.class, () -> {
			categoryService.getCategoryById(sampleId);
		});
	}

	@Test
	void givenCategory_shouldBeSavedSuccessfully() throws CategoryAlreadyExistsException {
		Category testEntity = TestUtils.createCategoryEntity();
		when(categoryRepo.findByNameAndStatus(testEntity.getName(),STATUS_ACTIVE)).thenReturn(null);
		when(categoryRepo.save(any())).thenReturn(testEntity);
		Category newCategory = categoryService.saveNewCategory(testEntity);
		assertEquals(testEntity, newCategory);
	}
	
	@Test
	void givenCategory_whenCategroyAlreadyAvailable_throwsCategoryAlreadyExistsException() throws CategoryAlreadyExistsException {
		Category testEntity = TestUtils.createCategoryEntity();
		when(categoryRepo.findByNameAndStatus(testEntity.getName(),STATUS_ACTIVE)).thenReturn(List.of(new Category()));
		assertThrows(CategoryAlreadyExistsException.class, () -> {
			categoryService.saveNewCategory(testEntity);
		});

	}
}
