package com.grocerycompare.backend.controller;

import com.grocerycompare.backend.DTO.ShoppingListComparisonDTO;
import com.grocerycompare.backend.entity.ShoppingList;
import com.grocerycompare.backend.service.ShoppingListService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lists")
public class ShoppingListController {

    private final ShoppingListService shoppingListService;

    public ShoppingListController(ShoppingListService shoppingListService) {
        this.shoppingListService = shoppingListService;
    }

    @GetMapping
    public ResponseEntity<List<ShoppingList>> getUserLists(Authentication authentication) {
        return ResponseEntity.ok(shoppingListService.getUserLists(authentication.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShoppingList> getListById(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(shoppingListService.getListById(id, authentication.getName()));
    }

    @PostMapping
    public ResponseEntity<ShoppingList> createList(@RequestBody Map<String, String> payload, Authentication authentication) {
        String name = payload.get("name");
        ShoppingList list = shoppingListService.createList(authentication.getName(), name);
        return new ResponseEntity<>(list, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteList(@PathVariable Long id, Authentication authentication) {
        shoppingListService.deleteList(id, authentication.getName());
        return ResponseEntity.ok("Shopping list deleted successfully!");
    }

    @PostMapping("/{listId}/items")
    public ResponseEntity<ShoppingList> addItemToList(
            @PathVariable Long listId,
            @RequestBody Map<String, Object> payload,
            Authentication authentication) {

        Long productId = Long.valueOf(payload.get("productId").toString());
        Integer quantity = payload.containsKey("quantity") ? Integer.valueOf(payload.get("quantity").toString()) : 1;

        ShoppingList updatedList = shoppingListService.addItemToList(listId, authentication.getName(), productId, quantity);
        return ResponseEntity.ok(updatedList);
    }

    @DeleteMapping("/{listId}/items/{itemId}")
    public ResponseEntity<ShoppingList> removeItemFromList(
            @PathVariable Long listId,
            @PathVariable Long itemId,
            Authentication authentication) {

        ShoppingList updatedList = shoppingListService.removeItemFromList(listId, authentication.getName(), itemId);
        return ResponseEntity.ok(updatedList);
    }

    @GetMapping("/{listId}/compare")
    public ResponseEntity<ShoppingListComparisonDTO> compareListCost(@PathVariable Long listId, Authentication authentication) {
        ShoppingListComparisonDTO comparison = shoppingListService.compareListCost(listId, authentication.getName());
        return ResponseEntity.ok(comparison);
    }
}
