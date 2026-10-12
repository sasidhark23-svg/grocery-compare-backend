package com.grocerycompare.backend.repository;

import com.grocerycompare.backend.entity.PriceAlert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceAlertRepository extends JpaRepository<PriceAlert, Long> {

    List<PriceAlert> findByUserId(Long userId);

    List<PriceAlert> findByActiveTrue();

    List<PriceAlert> findByProductIdAndActiveTrue(Long productId);
}
