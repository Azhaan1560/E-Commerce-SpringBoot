package com.ESD.ecomm.services;
import com.ESD.ecomm.entities.Cart;
import com.ESD.ecomm.entities.Cart_Items;
import com.ESD.ecomm.entities.Product;
import com.ESD.ecomm.repositories.CartItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartItemService {

    private final CartItemRepository cartItemRepository;

    @Autowired
    public CartItemService(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    public List<Cart_Items> getItemsByCart(Cart cart) {
        return cartItemRepository.findByCart(cart);
    }

    public Optional<Cart_Items> getCartItem(Cart cart, Product product) {
        return cartItemRepository.findByCartAndProduct(cart, product);
    }

    public Cart_Items saveCartItem(Cart_Items cartItem) {
        return cartItemRepository.save(cartItem);
    }

    public void deleteItemsByCart(Cart cart) {
        cartItemRepository.deleteByCart(cart);
    }

    public boolean existsInCart(Cart cart, Product product) {
        return cartItemRepository.existsByCartAndProduct(cart, product);
    }

    public List<Cart_Items> getAllCartItems() {return cartItemRepository.findAll();
    }

}

