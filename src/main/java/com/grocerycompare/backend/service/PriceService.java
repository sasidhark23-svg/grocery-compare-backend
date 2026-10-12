package com.grocerycompare.backend.service;

import com.grocerycompare.backend.DTO.DealDTO;
import com.grocerycompare.backend.DTO.PriceComparisonResponse;
import com.grocerycompare.backend.entity.*;
import com.grocerycompare.backend.exception.ProductNotFoundException;
import com.grocerycompare.backend.exception.ResourceNotFoundException;
import com.grocerycompare.backend.exception.StoreNotFoundException;
import com.grocerycompare.backend.repository.PriceHistoryRepository;
import com.grocerycompare.backend.repository.PriceRepository;
import com.grocerycompare.backend.repository.ProductRepository;
import com.grocerycompare.backend.repository.StoreRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PriceService {

    private final PriceRepository priceRepository;
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final PriceHistoryRepository priceHistoryRepository;

    public PriceService(
            PriceRepository priceRepository,
            ProductRepository productRepository,
            StoreRepository storeRepository,
            PriceHistoryRepository priceHistoryRepository) {

        this.priceRepository = priceRepository;
        this.productRepository = productRepository;
        this.storeRepository = storeRepository;
        this.priceHistoryRepository = priceHistoryRepository;
    }

    public List<Price> getAllPrices() {
        return priceRepository.findAll();
    }

    @Transactional
    @CacheEvict(value = "priceComparisons", allEntries = true)
    public Price addOrUpdatePrice(Long productId, Long storeId, Double price, Double discount, StockStatus stockStatus) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException(storeId));

        Optional<Price> existingOpt = priceRepository.findByProductIdAndStoreId(productId, storeId);
        Price priceEntity;

        if (existingOpt.isPresent()) {
            priceEntity = existingOpt.get();
            priceEntity.setPrice(price);
            if (discount != null) priceEntity.setDiscount(discount);
            if (stockStatus != null) priceEntity.setStockStatus(stockStatus);
            priceEntity.setLastUpdated(LocalDateTime.now());
        } else {
            priceEntity = new Price(product, store, price, discount, stockStatus);
        }

        Price savedPrice = priceRepository.save(priceEntity);

        // Record history entry
        priceHistoryRepository.save(new PriceHistory(product, store, price));

        return savedPrice;
    }

    public Price getCheapestPrice(Long productId) {
        List<Price> prices = priceRepository.findByProductId(productId);
        if (prices.isEmpty()) {
            throw new ResourceNotFoundException("No prices found for product ID: " + productId);
        }
        return prices.stream()
                .min((p1, p2) -> Double.compare(p1.getPrice(), p2.getPrice()))
                .orElseThrow(() -> new ResourceNotFoundException("No prices found for product ID: " + productId));
    }

    public List<Price> comparePrices(Long productId) {
        List<Price> prices = priceRepository.findByProductIdOrderByPriceAsc(productId);
        if (prices.isEmpty()) {
            throw new ResourceNotFoundException("No prices found for product ID: " + productId);
        }
        return prices;
    }

    @Cacheable(value = "priceComparisons", key = "#productId")
    public PriceComparisonResponse getPriceComparisonSummary(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        List<Price> prices = priceRepository.findByProductIdOrderByPriceAsc(productId);
        if (prices.isEmpty()) {
            return new PriceComparisonResponse(
                    product.getId(),
                    product.getName(),
                    product.getImageUrl(),
                    "N/A",
                    0.0,
                    0.0,
                    0.0,
                    0,
                    new ArrayList<>()
            );
        }

        Price cheapest = prices.get(0);
        Price highest = prices.get(prices.size() - 1);
        Double difference = Math.max(0.0, highest.getPrice() - cheapest.getPrice());

        List<DealDTO> deals = new ArrayList<>();
        for (Price p : prices) {
            deals.add(new DealDTO(
                    p.getId(),
                    p.getStore().getId(),
                    p.getStore().getStoreName(),
                    p.getStore().getStoreLogo(),
                    p.getStore().getRating(),
                    p.getPrice(),
                    p.getDiscount(),
                    p.getStockStatus()
            ));
        }

        return new PriceComparisonResponse(
                product.getId(),
                product.getName(),
                product.getImageUrl(),
                cheapest.getStore().getStoreName(),
                cheapest.getPrice(),
                highest.getPrice(),
                difference,
                prices.size(),
                deals
        );
    }

    public List<PriceHistory> getPriceHistory(Long productId, Long storeId) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException(productId);
        }

        if (storeId != null) {
            return priceHistoryRepository.findByProductIdAndStoreIdOrderByRecordedAtAsc(productId, storeId);
        } else {
            return priceHistoryRepository.findByProductIdOrderByRecordedAtAsc(productId);
        }
    }
}
