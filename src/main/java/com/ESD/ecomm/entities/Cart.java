package com.ESD.ecomm.entities;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Carts",
        indexes = {
            @Index(name="carts_idx_user",columnList = "user_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {

    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.REMOVE) //REMOVE IS USED CAUSE WHEN PARENT IS DELETED ONLY THEN CHILD IS DELETED
    @JoinColumn(name="user_id",nullable = false, unique = true)
    private User user;

    @OneToMany(mappedBy="cart",cascade=CascadeType.ALL,orphanRemoval= true)
    private List<Cart_Items> cartItems= new ArrayList<>();

    @Column(name="total_price",precision=10, scale=2)
    private  BigDecimal totalPrice=BigDecimal.valueOf(0.0);

    @Column(name="total_items")
    private Integer totalItems=0;

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

    public void addItem(Cart_Items item){
        cartItems.add(item);
        item.setCart(this);
        this.totalItems= cartItems.size();
        recalculateTotal();
    }

    public void removeItem(Cart_Items item){
        cartItems.remove(item);
        item.setCart(null);
        this.totalItems= cartItems.size();
        recalculateTotal();
    }

    private void recalculateTotal(){
        this.totalPrice = cartItems.stream()
                .map(Cart_Items::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
