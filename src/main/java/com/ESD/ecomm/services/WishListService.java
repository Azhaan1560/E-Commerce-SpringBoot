package com.ESD.ecomm.services;
import com.ESD.ecomm.entities.WishList;
import com.ESD.ecomm.entities.User;
import com.ESD.ecomm.entities.Product;
import com.ESD.ecomm.repositories.WishListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class WishListService {

    private final WishListRepository wishListRepository;

    @Autowired
    public WishListService(WishListRepository wishListRepository) {
        this.wishListRepository = wishListRepository;
    }

    public Optional<WishList> getWishlistByUser(User user) {
        return wishListRepository.findByUser(user);
    }

    public boolean existsByUserAndProduct(User user, Product product) {
        return wishListRepository.existsByUserAndProductsContaining(user, product);
    }

    public WishList addProductToWishlist(WishList wishlist, Product product) {
        if (!wishlist.getProducts().contains(product)) {
            wishlist.getProducts().add(product);
            return wishListRepository.save(wishlist);
        }
        return wishlist;
    }

    public WishList removeProductFromWishlist(WishList wishlist, Product product) {
        if (wishlist.getProducts().contains(product)) {
            wishlist.getProducts().remove(product);
            return wishListRepository.save(wishlist);
        }
        return wishlist;
    }
}
