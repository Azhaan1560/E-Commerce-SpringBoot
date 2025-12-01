package com.ESD.ecomm.services;
import com.ESD.ecomm.entities.Product;
import com.ESD.ecomm.entities.Category;
import com.ESD.ecomm.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getActiveProducts() {
        return productRepository.findByIsActiveTrue();
    }

    public List<Product> getFeaturedProducts() {
        return productRepository.findByIsFeaturedTrue();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Optional<Product> getProductBySku(String sku) {
        return productRepository.findBySku(sku);
    }

    public List<Product> getProductsByCategory(Category category) {
        return productRepository.findByCategory(category);
    }

    public List<Product> getProductsByPriceRange(Double minPrice, Double maxPrice) {
        return productRepository.findByProdPriceBetween(minPrice, maxPrice);
    }

    public List<Product> searchProducts(String keyword) {
        return productRepository.searchProducts(keyword);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
