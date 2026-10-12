package com.grocerycompare.backend.DTO;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListComparisonDTO {

    private Long listId;
    private String listName;
    private Integer totalItems;
    private Double cheapestTotal;
    private Double potentialSavings;
    private List<ItemComparison> items = new ArrayList<>();
    private List<StoreTotalSummary> storeTotals = new ArrayList<>();

    public static class ItemComparison {
        private Long productId;
        private String productName;
        private String imageUrl;
        private Integer quantity;
        private String cheapestStore;
        private Double cheapestPrice;
        private Double subtotal;

        public ItemComparison() {}

        public ItemComparison(Long productId, String productName, String imageUrl, Integer quantity, String cheapestStore, Double cheapestPrice, Double subtotal) {
            this.productId = productId;
            this.productName = productName;
            this.imageUrl = imageUrl;
            this.quantity = quantity;
            this.cheapestStore = cheapestStore;
            this.cheapestPrice = cheapestPrice;
            this.subtotal = subtotal;
        }

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }
        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        public String getCheapestStore() { return cheapestStore; }
        public void setCheapestStore(String cheapestStore) { this.cheapestStore = cheapestStore; }
        public Double getCheapestPrice() { return cheapestPrice; }
        public void setCheapestPrice(Double cheapestPrice) { this.cheapestPrice = cheapestPrice; }
        public Double getSubtotal() { return subtotal; }
        public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }
    }

    public static class StoreTotalSummary {
        private Long storeId;
        private String storeName;
        private Double totalCost;
        private Integer availableItems;

        public StoreTotalSummary() {}

        public StoreTotalSummary(Long storeId, String storeName, Double totalCost, Integer availableItems) {
            this.storeId = storeId;
            this.storeName = storeName;
            this.totalCost = totalCost;
            this.availableItems = availableItems;
        }

        public Long getStoreId() { return storeId; }
        public void setStoreId(Long storeId) { this.storeId = storeId; }
        public String getStoreName() { return storeName; }
        public void setStoreName(String storeName) { this.storeName = storeName; }
        public Double getTotalCost() { return totalCost; }
        public void setTotalCost(Double totalCost) { this.totalCost = totalCost; }
        public Integer getAvailableItems() { return availableItems; }
        public void setAvailableItems(Integer availableItems) { this.availableItems = availableItems; }
    }

    public ShoppingListComparisonDTO() {}

    public Long getListId() { return listId; }
    public void setListId(Long listId) { this.listId = listId; }
    public String getListName() { return listName; }
    public void setListName(String listName) { this.listName = listName; }
    public Integer getTotalItems() { return totalItems; }
    public void setTotalItems(Integer totalItems) { this.totalItems = totalItems; }
    public Double getCheapestTotal() { return cheapestTotal; }
    public void setCheapestTotal(Double cheapestTotal) { this.cheapestTotal = cheapestTotal; }
    public Double getPotentialSavings() { return potentialSavings; }
    public void setPotentialSavings(Double potentialSavings) { this.potentialSavings = potentialSavings; }
    public List<ItemComparison> getItems() { return items; }
    public void setItems(List<ItemComparison> items) { this.items = items; }
    public List<StoreTotalSummary> getStoreTotals() { return storeTotals; }
    public void setStoreTotals(List<StoreTotalSummary> storeTotals) { this.storeTotals = storeTotals; }
}
