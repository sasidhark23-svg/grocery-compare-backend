package com.grocerycompare.backend.service;

import com.grocerycompare.backend.entity.Store;
import com.grocerycompare.backend.exception.StoreNotFoundException;
import com.grocerycompare.backend.repository.PriceRepository;
import com.grocerycompare.backend.repository.StoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StoreService {

    private final StoreRepository storeRepository;
    private final PriceRepository priceRepository;

    public StoreService(StoreRepository storeRepository, PriceRepository priceRepository) {
        this.storeRepository = storeRepository;
        this.priceRepository = priceRepository;
    }

    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    public Store getStoreById(Long id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new StoreNotFoundException(id));
    }

    @Transactional
    public Store addStore(Store store) {
        validateRating(store.getRating());
        return storeRepository.save(store);
    }

    @Transactional
    public Store updateStore(Long id, Store newStore) {
        Store store = getStoreById(id);
        validateRating(newStore.getRating());

        store.setStoreName(newStore.getStoreName());
        store.setLocation(newStore.getLocation());
        store.setWebsite(newStore.getWebsite());
        store.setRating(newStore.getRating());
        if (newStore.getStoreLogo() != null) store.setStoreLogo(newStore.getStoreLogo());

        return storeRepository.save(store);
    }

    @Transactional
    public void deleteStore(Long id) {
        if (!storeRepository.existsById(id)) {
            throw new StoreNotFoundException(id);
        }
        priceRepository.deleteByStoreId(id);
        storeRepository.deleteById(id);
    }

    private void validateRating(Double rating) {
        if (rating != null && (rating < 0.0 || rating > 5.0)) {
            throw new IllegalArgumentException("Store rating must be between 0.0 and 5.0");
        }
    }
}
