package com.grocerycompare.backend.service;

import com.grocerycompare.backend.entity.Favorite;
import com.grocerycompare.backend.entity.Product;
import com.grocerycompare.backend.entity.Store;
import com.grocerycompare.backend.entity.User;
import com.grocerycompare.backend.exception.ProductNotFoundException;
import com.grocerycompare.backend.exception.StoreNotFoundException;
import com.grocerycompare.backend.exception.UserNotFoundException;
import com.grocerycompare.backend.repository.FavoriteRepository;
import com.grocerycompare.backend.repository.ProductRepository;
import com.grocerycompare.backend.repository.StoreRepository;
import com.grocerycompare.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;

    public FavoriteService(
            FavoriteRepository favoriteRepository,
            UserRepository userRepository,
            ProductRepository productRepository,
            StoreRepository storeRepository) {

        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.storeRepository = storeRepository;
    }

    public List<Favorite> getUserFavorites(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userEmail));
        return favoriteRepository.findByUserId(user.getId());
    }

    @Transactional
    public Favorite addFavoriteProduct(String userEmail, Long productId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userEmail));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        return favoriteRepository.findByUserIdAndProductId(user.getId(), productId)
                .orElseGet(() -> favoriteRepository.save(new Favorite(user, product)));
    }

    @Transactional
    public void removeFavoriteProduct(String userEmail, Long productId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userEmail));
        favoriteRepository.deleteByUserIdAndProductId(user.getId(), productId);
    }

    @Transactional
    public Favorite addFavoriteStore(String userEmail, Long storeId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userEmail));
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException(storeId));

        return favoriteRepository.findByUserIdAndStoreId(user.getId(), storeId)
                .orElseGet(() -> favoriteRepository.save(new Favorite(user, store)));
    }

    @Transactional
    public void removeFavoriteStore(String userEmail, Long storeId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userEmail));
        favoriteRepository.deleteByUserIdAndStoreId(user.getId(), storeId);
    }
}
