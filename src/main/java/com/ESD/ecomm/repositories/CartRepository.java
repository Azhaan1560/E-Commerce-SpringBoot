package com.ESD.ecomm.repositories;
import com.ESD.ecomm.entities.Cart;
import com.ESD.ecomm.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser(User user);

    boolean existsByUser(User user);
}

