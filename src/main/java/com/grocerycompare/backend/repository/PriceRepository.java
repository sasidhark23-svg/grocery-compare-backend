package com.grocerycompare.backend.repository;

import com.grocerycompare.backend.entity.Price;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PriceRepository extends JpaRepository<Price, Long> {

    List<Price> findByProductId(Long productId);

    List<Price> findByProductIdOrderByPriceAsc(Long productId);

    Optional<Price> findByProductIdAndStoreId(Long productId, Long storeId);

    void deleteByProductId(Long productId);

    void deleteByStoreId(Long storeId);
}