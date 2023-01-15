package com.fronchak.dscatalog.api.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fronchak.dscatalog.api.dtos.ProductDTO;
import com.fronchak.dscatalog.domain.exceptions.ResourceNotFoundException;
import com.fronchak.dscatalog.domain.services.ProductService;
import com.fronchak.dscatalog.mocks.ProductMockFactory;
import com.fronchak.dscatalog.util.TokenUtil;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {
	
	private Long existingId;
	private Long nonExistingId;
	private String username;
	private String password;

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private TokenUtil tokenUtil;
	
	@MockBean
	private ProductService service;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@BeforeEach
	void setUp() {
		existingId = 1L;
		nonExistingId = 2L;
		username = "alex@gmail.com";
		password = "123456";
		
		when(service.findAllPaged(any(), any(), any())).thenReturn(ProductMockFactory.mockProductDTOPage());
		
		when(service.findById(existingId)).thenReturn(ProductMockFactory.mockProductDTO());
		when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
		
	}
	
	@Test
	public void findAllShouldReturnPage() throws Exception {
		ResultActions result = mockMvc.perform(get("/api/products")
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
	}
	
	@Test
	public void findByIdShouldReturnProductDTOWhenIdExists() throws Exception {
		
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
		
		ResultActions result = mockMvc.perform(get("/api/products/{id}", existingId)
				.header("Authorization", "Bearer " + accessToken)
				.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
		result.andExpect(jsonPath("$.price").exists());
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
		
		ResultActions result = mockMvc.perform(get("/api/products/{id}", nonExistingId)
				.header("Authorization", "Bearer " + accessToken)
				.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void updateShouldReturnProductDTOWhenIdExists() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
		
		ProductDTO product = ProductMockFactory.mockProductDTO();
		product.setPrice(10.0);
		String body = objectMapper.writeValueAsString(product);
		when(service.update(eq(existingId), any())).thenReturn(ProductMockFactory.mockProductDTO(1));
		
		ResultActions result = mockMvc.perform(put("/api/products/{id}", existingId)
				.header("Authorization", "Bearer " + accessToken)
				.content(body)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
		result.andExpect(jsonPath("$.price").exists());
	}
	
	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
		
		ProductDTO product = ProductMockFactory.mockProductDTO();
		product.setPrice(10.0);
		String body = objectMapper.writeValueAsString(product);
		when(service.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);
		
		ResultActions result = mockMvc.perform(put("/api/products/{id}", nonExistingId)
				.header("Authorization", "Bearer " + accessToken)
				.content(body)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
		
		doNothing().when(service).delete(existingId);
		
		ResultActions result = mockMvc.perform(delete("/api/products/{id}", existingId)
				.header("Authorization", "Bearer " + accessToken)
				.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNoContent());
	}
	
	@Test
	public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
		
		doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
		
		ResultActions result = mockMvc.perform(delete("/api/products/{id}", nonExistingId)
				.header("Authorization", "Bearer " + accessToken)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void saveShouldReturnProductDTO() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
		
		ProductDTO product = ProductMockFactory.mockProductDTO();
		product.setPrice(10.0);
		product.setId(null);
		
		when(service.save(any())).thenReturn(ProductMockFactory.mockProductDTO());
		
		String body = objectMapper.writeValueAsString(product);
		ResultActions result = mockMvc.perform(post("/api/products")
				.header("Authorization", "Bearer " + accessToken)
				.content(body)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
		result.andExpect(jsonPath("$.price").exists());
		result.andExpect(jsonPath("$.date").exists());
	}
	
}
