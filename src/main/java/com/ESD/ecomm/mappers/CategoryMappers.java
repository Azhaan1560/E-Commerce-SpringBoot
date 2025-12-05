package com.ESD.ecomm.mappers;
import com.ESD.ecomm.dto.category.*;
import com.ESD.ecomm.entities.Category;
import java.time.format.DateTimeFormatter;

public class CategoryMappers {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // CategoryRequestDTO -> Category entity
    public static Category toCategory(CategoryRequestDTO dto) {
        if (dto == null) return null;
        Category category = Category.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .slug(dto.getSlug())
                .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
                .build();

        if (dto.getParentCategoryId() != null) {
            Category parent = new Category();
            parent.setId(dto.getParentCategoryId());
            category.setParentCategory(parent);
        }

        return category;
    }

    // CategoryUpdateDTO -> update existing Category entity
    public static void updateCategoryFromDTO(CategoryUpdateDTO dto, Category category) {
        if (dto == null || category == null) return;

        if (dto.getName() != null) category.setName(dto.getName());
        if (dto.getDescription() != null) category.setDescription(dto.getDescription());
        if (dto.getSlug() != null) category.setSlug(dto.getSlug());
        if (dto.getIsActive() != null) category.setIsActive(dto.getIsActive());
        if (dto.getParentCategoryId() != null) {
            Category parent = new Category();
            parent.setId(dto.getParentCategoryId());
            category.setParentCategory(parent);
        }
    }

    // Category entity -> CategoryResponseDTO
    public static CategoryResponseDTO toCategoryResponseDTO(Category category) {
        if (category == null) return null;
        CategoryResponseDTO dto = CategoryResponseDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .slug(category.getSlug())
                .isActive(category.getIsActive())
                .createdAt(category.getCreatedAt() != null ? category.getCreatedAt().format(formatter) : null)
                .updatedAt(category.getUpdatedAt() != null ? category.getUpdatedAt().format(formatter) : null)
                .build();

        if (category.getParentCategory() != null) {
            dto.setParentCategoryId(category.getParentCategory().getId());
            dto.setParentCategoryName(category.getParentCategory().getName());
        }

        return dto;
    }
}
