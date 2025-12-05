package com.ESD.ecomm.dto.wishlist;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishListResponseDTO {

    private Long id;
    private Long userId;
    private List<Long> productIds; // Only returning product IDs
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
