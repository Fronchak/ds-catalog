package com.fronchak.dscatalog.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fronchak.dscatalog.domain.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
