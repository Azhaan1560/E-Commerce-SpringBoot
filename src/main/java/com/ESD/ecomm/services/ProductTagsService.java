package com.ESD.ecomm.services;
import com.ESD.ecomm.entities.Product_Tags;
import com.ESD.ecomm.repositories.ProductTagsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductTagsService {

    private final ProductTagsRepository productTagsRepository;

    @Autowired
    public ProductTagsService(ProductTagsRepository productTagsRepository) {
        this.productTagsRepository = productTagsRepository;
    }

    public List<Product_Tags> getAllTags() {
        return productTagsRepository.findAll();
    }

    public Optional<Product_Tags> getTagById(Long id) {
        return productTagsRepository.findById(id);
    }

    public Optional<Product_Tags> getTagByName(String name) {
        return productTagsRepository.findByName(name);
    }

    public Optional<Product_Tags> getTagBySlug(String slug) {
        return productTagsRepository.findBySlug(slug);
    }

    public Product_Tags createTag(Product_Tags tag) {
        if (productTagsRepository.existsByName(tag.getName()) || productTagsRepository.existsBySlug(tag.getSlug())) {
            throw new IllegalArgumentException("Tag with this name or slug already exists");
        }
        return productTagsRepository.save(tag);
    }

    public Product_Tags updateTag(Product_Tags tag) {
        if (!productTagsRepository.existsById(tag.getId())) {
            throw new IllegalArgumentException("Tag not found");
        }
        return productTagsRepository.save(tag);
    }

    public void deleteTag(Long id) {
        if (productTagsRepository.existsById(id)) {
            productTagsRepository.deleteById(id);
        }
    }
}
