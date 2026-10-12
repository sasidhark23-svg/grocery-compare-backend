package com.grocerycompare.backend.DTO;

import com.grocerycompare.backend.entity.StockStatus;

public class DealDTO {
    private Long priceId;
    private Long storeId;
    private String storeName;
    private String storeLogo;
    private Double storeRating;
    private Double price;
    private Double discount;
    private StockStatus stockStatus;

    public DealDTO() {
    }

    public DealDTO(Long priceId, Long storeId, String storeName, String storeLogo, Double storeRating, Double price, Double discount, StockStatus stockStatus) {
        this.priceId = priceId;
        this.storeId = storeId;
        this.storeName = storeName;
        this.storeLogo = storeLogo;
        this.storeRating = storeRating;
        this.price = price;
        this.discount = discount;
        this.stockStatus = stockStatus;
    }

    public Long getPriceId() {
        return priceId;
    }

    public void setPriceId(Long priceId) {
        this.priceId = priceId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreLogo() {
        return storeLogo;
    }

    public void setStoreLogo(String storeLogo) {
        this.storeLogo = storeLogo;
    }

    public Double getStoreRating() {
        return storeRating;
    }

    public void setStoreRating(Double storeRating) {
        this.storeRating = storeRating;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public StockStatus getStockStatus() {
        return stockStatus;
    }

    public void setStockStatus(StockStatus stockStatus) {
        this.stockStatus = stockStatus;
    }
}
