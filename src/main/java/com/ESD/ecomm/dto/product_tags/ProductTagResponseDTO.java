package com.ESD.ecomm.dto.product_tags;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductTagResponseDTO {

    private Long id;
    private String name;
    private String slug;
    private LocalDateTime createdAt;
}
