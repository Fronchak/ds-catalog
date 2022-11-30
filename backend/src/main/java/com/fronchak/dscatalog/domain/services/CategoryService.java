package com.fronchak.dscatalog.domain.services;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fronchak.dscatalog.api.dtos.CategoryDTO;
import com.fronchak.dscatalog.api.mappers.CategoryMapper;
import com.fronchak.dscatalog.domain.entities.Category;
import com.fronchak.dscatalog.domain.exceptions.ResourceNotFoundException;
import com.fronchak.dscatalog.domain.repositories.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;
	
	@Autowired
	private CategoryMapper mapper;
	
	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll() {
		List<Category> entities = repository.findAll();
		return mapper.convertEntityListToDTOList(entities);
	}
	
	public CategoryDTO findById(Long id) {
		Category entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found."));
		return mapper.convertEntityToDTO(entity);
	}
	
	public CategoryDTO save(CategoryDTO inputDTO) {
		Category entity = mapper.convertDTOToEntity(inputDTO);
		entity = repository.save(entity);
		return mapper.convertEntityToDTO(entity);
	}
	
	@Transactional
	public CategoryDTO update(Long id, CategoryDTO inputDTO) {
		try {
			Category entity = repository.getReferenceById(id);
			entity.setName(inputDTO.getName());
			entity = repository.save(entity);
			return mapper.convertEntityToDTO(entity);			
		}
		catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("ID not found: " + id);
		}

	}
}
