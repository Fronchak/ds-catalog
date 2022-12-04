package com.fronchak.dscatalog.domain.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.fronchak.dscatalog.domain.entities.Product;
import com.fronchak.dscatalog.mocks.ProductMockFactory;

@DataJpaTest
public class ProductRepositoryTest {

	private Long validId;
	private Long invalidId;
	
	@Autowired
	private ProductRepository repository;
	
	@BeforeEach
	public void setUp() {
		validId = 1L;
		invalidId = 1000L;
	}
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		repository.deleteById(validId);
		Optional<Product> result = repository.findById(validId);
		assertTrue(result.isEmpty());	
	}
	
	@Test
	public void deleteShouldThrowExceptionWhenDeleteIdNonexistent() {
		assertThrows(EmptyResultDataAccessException.class, () -> repository.deleteById(invalidId));
	}
	
	@Test
	public void saveShouldSaveNewObjectWhenIdIsNull() {
		Product entity = ProductMockFactory.mockProduct();
		entity.setId(null);
		
		entity = repository.save(entity);
		
		assertNotNull(entity.getId());
		assertEquals(26, entity.getId());
	}
	
	@Test
	public void findByIdShouldReturnNonEmptyOptionalWhenIdExist() {
		Optional<Product> result = repository.findById(validId);
		assertTrue(result.isPresent());
	}
	
	@Test
	public void findByIdShouldReturnEmptyOptionalWhenIdDoesNotExist() {
		Optional<Product> result = repository.findById(invalidId);
		assertTrue(result.isEmpty());
	}
}
