package com.grocerycompare.backend.service;

import com.grocerycompare.backend.DTO.ShoppingListComparisonDTO;
import com.grocerycompare.backend.entity.*;
import com.grocerycompare.backend.exception.ProductNotFoundException;
import com.grocerycompare.backend.exception.ResourceNotFoundException;
import com.grocerycompare.backend.exception.UserNotFoundException;
import com.grocerycompare.backend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ShoppingListService {

    private final ShoppingListRepository shoppingListRepository;
    private final ShoppingListItemRepository shoppingListItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PriceRepository priceRepository;
    private final StoreRepository storeRepository;

    public ShoppingListService(
            ShoppingListRepository shoppingListRepository,
            ShoppingListItemRepository shoppingListItemRepository,
            UserRepository userRepository,
            ProductRepository productRepository,
            PriceRepository priceRepository,
            StoreRepository storeRepository) {

        this.shoppingListRepository = shoppingListRepository;
        this.shoppingListItemRepository = shoppingListItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.priceRepository = priceRepository;
        this.storeRepository = storeRepository;
    }

    public List<ShoppingList> getUserLists(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userEmail));
        return shoppingListRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
    }

    public ShoppingList getListById(Long listId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userEmail));
        return shoppingListRepository.findByIdAndUserId(listId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Shopping list not found or unauthorized"));
    }

    @Transactional
    public ShoppingList createList(String userEmail, String name) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userEmail));
        ShoppingList list = new ShoppingList(user, name != null && !name.isBlank() ? name : "My Shopping List");
        return shoppingListRepository.save(list);
    }

    @Transactional
    public void deleteList(Long listId, String userEmail) {
        ShoppingList list = getListById(listId, userEmail);
        shoppingListRepository.delete(list);
    }

    @Transactional
    public ShoppingList addItemToList(Long listId, String userEmail, Long productId, Integer quantity) {
        ShoppingList list = getListById(listId, userEmail);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        Optional<ShoppingListItem> existingItemOpt = shoppingListItemRepository
                .findByShoppingListIdAndProductId(listId, productId);

        if (existingItemOpt.isPresent()) {
            ShoppingListItem item = existingItemOpt.get();
            item.setQuantity(item.getQuantity() + (quantity != null ? quantity : 1));
            shoppingListItemRepository.save(item);
        } else {
            ShoppingListItem newItem = new ShoppingListItem(list, product, quantity != null ? quantity : 1);
            list.getItems().add(newItem);
            shoppingListRepository.save(list);
        }

        return shoppingListRepository.findById(listId).orElse(list);
    }

    @Transactional
    public ShoppingList removeItemFromList(Long listId, String userEmail, Long itemId) {
        ShoppingList list = getListById(listId, userEmail);
        list.getItems().removeIf(item -> item.getId().equals(itemId));
        return shoppingListRepository.save(list);
    }

    public ShoppingListComparisonDTO compareListCost(Long listId, String userEmail) {
        ShoppingList list = getListById(listId, userEmail);
        ShoppingListComparisonDTO dto = new ShoppingListComparisonDTO();
        dto.setListId(list.getId());
        dto.setListName(list.getName());
        dto.setTotalItems(list.getItems().size());

        if (list.getItems().isEmpty()) {
            dto.setCheapestTotal(0.0);
            dto.setPotentialSavings(0.0);
            return dto;
        }

        double cheapestTotal = 0.0;
        double highestTotalSum = 0.0;
        List<ShoppingListComparisonDTO.ItemComparison> itemComparisons = new ArrayList<>();
        Map<Long, Double> storeCostMap = new HashMap<>();
        Map<Long, Integer> storeItemCountMap = new HashMap<>();
        Map<Long, String> storeNameMap = new HashMap<>();

        for (Store store : storeRepository.findAll()) {
            storeNameMap.put(store.getId(), store.getStoreName());
        }

        for (ShoppingListItem item : list.getItems()) {
            Product p = item.getProduct();
            int qty = item.getQuantity();

            List<Price> prices = priceRepository.findByProductIdOrderByPriceAsc(p.getId());
            if (!prices.isEmpty()) {
                Price cheapest = prices.get(0);
                Price highest = prices.get(prices.size() - 1);

                double itemMinTotal = cheapest.getPrice() * qty;
                cheapestTotal += itemMinTotal;
                highestTotalSum += highest.getPrice() * qty;

                itemComparisons.add(new ShoppingListComparisonDTO.ItemComparison(
                        p.getId(),
                        p.getName(),
                        p.getImageUrl(),
                        qty,
                        cheapest.getStore().getStoreName(),
                        cheapest.getPrice(),
                        itemMinTotal
                ));

                for (Price priceEntry : prices) {
                    Long storeId = priceEntry.getStore().getId();
                    storeCostMap.put(storeId, storeCostMap.getOrDefault(storeId, 0.0) + (priceEntry.getPrice() * qty));
                    storeItemCountMap.put(storeId, storeItemCountMap.getOrDefault(storeId, 0) + 1);
                }
            } else {
                itemComparisons.add(new ShoppingListComparisonDTO.ItemComparison(
                        p.getId(),
                        p.getName(),
                        p.getImageUrl(),
                        qty,
                        "N/A",
                        0.0,
                        0.0
                ));
            }
        }

        List<ShoppingListComparisonDTO.StoreTotalSummary> storeTotals = new ArrayList<>();
        for (Map.Entry<Long, Double> entry : storeCostMap.entrySet()) {
            storeTotals.add(new ShoppingListComparisonDTO.StoreTotalSummary(
                    entry.getKey(),
                    storeNameMap.get(entry.getKey()),
                    Math.round(entry.getValue() * 100.0) / 100.0,
                    storeItemCountMap.get(entry.getKey())
            ));
        }

        storeTotals.sort(Comparator.comparingDouble(ShoppingListComparisonDTO.StoreTotalSummary::getTotalCost));

        dto.setCheapestTotal(Math.round(cheapestTotal * 100.0) / 100.0);
        dto.setPotentialSavings(Math.round(Math.max(0.0, highestTotalSum - cheapestTotal) * 100.0) / 100.0);
        dto.setItems(itemComparisons);
        dto.setStoreTotals(storeTotals);

        return dto;
    }
}
