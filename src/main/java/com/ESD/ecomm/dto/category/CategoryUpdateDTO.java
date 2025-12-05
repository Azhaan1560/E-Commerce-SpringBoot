package com.ESD.ecomm.dto.category;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryUpdateDTO {

    @Size(max = 100)
    private String name;

    @Size(max = 500)
    private String description;

    @Size(max = 150)
    private String slug;

    private Boolean isActive;

    private Long parentCategoryId;
}
