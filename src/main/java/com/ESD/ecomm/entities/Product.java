package com.ESD.ecomm.entities;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name="Products",
        indexes = {
            @Index(name="prod_idx_sku",columnList = "sku"),
            @Index(name="prod_idx_active",columnList = "is_active"),
            @Index(name="prod_idx_featured",columnList="is_featured"),
            @Index(name="prod_idx_price",columnList ="prod_price"),
            @Index(name="prod_idx_stock",columnList = "stock_quantity")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "product_name", nullable = false, length = 200)
    private String prodName;

    @Column(name = "prod_description", nullable = false, columnDefinition = "TEXT")
    private String prodDescription;

    @Column(name = "sku", nullable = false, unique = true, length = 12)
    private String sku;

    @Column(name = "prod_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal prodPrice;

    @Column(precision = 10, scale = 2)
    private BigDecimal discountedPrice;

    @Column(name = "stock_quantity")
    private Integer stockQuantity = 0;

    @Column(name = "min_stock_level")
    private Integer minStockLevel = 10;

    @Column(length = 100)
    private String brand;

    @Column(name = "weight")
    private double weight;

    @Column(name = "dimensions", length = 50)
    private String dimensions;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "is_featured")
    private Boolean isFeatured = false;

    @Column(name = "average_rating", precision = 3, scale = 2)
    private BigDecimal averageRating = BigDecimal.ZERO;

    @Column(name = "total_reviews")
    private Integer totalReviews = 0;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToMany
    @JoinTable(
            name = "product_tag_mappings",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Product_Tags> tags = new ArrayList<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}