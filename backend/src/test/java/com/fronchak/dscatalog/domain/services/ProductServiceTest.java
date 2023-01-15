package com.fronchak.dscatalog.domain.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

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
import com.fronchak.dscatalog.domain.entities.Category;
import com.fronchak.dscatalog.domain.entities.Product;
import com.fronchak.dscatalog.domain.exceptions.DatabaseException;
import com.fronchak.dscatalog.domain.exceptions.ResourceNotFoundException;
import com.fronchak.dscatalog.domain.repositories.CategoryRepository;
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
	private CategoryRepository categoryRepository;

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
		
		ProductDTO expected = ProductMockFactory.mockProductDTO();
		ProductDTO result = service.findById(10L);

		assertDTOsAreEquals(expected, result);
	}
	
	private void assertDTOsAreEquals(ProductDTO expected, ProductDTO result) {
		assertEquals(expected.getId(), result.getId());
		assertEquals(expected.getName(), result.getName());
		assertEquals(expected.getDescription(), result.getDescription());
		assertEquals(expected.getImgUrl(), result.getImgUrl());
		assertEquals(expected.getPrice(), result.getPrice());
		assertEquals(expected.getDate(), result.getDate());
	}
	
	@Test
	public void findByIdShouldReturnResourceNotFoundExceptionWhenIdDoesNotExist() {
		Long invalidId = 1L;
		
		when(repository.findById(invalidId)).thenReturn(Optional.empty());
		
		assertThrows(ResourceNotFoundException.class, () -> service.findById(invalidId));
	}
	
	@Test
	public void findAllPagedShouldReturnPageWithProductDTO() {
		Page<Product> products = ProductMockFactory.mockProductPage();
		Page<ProductDTO> dtos = ProductMockFactory.mockProductDTOPage();
		
		when(repository.findFiltered(any(), any(), any())).thenReturn(products);
		when(mapper.convertPageEntityToPageDTO(products)).thenReturn(dtos);
		
		Page<ProductDTO> results = service.findAllPaged(eq(0L), eq(""), any());
		
		ProductDTO expected = ProductMockFactory.mockProductDTO();
		ProductDTO result = results.getContent().get(0);
		
		assertDTOsAreEquals(expected, result);
		
		expected = ProductMockFactory.mockProductDTO(3);
		result = results.getContent().get(3);
		
		assertDTOsAreEquals(expected, result);
	}
	
	@Test
	public void updateShouldReturnProductDTOWhenIdExists() {
		Long validId = 1L;
		Product productPersisted = ProductMockFactory.mockProduct();
		Product updatedProduct = ProductMockFactory.mockProduct(1);
		ProductDTO inputDTO = ProductMockFactory.mockProductDTO();
		ProductDTO outputDTO = ProductMockFactory.mockProductDTO(1);
		
		
		when(repository.getReferenceById(validId)).thenReturn(productPersisted);
		doNothing().when(mapper).convertDTOToEntity(inputDTO, productPersisted);
		when(repository.save(productPersisted)).thenReturn(updatedProduct);
		when(mapper.convertEntityToDTO(updatedProduct, updatedProduct.getCategories())).thenReturn(outputDTO);
		
		ProductDTO expected = ProductMockFactory.mockProductDTO(1);
		ProductDTO result = service.update(validId, inputDTO);

		assertDTOsAreEquals(expected, result);
		verify(repository, times(1)).save(productPersisted);
		verify(repository, times(1)).getReferenceById(validId);
		verify(mapper, times(1)).convertDTOToEntity(inputDTO, productPersisted);
		verify(mapper, times(1)).convertEntityToDTO(updatedProduct, updatedProduct.getCategories());
	}
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Long invalidId = 1L;
		ProductDTO dto = ProductMockFactory.mockProductDTO();
		doThrow(EntityNotFoundException.class).when(repository).getReferenceById(invalidId);
		
		assertThrows(ResourceNotFoundException.class, () -> service.update(invalidId, dto));
		
		verify(repository, times(1)).getReferenceById(invalidId);
		verify(repository, never()).save(any());
	}
}
