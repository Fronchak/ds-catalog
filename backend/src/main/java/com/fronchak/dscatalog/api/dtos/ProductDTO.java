package com.fronchak.dscatalog.api.dtos;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class ProductDTO {

	private Long id;
	
	@Size(min = 3, max = 60, message = "Name must has between 3 and 60 characteres")
	@NotBlank(message = "Name must be specified")
	private String name;
	private String description;
	
	@Positive(message = "Price must be a positive value")
	private Double price;
	private String imgUrl;
	
	@PastOrPresent(message = "Product's date cannot be future")
	private Instant date;
	
	private List<CategoryDTO> categories = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Instant getDate() {
		return date;
	}

	public void setDate(Instant date) {
		this.date = date;
	}

	public List<CategoryDTO> getCategories() {
		return categories;
	}
}
