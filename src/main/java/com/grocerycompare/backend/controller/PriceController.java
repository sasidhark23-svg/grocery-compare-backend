package com.grocerycompare.backend.controller;

import com.grocerycompare.backend.DTO.PriceComparisonResponse;
import com.grocerycompare.backend.entity.Price;
import com.grocerycompare.backend.entity.PriceHistory;
import com.grocerycompare.backend.entity.StockStatus;
import com.grocerycompare.backend.service.PriceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prices")
public class PriceController {

    private final PriceService priceService;

    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    @GetMapping
    public List<Price> getAllPrices() {
        return priceService.getAllPrices();
    }

    @PostMapping
    public ResponseEntity<Price> addPrice(
            @RequestParam Long productId,
            @RequestParam Long storeId,
            @RequestParam Double price,
            @RequestParam(required = false, defaultValue = "0.0") Double discount,
            @RequestParam(required = false, defaultValue = "IN_STOCK") StockStatus stockStatus) {

        Price savedPrice = priceService.addOrUpdatePrice(productId, storeId, price, discount, stockStatus);
        return new ResponseEntity<>(savedPrice, HttpStatus.CREATED);
    }

    @GetMapping("/cheapest/{productId}")
    public ResponseEntity<Price> getCheapestPrice(@PathVariable Long productId) {
        return ResponseEntity.ok(priceService.getCheapestPrice(productId));
    }

    @GetMapping("/compare/{productId}")
    public ResponseEntity<List<Price>> comparePrices(@PathVariable Long productId) {
        return ResponseEntity.ok(priceService.comparePrices(productId));
    }

    @GetMapping("/summary/{productId}")
    public ResponseEntity<PriceComparisonResponse> getPriceComparison(@PathVariable Long productId) {
        return ResponseEntity.ok(priceService.getPriceComparisonSummary(productId));
    }

    @GetMapping("/history/{productId}")
    public ResponseEntity<List<PriceHistory>> getPriceHistory(
            @PathVariable Long productId,
            @RequestParam(required = false) Long storeId) {

        List<PriceHistory> history = priceService.getPriceHistory(productId, storeId);
        return ResponseEntity.ok(history);
    }
}