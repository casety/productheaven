package com.productheaven.catalog.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import com.productheaven.catalog.api.schema.request.CategoryRequestDTO;
import com.productheaven.catalog.api.schema.request.DeleteCategoryRequestDTO;
import com.productheaven.catalog.api.schema.response.CategoryDTO;
import com.productheaven.catalog.persistence.entity.Category;
import com.productheaven.catalog.service.CategoryService;
import com.productheaven.catalog.service.RequestValidationService;
import com.productheaven.catalog.service.exception.CategoryNotFoundException;
import com.productheaven.catalog.util.TestUtils;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK, classes = { CategoryController.class })
class CategoryControllerTests {

	private MockMvc mockMvc;

	@MockBean
	private CategoryService mockCategoryService;
	
	@MockBean
	private RequestValidationService validationService;

	@MockBean
	private ModelMapper mockModelMapper;
	
	
	private ModelMapper testMapper = new ModelMapper();
	
	@BeforeEach
	void init() {
		MockitoAnnotations.openMocks(this);
		this.mockMvc = MockMvcBuilders
				.standaloneSetup(new CategoryController(mockCategoryService,mockModelMapper,validationService)).build();
	}
	
	@Test
	void whenServiceReturnsData_productsToBeListedSuccessfully() throws Exception {

		// given
		String randomId = UUID.randomUUID().toString();
		Category product = new Category(randomId);
		List<Category> result = List.of(product);

		// when
		when(mockCategoryService.getAllCategories()).thenReturn(result);
		when(mockModelMapper.map(product, CategoryDTO.class)).thenReturn(testMapper.map(product, CategoryDTO.class));

		// expect
		this.mockMvc
		.perform(get("/category"))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(content().string(containsString(randomId)));
	}
	
	@Test
	void whenServiceReturnsNoData_throwsCategoryNotFoundException() throws Exception {
		when(mockCategoryService.getAllCategories()).thenThrow(new CategoryNotFoundException());
		Exception thrown = assertThrows(NestedServletException.class, () -> {
			this.mockMvc.perform(get("/category"));
		});
		assertTrue(thrown.getCause() instanceof CategoryNotFoundException);
	}
	
	@Test
	void givenCategoryId_productsShouldBeFetchedSuccessfully() throws Exception {
		// given,
		String randomId = UUID.randomUUID().toString();
		Category product = new Category(randomId);

		// when
		when(mockCategoryService.getCategoryById(randomId)).thenReturn(product);
		when(mockModelMapper.map(product, CategoryDTO.class)).thenReturn(testMapper.map(product, CategoryDTO.class));

		// expect
		this.mockMvc
		.perform(get("/category/" + randomId))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(content().string(containsString(randomId)));
	}

	
	@Test
	void givenCategoryShouldBeCreatedSuccessfully() throws Exception {
		
		// given
		String categoryId = UUID.randomUUID().toString();
		CategoryRequestDTO productRequest = TestUtils.createCategoryRequestDTO();
		Category mappedMockCategory = testMapper.map(productRequest, Category.class);
		mappedMockCategory.setId(categoryId);
		mappedMockCategory.setCreatedBy(productRequest.getActionUser());
		CategoryDTO mappedMockCategoryDTO = testMapper.map(mappedMockCategory, CategoryDTO.class);

		// when
		when(mockModelMapper.map(productRequest, Category.class)).thenReturn(mappedMockCategory);
		when(mockCategoryService.saveNewCategory(mappedMockCategory)).thenReturn(mappedMockCategory);
		when(mockModelMapper.map(mappedMockCategory, CategoryDTO.class)).thenReturn(testMapper.map(mappedMockCategoryDTO, CategoryDTO.class));

		
		this.mockMvc
		.perform(post("/category")
				.content(TestUtils.objectToJsonString(productRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated()).andDo(print())
		.andExpect(MockMvcResultMatchers.jsonPath("$.category.id",is(categoryId)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.category.description",is(productRequest.getDescription())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.category.name",is(productRequest.getName())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.category.imagePath",is(productRequest.getImagePath())))
 		.andExpect(MockMvcResultMatchers.jsonPath("$.category.createdBy",is(productRequest.getActionUser())));

	}
	
	@Test
	void categoryShouldBeUpdatedSuccessfully() throws Exception {
		// given,
		CategoryRequestDTO updateRequest = TestUtils.createCategoryUpdateRequestDTO();
		String categoryId = UUID.randomUUID().toString();
		Category mappedMockCategory = testMapper.map(updateRequest, Category.class);
		mappedMockCategory.setId(categoryId);
		mappedMockCategory.setLastUpdatedBy(updateRequest.getActionUser());
		CategoryDTO mappedMockCategoryDTO = testMapper.map(mappedMockCategory, CategoryDTO.class);

		// when
		when(mockModelMapper.map(updateRequest, Category.class)).thenReturn(mappedMockCategory);
		when(mockCategoryService.updateCategory(mappedMockCategory)).thenReturn(mappedMockCategory);
		when(mockModelMapper.map(mappedMockCategory, CategoryDTO.class)).thenReturn(testMapper.map(mappedMockCategoryDTO, CategoryDTO.class));
	
		this.mockMvc
		.perform(put("/category/"+categoryId)
				.content(TestUtils.objectToJsonString(updateRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk()).andDo(print())
		.andExpect(MockMvcResultMatchers.jsonPath("$.category.id",is(categoryId)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.category.description",is(updateRequest.getDescription())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.category.name",is(updateRequest.getName())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.category.imagePath",is(updateRequest.getImagePath())))
 		.andExpect(MockMvcResultMatchers.jsonPath("$.category.lastUpdatedBy",is(updateRequest.getActionUser())));
	}
	
	@Test
	void categoryShouldBeDeletedSuccessfully() throws Exception {
		// given,
		DeleteCategoryRequestDTO deleteRequest = new DeleteCategoryRequestDTO("Deleter!");
		String categoryId = UUID.randomUUID().toString();
		
		this.mockMvc
		.perform(delete("/category/"+categoryId)
				.content(TestUtils.objectToJsonString(deleteRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}
	
	
}
