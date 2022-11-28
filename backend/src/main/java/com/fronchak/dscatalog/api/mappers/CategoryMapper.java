package com.fronchak.dscatalog.api.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fronchak.dscatalog.api.dtos.CategoryDTO;
import com.fronchak.dscatalog.domain.entities.Category;

@Service
public class CategoryMapper {

	public CategoryDTO convertEntityToDTO(Category entity) {
		CategoryDTO dto = new CategoryDTO();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		return dto;
	}
	
	public List<CategoryDTO> convertEntityListToDTOList(List<Category> entities) {
		return entities.stream()
				.map(entity -> convertEntityToDTO(entity))
				.collect(Collectors.toList());
	}
	
	public Category convertDTOToEntity(CategoryDTO dto) {
		Category entity = new Category();
		entity.setId(dto.getId());
		entity.setName(dto.getName());
		return entity;
	}
}
