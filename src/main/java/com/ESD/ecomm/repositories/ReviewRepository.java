package com.ESD.ecomm.repositories;
import com.ESD.ecomm.entities.Review;
import com.ESD.ecomm.entities.Product;
import com.ESD.ecomm.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByProduct(Product product);

    List<Review> findByUser(User user);

    List<Review> findByProductAndIsApprovedTrue(Product product);

    boolean existsByUserAndProduct(User user, Product product);
}
