package com.bijoy.cloud.productservice.repository;

import com.bijoy.cloud.productservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
