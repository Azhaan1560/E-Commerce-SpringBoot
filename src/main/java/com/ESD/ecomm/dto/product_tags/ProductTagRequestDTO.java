package com.ESD.ecomm.dto.product_tags;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductTagRequestDTO {

    @NotBlank(message = "Tag name is required")
    @Size(max = 50)
    private String name;

    @NotBlank(message = "Tag slug is required")
    @Size(max = 50)
    private String slug;
}
