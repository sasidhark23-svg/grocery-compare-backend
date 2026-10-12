package com.grocerycompare.backend.service;

import com.grocerycompare.backend.entity.Product;
import com.grocerycompare.backend.exception.ProductNotFoundException;
import com.grocerycompare.backend.repository.PriceHistoryRepository;
import com.grocerycompare.backend.repository.PriceRepository;
import com.grocerycompare.backend.repository.ProductRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final PriceRepository priceRepository;

    public ProductService(ProductRepository productRepository, PriceRepository priceRepository) {
        this.productRepository = productRepository;
        this.priceRepository = priceRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> searchProducts(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    @Cacheable(value = "products", key = "#query + '-' + #category + '-' + #brand + '-' + #page + '-' + #size + '-' + #sortBy")
    public Page<Product> searchProductsPaginated(String query, String category, String brand, int page, int size, String sortBy) {
        page = Math.max(page, 0);
        size = Math.min(Math.max(size, 1), 100);
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        if ("priceAsc".equalsIgnoreCase(sortBy) || "priceDesc".equalsIgnoreCase(sortBy)) {
            sort = Sort.by(Sort.Direction.ASC, "id");
        } else if ("newest".equalsIgnoreCase(sortBy)) {
            sort = Sort.by(Sort.Direction.DESC, "createdAt");
        }
        Pageable pageable = PageRequest.of(page, size, sort);
        return productRepository.searchProducts(query, category, brand, pageable);
    }

    @Transactional
    @CacheEvict(value = {"products", "prices"}, allEntries = true)
    public Product updateProduct(Long id, Product newProduct) {
        Product product = getProductById(id);

        product.setName(newProduct.getName());
        product.setBrand(newProduct.getBrand());
        product.setCategory(newProduct.getCategory());
        if (newProduct.getUnit() != null) product.setUnit(newProduct.getUnit());
        if (newProduct.getDescription() != null) product.setDescription(newProduct.getDescription());
        if (newProduct.getImageUrl() != null) product.setImageUrl(newProduct.getImageUrl());
        if (newProduct.getBarcode() != null) product.setBarcode(newProduct.getBarcode());

        return productRepository.save(product);
    }

    @Transactional
    @CacheEvict(value = {"products", "prices"}, allEntries = true)
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        priceRepository.deleteByProductId(id);
        productRepository.deleteById(id);
    }
}
