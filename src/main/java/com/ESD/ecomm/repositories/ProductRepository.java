package com.ESD.ecomm.repositories;
import com.ESD.ecomm.entities.Product;
import com.ESD.ecomm.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findBySku(String sku);

    List<Product> findByIsActiveTrue();

    List<Product> findByIsFeaturedTrue();

    List<Product> findByCategory(Category category);

    List<Product> findByProdPriceBetween(Double minPrice, Double maxPrice);
    @Query(
            value = "SELECT * FROM products WHERE MATCH(name, description) AGAINST(:keyword IN NATURAL LANGUAGE MODE)",
            nativeQuery = true
    )
    List<Product> searchProducts(@Param("keyword") String keyword);
}
