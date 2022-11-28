package com.fronchak.dscatalog.domain.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fronchak.dscatalog.api.dtos.CategoryDTO;
import com.fronchak.dscatalog.api.mappers.CategoryMapper;
import com.fronchak.dscatalog.domain.entities.Category;
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
}
