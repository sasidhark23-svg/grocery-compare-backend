package com.grocerycompare.backend.repository;

import com.grocerycompare.backend.entity.ShoppingListItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShoppingListItemRepository extends JpaRepository<ShoppingListItem, Long> {

    Optional<ShoppingListItem> findByShoppingListIdAndProductId(Long shoppingListId, Long productId);
}
