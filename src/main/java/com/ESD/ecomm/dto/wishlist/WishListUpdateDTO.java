package com.ESD.ecomm.dto.wishlist;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishListUpdateDTO {

    @NotNull(message = "User ID is required")
    private Long userId;

    private List<Long> productIds; // Full list of products in wishlist
}
