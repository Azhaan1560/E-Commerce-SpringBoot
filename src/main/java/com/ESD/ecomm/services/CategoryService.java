package com.ESD.ecomm.services;
import com.ESD.ecomm.entities.Category;
import com.ESD.ecomm.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Category> getActiveCategories() {
        return categoryRepository.findByIsActiveTrue();
    }

    public List<Category> getInactiveCategories() {
        return categoryRepository.findByIsActiveFalse();
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public Optional<Category> getCategoryBySlug(String slug) {
        return categoryRepository.findByName(slug);
    }

    public List<Category> getSubcategories(Category parentCategory) {
        return categoryRepository.findByParentCategory(parentCategory);
    }

    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    public boolean existsByName(String name) {
        return categoryRepository.existsByName(name);
    }
}

