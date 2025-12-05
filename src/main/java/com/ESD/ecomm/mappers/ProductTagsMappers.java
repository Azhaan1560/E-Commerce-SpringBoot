package com.ESD.ecomm.mappers;
import com.ESD.ecomm.dto.product_tags.*;
import com.ESD.ecomm.entities.Product_Tags;
import java.time.format.DateTimeFormatter;

public class ProductTagsMappers {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // ProductTagRequestDTO -> Product_Tags entity
    public static Product_Tags toProductTag(ProductTagRequestDTO dto) {
        if (dto == null) return null;
        return Product_Tags.builder()
                .name(dto.getName())
                .slug(dto.getSlug())
                .build();
    }

    // ProductTagUpdateDTO -> update existing Product_Tags entity
    public static void updateProductTagFromDTO(ProductTagUpdateDTO dto, Product_Tags tag) {
        if (dto == null || tag == null) return;

        if (dto.getName() != null) tag.setName(dto.getName());
        if (dto.getSlug() != null) tag.setSlug(dto.getSlug());
    }

    // Product_Tags entity -> ProductTagResponseDTO
    public static ProductTagResponseDTO toProductTagResponseDTO(Product_Tags tag) {
        if (tag == null) return null;
        return ProductTagResponseDTO.builder()
                .id(tag.getId())
                .name(tag.getName())
                .slug(tag.getSlug())
                .createdAt(tag.getCreatedAt())
                .build();
    }
}
