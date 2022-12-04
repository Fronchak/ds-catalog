package com.fronchak.dscatalog.domain.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fronchak.dscatalog.api.dtos.ProductDTO;
import com.fronchak.dscatalog.api.mappers.ProductMapper;
import com.fronchak.dscatalog.domain.entities.Product;
import com.fronchak.dscatalog.domain.exceptions.DatabaseException;
import com.fronchak.dscatalog.domain.exceptions.ResourceNotFoundException;
import com.fronchak.dscatalog.domain.repositories.ProductRepository;
import com.fronchak.dscatalog.mocks.ProductMockFactory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {
	
	private Long validId;
	private Long invalidId;
	private Long dependentId;
	
	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;
	
	@Mock
	private ProductMapper mapper;
	
	@BeforeEach
	void setUp() {
		validId = 1L;
		invalidId = 1000L;
		dependentId = 500L;		
		doNothing().when(repository).deleteById(validId);
		doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(invalidId);
		doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		assertDoesNotThrow(() -> service.delete(validId));
		verify(repository, times(1)).deleteById(validId);
	}
	
	@Test
	public void deleteShouldThrowExceptionWhenIdDoesNotExists() {
		assertThrows(ResourceNotFoundException.class, () -> service.delete(invalidId));
		verify(repository, times(1)).deleteById(invalidId);
	}
	
	@Test
	public void deleteShouldThrowExceptionWhenIdIsDependent() {
		assertThrows(DatabaseException.class, () -> service.delete(dependentId));
		verify(repository, times(1)).deleteById(dependentId);
	}
	
	@Test
	public void findByIdShouldReturnProductDTOWhenIdExists() {
		Product entity = ProductMockFactory.mockProduct();
		ProductDTO dto = ProductMockFactory.mockProductDTO();
		
		when(repository.findById(10L)).thenReturn(Optional.of(entity));
		when(mapper.convertEntityToDTO(entity, entity.getCategories())).thenReturn(dto);
		
		ProductDTO result = service.findById(10L);
		
		assertEquals(0L, result.getId());
		assertEquals("Mock name 0", result.getName());
		assertEquals("Mock description 0", result.getDescription());
		assertEquals("Mock url 0", result.getImgUrl());
		assertEquals(0.0, result.getPrice());
		assertEquals(Instant.parse("2000-10-05T12:00:00Z"), result.getDate());
	}
	
	@Test
	public void findAllPagedShouldReturnPageWithProductDTO() {
		Page<Product> products = ProductMockFactory.mockProductPage();
		Page<ProductDTO> dtos = ProductMockFactory.mockProductDTOPage();
		
		when(repository.findAll((Pageable) any())).thenReturn(products);
		when(mapper.convertPageEntityToPageDTO(products)).thenReturn(dtos);
		
		Page<ProductDTO> results = service.findAllPaged(any());
		ProductDTO result = results.getContent().get(0);
		
		assertEquals(0L, result.getId());
		assertEquals("Mock name 0", result.getName());
		assertEquals("Mock description 0", result.getDescription());
		assertEquals("Mock url 0", result.getImgUrl());
		assertEquals(0.0, result.getPrice());
		assertEquals(Instant.parse("2000-10-05T12:00:00Z"), result.getDate());
		
		result = results.getContent().get(3);
		
		assertEquals(3L, result.getId());
		assertEquals("Mock name 3", result.getName());
		assertEquals("Mock description 3", result.getDescription());
		assertEquals("Mock url 3", result.getImgUrl());
		assertEquals(3.0, result.getPrice());
		assertEquals(Instant.parse("2000-10-05T12:00:00Z"), result.getDate());
	}
}
