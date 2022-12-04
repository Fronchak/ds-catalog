package com.fronchak.dscatalog.mocks;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.fronchak.dscatalog.api.dtos.ProductDTO;
import com.fronchak.dscatalog.domain.entities.Product;

public class ProductMockFactory {

	public static Product mockProduct() {
		return mockProduct(0);
	}
	
	public static Product mockProduct(int number) {
		Product product = new Product();
		product.setId(mockId(number));
		product.setName(mockName(number));
		product.setDescription(mockDescription(number));
		product.setImgUrl(mockUrl(number));
		product.setPrice(mockPrice(number));
		product.setDate(mockDate());
		return product;
	}
	
	private static Long mockId(int number) {
		return number + 0L;
	}
	
	private static String mockName(int number) {
		return "Mock name " + number;
	}
	
	private static String mockDescription(int number) {
		return "Mock description " + number;
	}
	
	private static String mockUrl(int number) {
		return "Mock url " + number;
	}
	
	private static Double mockPrice(int number) {
		return number + 0.0;
	}
	
	private static Instant mockDate() {
		return Instant.parse("2000-10-05T12:00:00Z");
	}
	
	public static ProductDTO mockProductDTO() {
		return mockProductDTO(0);
	}
	
	public static List<Product> mockProductList() {
		List<Product> products = new ArrayList<>();
		for(int i = 0; i < 6; i++) {
			products.add(mockProduct(i));
		}
		return products;
	}
	
	public static Page<Product> mockProductPage() {
		return new PageImpl<>(mockProductList());
	}
	
	public static ProductDTO mockProductDTO(int number) {
		ProductDTO product = new ProductDTO();
		product.setId(mockId(number));
		product.setName(mockName(number));
		product.setDescription(mockDescription(number));
		product.setImgUrl(mockUrl(number));
		product.setPrice(mockPrice(number));
		product.setDate(mockDate());
		return product;
	}
	
	public static List<ProductDTO> mockProductDTOList() {
		List<ProductDTO> products = new ArrayList<>();
		for(int i = 0; i < 6; i++) {
			products.add(mockProductDTO(i));
		}
		return products;
	}
	
	public static Page<ProductDTO> mockProductDTOPage() {
		return new PageImpl<>(mockProductDTOList());
	}
}
