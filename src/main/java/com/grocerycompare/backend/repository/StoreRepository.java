package com.grocerycompare.backend.repository;

import com.grocerycompare.backend.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {

    java.util.Optional<Store> findByStoreNameIgnoreCase(String storeName);

}
