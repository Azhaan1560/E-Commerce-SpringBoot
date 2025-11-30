package com.ESD.ecomm.repositories;
import com.ESD.ecomm.entities.Cart_Items;
import com.ESD.ecomm.entities.Cart;
import com.ESD.ecomm.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<Cart_Items, Long> {

    List<Cart_Items> findByCart(Cart cart);

    Optional<Cart_Items> findByCartAndProduct(Cart cart, Product product);

    void deleteByCart(Cart cart);

    boolean existsByCartAndProduct(Cart cart, Product product);
}

