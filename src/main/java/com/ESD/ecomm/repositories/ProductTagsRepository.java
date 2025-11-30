package com.ESD.ecomm.repositories;
import com.ESD.ecomm.entities.Product_Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProductTagsRepository extends JpaRepository<Product_Tags, Long> {

    Optional<Product_Tags> findByName(String name);

    Optional<Product_Tags> findBySlug(String slug);

    boolean existsByName(String name);

    boolean existsBySlug(String slug);
}
