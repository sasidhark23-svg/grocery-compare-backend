package com.grocerycompare.backend.DTO;

import java.util.ArrayList;
import java.util.List;

public class PriceComparisonResponse {

    private Long productId;
    private String product;
    private String imageUrl;
    private String cheapestStore;
    private Double cheapestPrice;
    private Double highestPrice;
    private Double priceDifference;
    private Integer totalStores;
    private List<DealDTO> deals = new ArrayList<>();

    public PriceComparisonResponse() {
    }

    public PriceComparisonResponse(
            String product,
            String cheapestStore,
            Double cheapestPrice,
            Double highestPrice,
            Double priceDifference,
            Integer totalStores) {

        this.product = product;
        this.cheapestStore = cheapestStore;
        this.cheapestPrice = cheapestPrice;
        this.highestPrice = highestPrice;
        this.priceDifference = priceDifference;
        this.totalStores = totalStores;
    }

    public PriceComparisonResponse(
            Long productId,
            String product,
            String imageUrl,
            String cheapestStore,
            Double cheapestPrice,
            Double highestPrice,
            Double priceDifference,
            Integer totalStores,
            List<DealDTO> deals) {

        this.productId = productId;
        this.product = product;
        this.imageUrl = imageUrl;
        this.cheapestStore = cheapestStore;
        this.cheapestPrice = cheapestPrice;
        this.highestPrice = highestPrice;
        this.priceDifference = priceDifference;
        this.totalStores = totalStores;
        this.deals = deals != null ? deals : new ArrayList<>();
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCheapestStore() {
        return cheapestStore;
    }

    public void setCheapestStore(String cheapestStore) {
        this.cheapestStore = cheapestStore;
    }

    public Double getCheapestPrice() {
        return cheapestPrice;
    }

    public void setCheapestPrice(Double cheapestPrice) {
        this.cheapestPrice = cheapestPrice;
    }

    public Double getHighestPrice() {
        return highestPrice;
    }

    public void setHighestPrice(Double highestPrice) {
        this.highestPrice = highestPrice;
    }

    public Double getPriceDifference() {
        return priceDifference;
    }

    public void setPriceDifference(Double priceDifference) {
        this.priceDifference = priceDifference;
    }

    public Integer getTotalStores() {
        return totalStores;
    }

    public void setTotalStores(Integer totalStores) {
        this.totalStores = totalStores;
    }

    public List<DealDTO> getDeals() {
        return deals;
    }

    public void setDeals(List<DealDTO> deals) {
        this.deals = deals;
    }
}