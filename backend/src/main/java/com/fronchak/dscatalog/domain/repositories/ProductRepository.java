package com.fronchak.dscatalog.domain.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fronchak.dscatalog.domain.entities.Category;
import com.fronchak.dscatalog.domain.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	@Query("SELECT DISTINCT obj FROM Product obj INNER JOIN obj.categories cats WHERE " + 
			"(:category IS NULL OR :category IN cats) AND " +
			"(UPPER(obj.name) LIKE UPPER(CONCAT('%', :filter, '%')) OR  " + 
			"UPPER(obj.description) LIKE UPPER(CONCAT('%', :filter, '%')))")
	Page<Product> findFiltered(Category category, String filter, Pageable pageable);
	
	@Query("SELECT DISTINCT obj FROM Product obj INNER JOIN obj.categories cats WHERE " + 
			"(COALESCE(:categories) IS NULL OR cats IN :categories) AND " +
			"(UPPER(obj.name) LIKE UPPER(CONCAT('%', :filter, '%')) OR  " + 
			"UPPER(obj.description) LIKE UPPER(CONCAT('%', :filter, '%')))")
	Page<Product> findFilteredPostgree(List<Category> categories, String filter, Pageable pageable);
	
	@Query("SELECT obj FROM Product obj JOIN FETCH obj.categories WHERE obj IN :products")
	List<Product> findProductsWithCategories(List<Product> products); 
}
