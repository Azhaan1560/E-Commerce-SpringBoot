package com.ESD.ecomm.dto.product_tags;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductTagUpdateDTO {

    @NotNull(message = "Tag ID is required")
    private Long id;

    @Size(max = 50)
    private String name;

    @Size(max = 50)
    private String slug;
}

