package com.grocerycompare.backend.controller;

import com.grocerycompare.backend.entity.Favorite;
import com.grocerycompare.backend.service.FavoriteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping
    public ResponseEntity<List<Favorite>> getUserFavorites(Authentication authentication) {
        return ResponseEntity.ok(favoriteService.getUserFavorites(authentication.getName()));
    }

    @PostMapping("/products/{productId}")
    public ResponseEntity<Favorite> addFavoriteProduct(@PathVariable Long productId, Authentication authentication) {
        Favorite favorite = favoriteService.addFavoriteProduct(authentication.getName(), productId);
        return new ResponseEntity<>(favorite, HttpStatus.CREATED);
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<String> removeFavoriteProduct(@PathVariable Long productId, Authentication authentication) {
        favoriteService.removeFavoriteProduct(authentication.getName(), productId);
        return ResponseEntity.ok("Product removed from favorites");
    }

    @PostMapping("/stores/{storeId}")
    public ResponseEntity<Favorite> addFavoriteStore(@PathVariable Long storeId, Authentication authentication) {
        Favorite favorite = favoriteService.addFavoriteStore(authentication.getName(), storeId);
        return new ResponseEntity<>(favorite, HttpStatus.CREATED);
    }

    @DeleteMapping("/stores/{storeId}")
    public ResponseEntity<String> removeFavoriteStore(@PathVariable Long storeId, Authentication authentication) {
        favoriteService.removeFavoriteStore(authentication.getName(), storeId);
        return ResponseEntity.ok("Store removed from favorites");
    }
}
