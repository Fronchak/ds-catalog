package com.fronchak.dscatalog.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.fronchak.dscatalog.api.dtos.ProductDTO;
import com.fronchak.dscatalog.api.mappers.ProductMapper;
import com.fronchak.dscatalog.domain.entities.Product;
import com.fronchak.dscatalog.domain.exceptions.ResourceNotFoundException;
import com.fronchak.dscatalog.domain.repositories.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;
	
	@Autowired
	private ProductMapper mapper;
	
	public ProductDTO findById(Long id) {
		Product entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found"));
		return mapper.convertEntityToDTO(entity, entity.getCategories());
	}
	
	public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
		Page<Product> entities = repository.findAll(pageRequest);
		return mapper.convertPageEntityToPageDTO(entities);
	}
}
