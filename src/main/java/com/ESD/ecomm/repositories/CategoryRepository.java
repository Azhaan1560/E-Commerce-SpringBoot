package com.ESD.ecomm.repositories;
import com.ESD.ecomm.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String slug);

    List<Category> findByIsActiveTrue();

    List<Category> findByIsActiveFalse();

    boolean existsByName(String name);
    List<Category> findByParentCategory(Category parentCategory);

}
