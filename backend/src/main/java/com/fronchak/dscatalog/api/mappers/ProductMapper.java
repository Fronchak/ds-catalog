package com.fronchak.dscatalog.api.mappers;

import java.io.Serializable;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.fronchak.dscatalog.api.dtos.ProductDTO;
import com.fronchak.dscatalog.domain.entities.Category;
import com.fronchak.dscatalog.domain.entities.Product;

@Service
public class ProductMapper implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private CategoryMapper categoryMapper;

	public ProductDTO convertEntityToDTO(Product entity) {
		ProductDTO dto = new ProductDTO();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setDescription(entity.getDescription());
		dto.setPrice(entity.getPrice());
		dto.setImgUrl(entity.getImgUrl());
		dto.setDate(entity.getDate());
		return dto;
	}
	
	public ProductDTO convertEntityToDTO(Product entity, Set<Category> categories) {
		ProductDTO dto = convertEntityToDTO(entity);	
		categories.forEach(category -> dto.getCategories().add(categoryMapper.convertEntityToDTO(category)));
		return dto;
	}
	
	public Page<ProductDTO> convertPageEntityToPageDTO(Page<Product> entities) {
		return entities
				.map(entity -> convertEntityToDTO(entity));
	}
	
	public void convertDTOToEntity(ProductDTO dto, Product entity) {
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setPrice(dto.getPrice());
		entity.setImgUrl(dto.getImgUrl());
		entity.setDate(dto.getDate());
	}
}
