package com.fronchak.dscatalog.api.controllers;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fronchak.dscatalog.api.dtos.ProductDTO;
import com.fronchak.dscatalog.domain.services.ProductService;

@RestController
@RequestMapping(value = "/api/products")
public class ProductController {

	@Autowired
	private ProductService service;
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
		ProductDTO product = service.findById(id);
		return ResponseEntity.ok().body(product);
	}
	
	@GetMapping
	public ResponseEntity<Page<ProductDTO>> findAllPaged(
			@RequestParam(value = "categoryId", defaultValue = "0") Long categoryId,
			@RequestParam(value = "filter", defaultValue = "") String filter,
			Pageable pageable) {
		Page<ProductDTO> dtos = service.findAllPaged(categoryId, filter.trim(), pageable);
		return ResponseEntity.ok().body(dtos);
	}
	
	@GetMapping(value = "/postgree")
	public ResponseEntity<Page<ProductDTO>> findAllPagedPostgree(
			@RequestParam(value = "categoryId", defaultValue = "0") Long categoryId,
			@RequestParam(value = "filter", defaultValue = "") String filter,
			Pageable pageable) {
		Page<ProductDTO> dtos = service.findAllPagedPostgree(categoryId, filter.trim(), pageable);
		return ResponseEntity.ok().body(dtos);
	}
	
	@PostMapping
	public ResponseEntity<ProductDTO> save(@Valid @RequestBody ProductDTO dto) {
		dto = service.save(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<ProductDTO> update(@PathVariable Long id, @Valid @RequestBody ProductDTO dto) {
		dto = service.update(id, dto);
		return ResponseEntity.ok().body(dto);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
