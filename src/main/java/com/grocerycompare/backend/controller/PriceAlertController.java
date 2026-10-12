package com.grocerycompare.backend.controller;

import com.grocerycompare.backend.entity.PriceAlert;
import com.grocerycompare.backend.service.PriceAlertService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alerts")
public class PriceAlertController {

    private final PriceAlertService priceAlertService;

    public PriceAlertController(PriceAlertService priceAlertService) {
        this.priceAlertService = priceAlertService;
    }

    @GetMapping
    public ResponseEntity<List<PriceAlert>> getUserAlerts(Authentication authentication) {
        return ResponseEntity.ok(priceAlertService.getUserAlerts(authentication.getName()));
    }

    @PostMapping
    public ResponseEntity<PriceAlert> createAlert(@RequestBody Map<String, Object> payload, Authentication authentication) {
        Long productId = Long.valueOf(payload.get("productId").toString());
        Double targetPrice = Double.valueOf(payload.get("targetPrice").toString());

        PriceAlert alert = priceAlertService.createAlert(authentication.getName(), productId, targetPrice);
        return new ResponseEntity<>(alert, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PriceAlert> updateAlert(
            @PathVariable Long id,
            @RequestBody Map<String, Object> payload,
            Authentication authentication) {

        Double targetPrice = payload.containsKey("targetPrice") ? Double.valueOf(payload.get("targetPrice").toString()) : null;
        Boolean active = payload.containsKey("active") ? Boolean.valueOf(payload.get("active").toString()) : null;

        PriceAlert alert = priceAlertService.updateAlert(id, authentication.getName(), targetPrice, active);
        return ResponseEntity.ok(alert);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAlert(@PathVariable Long id, Authentication authentication) {
        priceAlertService.deleteAlert(id, authentication.getName());
        return ResponseEntity.ok("Price alert deleted successfully");
    }
}
