package com.ESD.ecomm.repositories;
import com.ESD.ecomm.entities.WishList;
import com.ESD.ecomm.entities.User;
import com.ESD.ecomm.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface WishListRepository extends JpaRepository<WishList, Long> {

    Optional<WishList> findByUser(User user);

    boolean existsByUserAndProductsContaining(User user, Product product);
}

