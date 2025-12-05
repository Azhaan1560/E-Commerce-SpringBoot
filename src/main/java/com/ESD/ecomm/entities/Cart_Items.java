package com.ESD.ecomm.entities;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.validation.constraints.Min;


@Entity
@Table(name="Cart_Item",
       uniqueConstraints = {
        @UniqueConstraint(name="unique_cart_product",columnNames = {"cart_id","product_id"})
       },
        indexes = {
            @Index(name="ci_idx_cart",columnList = "cart_id"),
            @Index(name="ci_idx_product",columnList = "product_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart_Items {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name="cart_id",nullable = false)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name="product_id",nullable = false)
    private Product product;

    @Column(nullable = false)
    @Min(1)
    private Integer quantity=1;

    @Column(nullable = false,precision=10,scale=2)
    private BigDecimal price;

    @Column(nullable = false,precision=10,scale=2)
    private BigDecimal subtotal;

    @Column(name="added_at")
    private LocalDateTime addedAt;

    @PrePersist
    protected void onAdd() {
        this.addedAt = LocalDateTime.now();
    }

    public void calculateSubtotal() {
        if (this.price != null && this.quantity != null) {
            this.subtotal = this.price.multiply(BigDecimal.valueOf(this.quantity));
        }
    }
}
