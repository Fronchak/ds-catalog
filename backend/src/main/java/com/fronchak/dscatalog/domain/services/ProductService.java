package com.fronchak.dscatalog.domain.services;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fronchak.dscatalog.api.dtos.ProductDTO;
import com.fronchak.dscatalog.api.mappers.ProductMapper;
import com.fronchak.dscatalog.domain.entities.Product;
import com.fronchak.dscatalog.domain.exceptions.DatabaseException;
import com.fronchak.dscatalog.domain.exceptions.ResourceNotFoundException;
import com.fronchak.dscatalog.domain.repositories.CategoryRepository;
import com.fronchak.dscatalog.domain.repositories.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ProductMapper mapper;
	
	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Product entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found"));
		return mapper.convertEntityToDTO(entity, entity.getCategories());
	}
	
	public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
		Page<Product> entities = repository.findAll(pageRequest);
		return mapper.convertPageEntityToPageDTO(entities);
	}
	
	@Transactional
	public ProductDTO save(ProductDTO dto) {
		Product entity = new Product();
		copyDTOToEntity(dto, entity);
		entity = repository.save(entity);
		return mapper.convertEntityToDTO(entity, entity.getCategories());
	}
	
	private void copyDTOToEntity(ProductDTO dto, Product entity) {
		mapper.convertDTOToEntity(dto, entity);
		entity.getCategories().clear();
		dto.getCategories()
				.forEach(categoryDTO -> 
				entity.getCategories().add(categoryRepository.getReferenceById(categoryDTO.getId())));
	}
	
	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try {
			Product entity = repository.getReferenceById(id);
			copyDTOToEntity(dto, entity);
			entity = repository.save(entity);
			return mapper.convertEntityToDTO(entity, entity.getCategories());
		}
		catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("Product not found");
		}
	}
	
	public void delete(Long id) {
		try {
			repository.deleteById(id);
		}
		catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Product not found");
		}
		catch(DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}
	}
}
