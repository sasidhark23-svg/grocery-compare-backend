package com.grocerycompare.backend.repository;

import com.grocerycompare.backend.entity.PriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Long> {

    List<PriceHistory> findByProductIdOrderByRecordedAtAsc(Long productId);

    List<PriceHistory> findByProductIdAndStoreIdOrderByRecordedAtAsc(Long productId, Long storeId);
}
