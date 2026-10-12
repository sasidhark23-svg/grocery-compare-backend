package com.grocerycompare.backend.service;

import com.grocerycompare.backend.entity.Price;
import com.grocerycompare.backend.entity.PriceAlert;
import com.grocerycompare.backend.entity.Product;
import com.grocerycompare.backend.entity.User;
import com.grocerycompare.backend.exception.ProductNotFoundException;
import com.grocerycompare.backend.exception.ResourceNotFoundException;
import com.grocerycompare.backend.exception.UserNotFoundException;
import com.grocerycompare.backend.repository.PriceAlertRepository;
import com.grocerycompare.backend.repository.PriceRepository;
import com.grocerycompare.backend.repository.ProductRepository;
import com.grocerycompare.backend.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PriceAlertService {

    private final PriceAlertRepository priceAlertRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PriceRepository priceRepository;

    public PriceAlertService(
            PriceAlertRepository priceAlertRepository,
            UserRepository userRepository,
            ProductRepository productRepository,
            PriceRepository priceRepository) {

        this.priceAlertRepository = priceAlertRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.priceRepository = priceRepository;
    }

    public List<PriceAlert> getUserAlerts(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userEmail));
        return priceAlertRepository.findByUserId(user.getId());
    }

    @Transactional
    public PriceAlert createAlert(String userEmail, Long productId, Double targetPrice) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userEmail));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        PriceAlert alert = new PriceAlert(user, product, targetPrice);
        checkAlertCondition(alert);

        return priceAlertRepository.save(alert);
    }

    @Transactional
    public PriceAlert updateAlert(Long alertId, String userEmail, Double targetPrice, Boolean active) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userEmail));
        PriceAlert alert = priceAlertRepository.findById(alertId)
                .orElseThrow(() -> new ResourceNotFoundException("Price alert not found"));

        if (!alert.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Unauthorized access to price alert");
        }

        if (targetPrice != null) alert.setTargetPrice(targetPrice);
        if (active != null) alert.setActive(active);

        checkAlertCondition(alert);
        return priceAlertRepository.save(alert);
    }

    @Transactional
    public void deleteAlert(Long alertId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userEmail));
        PriceAlert alert = priceAlertRepository.findById(alertId)
                .orElseThrow(() -> new ResourceNotFoundException("Price alert not found"));

        if (!alert.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Unauthorized access to price alert");
        }

        priceAlertRepository.delete(alert);
    }

    @Scheduled(fixedRate = 60000) // Runs every minute
    @Transactional
    public void evaluatePriceAlerts() {
        List<PriceAlert> activeAlerts = priceAlertRepository.findByActiveTrue();
        for (PriceAlert alert : activeAlerts) {
            checkAlertCondition(alert);
            priceAlertRepository.save(alert);
        }
    }

    private void checkAlertCondition(PriceAlert alert) {
        List<Price> prices = priceRepository.findByProductIdOrderByPriceAsc(alert.getProduct().getId());
        if (!prices.isEmpty()) {
            Double cheapestPrice = prices.get(0).getPrice();
            if (cheapestPrice <= alert.getTargetPrice()) {
                alert.setTriggered(true);
            } else {
                alert.setTriggered(false);
            }
        }
    }
}
