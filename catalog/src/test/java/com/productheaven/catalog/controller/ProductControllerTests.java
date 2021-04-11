package com.productheaven.catalog.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import com.productheaven.catalog.api.schema.response.ProductDTO;
import com.productheaven.catalog.controller.ProductController;
import com.productheaven.catalog.persistence.entity.Product;
import com.productheaven.catalog.service.ProductService;
import com.productheaven.catalog.service.RequestValidationService;
import com.productheaven.catalog.service.exception.NoProductsFoundException;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK, classes = { ProductController.class })
class ProductControllerTests {

	private MockMvc mockMvc;

	@MockBean
	private ProductService mockProductService;
	
	@MockBean
	private RequestValidationService validationService;

	@MockBean
	private ModelMapper mockModelMapper;
	
	private ModelMapper testMapper = new ModelMapper();
	
	@BeforeEach
	void init() {
		MockitoAnnotations.openMocks(this);
		this.mockMvc = MockMvcBuilders
				.standaloneSetup(new ProductController(mockProductService,mockModelMapper,validationService)).build();
	}
	
	@Test
	void whenServiceReturnsData_productsToBeListedSuccessfully() throws Exception {

		// given
		String randomId = UUID.randomUUID().toString();
		Product product = new Product(randomId);
		List<Product> result = List.of(product);

		// when
		when(mockProductService.getAllProducts()).thenReturn(result);
		when(mockModelMapper.map(product, ProductDTO.class)).thenReturn(testMapper.map(product, ProductDTO.class));

		// expect
		this.mockMvc
		.perform(get("/product")).andDo(print())
		.andExpect(status().isOk())
		.andExpect(content().string(containsString(randomId)));
	}
	
	@Test
	void whenServiceReturnsNoData_throwsNoProductFoundException() throws Exception {
		when(mockProductService.getAllProducts()).thenThrow(new NoProductsFoundException());
		Exception thrown = assertThrows(NestedServletException.class, () -> {
			this.mockMvc.perform(get("/product"));
		});
		assertTrue(thrown.getCause() instanceof NoProductsFoundException);
	}
	
	@Test
	void givenProductId_productsShouldBeFetchedSuccessfully() throws Exception {
		// given,
		String randomId = UUID.randomUUID().toString();
		Product product = new Product(randomId);

		// when
		when(mockProductService.getProductById(randomId)).thenReturn(product);
		when(mockModelMapper.map(product, ProductDTO.class)).thenReturn(testMapper.map(product, ProductDTO.class));

		// expect
		this.mockMvc
		.perform(get("/product/" + randomId))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(content().string(containsString(randomId)));
	}
	
}
