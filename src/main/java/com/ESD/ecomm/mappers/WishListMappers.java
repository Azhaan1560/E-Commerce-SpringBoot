package com.ESD.ecomm.mappers;

import com.ESD.ecomm.dto.wishlist.WishListResponseDTO;
import com.ESD.ecomm.entities.Product;
import com.ESD.ecomm.entities.WishList;

public class WishListMappers {

    public static WishListResponseDTO toDTO(WishList wishlist) {
        if (wishlist == null) return null;

        return WishListResponseDTO.builder()
                .id(wishlist.getId())
                .userId(wishlist.getUser().getId())
                .productIds(
                        wishlist.getProducts()
                                .stream()
                                .map(Product::getId)
                                .toList()
                )
                .createdAt(wishlist.getCreatedAt())
                .updatedAt(wishlist.getUpdatedAt())
                .build();
    }
}
