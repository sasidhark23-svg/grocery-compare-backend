package com.grocerycompare.backend.repository;

import com.grocerycompare.backend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    java.util.Optional<Product> findByBarcode(String barcode);

    List<Product> findByNameContainingIgnoreCase(String name);

    @Query("SELECT p FROM Product p WHERE " +
           "(:query IS NULL OR :query = '' OR LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(p.brand) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
           "(:category IS NULL OR :category = '' OR LOWER(p.category) = LOWER(:category)) AND " +
           "(:brand IS NULL OR :brand = '' OR LOWER(p.brand) = LOWER(:brand))")
    Page<Product> searchProducts(
            @Param("query") String query,
            @Param("category") String category,
            @Param("brand") String brand,
            Pageable pageable
    );
}
