package com.grocerycompare.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 180)
    @Column(nullable = false, length = 180)
    private String name;

    @Size(max = 120)
    private String brand;
    @Size(max = 80)
    private String category;
    @Size(max = 80)
    private String unit;

    @Column(columnDefinition = "TEXT")
    @Size(max = 4000)
    private String description;

    @Size(max = 1000)
    private String imageUrl;
    @Size(max = 80)
    private String barcode;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Product() {
    }

    public Product(String name, String brand, String category, String description, String imageUrl, String barcode) {
        this.name = name;
        this.brand = brand;
        this.category = category;
        this.description = description;
        this.imageUrl = imageUrl;
        this.barcode = barcode;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
