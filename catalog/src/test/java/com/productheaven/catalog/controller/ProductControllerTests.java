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

import com.productheaven.catalog.api.schema.request.DeleteProductRequestDTO;
import com.productheaven.catalog.api.schema.request.ProductRequestDTO;
import com.productheaven.catalog.api.schema.response.ProductDTO;
import com.productheaven.catalog.persistence.entity.Product;
import com.productheaven.catalog.service.ProductService;
import com.productheaven.catalog.service.RequestValidationService;
import com.productheaven.catalog.service.exception.ProductNotFoundException;
import com.productheaven.catalog.util.TestUtils;

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
		.perform(get("/product"))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(content().string(containsString(randomId)));
	}
	
	@Test
	void whenServiceReturnsNoData_throwsProductNotFoundException() throws Exception {
		when(mockProductService.getAllProducts()).thenThrow(new ProductNotFoundException());
		Exception thrown = assertThrows(NestedServletException.class, () -> {
			this.mockMvc.perform(get("/product"));
		});
		assertTrue(thrown.getCause() instanceof ProductNotFoundException);
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
	
	@Test
	void givenCategoryId_productsShouldBeFetchedSuccessfully() throws Exception {
		// given,
		String categoryId = UUID.randomUUID().toString();
		Product product = TestUtils.createProductEntity(categoryId);
		List<Product> products = List.of(product);

		// when
		when(mockProductService.getProductsByCategoryId(categoryId)).thenReturn(products);
		when(mockModelMapper.map(product, ProductDTO.class)).thenReturn(testMapper.map(product, ProductDTO.class));

		// expect
		this.mockMvc
		.perform(get("/products-by-category/" + categoryId))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(content().string(containsString(categoryId)));
	}
	
	@Test
	void givenProductShouldBeCreatedSuccessfully() throws Exception {
		// given
		String categoryId = UUID.randomUUID().toString();
		String productId = UUID.randomUUID().toString();
		ProductRequestDTO productRequest = TestUtils.createProductRequestDTO(categoryId);
		Product mappedMockProduct = testMapper.map(productRequest, Product.class);
		mappedMockProduct.setId(productId);
		mappedMockProduct.setCreatedBy(productRequest.getActionUser());
		ProductDTO mappedMockProductDTO = testMapper.map(mappedMockProduct, ProductDTO.class);

		// when
		when(mockModelMapper.map(productRequest, Product.class)).thenReturn(mappedMockProduct);
		when(mockProductService.saveNewProduct(mappedMockProduct)).thenReturn(mappedMockProduct);
		when(mockModelMapper.map(mappedMockProduct, ProductDTO.class)).thenReturn(testMapper.map(mappedMockProductDTO, ProductDTO.class));
	
		this.mockMvc
		.perform(post("/product")
				.content(TestUtils.objectToJsonString(productRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated()).andDo(print())
		.andExpect(MockMvcResultMatchers.jsonPath("$.product.id",is(productId)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.product.categoryId",is(categoryId)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.product.description",is(productRequest.getDescription())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.product.name",is(productRequest.getName())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.product.imagePath",is(productRequest.getImagePath())))
 		.andExpect(MockMvcResultMatchers.jsonPath("$.product.createdBy",is(productRequest.getActionUser())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.product.price",is(productRequest.getPrice())));

	}
	
	@Test
	void productShouldBeUpdatedSuccessfully() throws Exception {
		// given,
		ProductRequestDTO updateRequest = TestUtils.createProductUpdateRequestDTO();
		String productId = UUID.randomUUID().toString();
		Product mappedMockProduct = testMapper.map(updateRequest, Product.class);
		mappedMockProduct.setId(productId);
		mappedMockProduct.setLastUpdatedBy(updateRequest.getActionUser());
		ProductDTO mappedMockProductDTO = testMapper.map(mappedMockProduct, ProductDTO.class);

		// when
		when(mockModelMapper.map(updateRequest, Product.class)).thenReturn(mappedMockProduct);
		when(mockProductService.updateProduct(mappedMockProduct)).thenReturn(mappedMockProduct);
		when(mockModelMapper.map(mappedMockProduct, ProductDTO.class)).thenReturn(testMapper.map(mappedMockProductDTO, ProductDTO.class));
	
		this.mockMvc
		.perform(put("/product/"+productId)
				.content(TestUtils.objectToJsonString(updateRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk()).andDo(print())
		.andExpect(MockMvcResultMatchers.jsonPath("$.product.id",is(productId)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.product.categoryId",is(updateRequest.getCategoryId())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.product.description",is(updateRequest.getDescription())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.product.name",is(updateRequest.getName())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.product.imagePath",is(updateRequest.getImagePath())))
 		.andExpect(MockMvcResultMatchers.jsonPath("$.product.lastUpdatedBy",is(updateRequest.getActionUser())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.product.price",is(updateRequest.getPrice())));

	}
	
	@Test
	void productShouldBeDeletedSuccessfully() throws Exception {
		// given,
		DeleteProductRequestDTO deleteRequest = new DeleteProductRequestDTO("Deleter!");
		String productId = UUID.randomUUID().toString();
		
		this.mockMvc
		.perform(delete("/product/"+productId)
				.content(TestUtils.objectToJsonString(deleteRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}
	
	
}
