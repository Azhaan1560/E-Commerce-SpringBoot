package com.ESD.ecomm.services;
import com.ESD.ecomm.entities.Cart;
import com.ESD.ecomm.entities.User;
import com.ESD.ecomm.repositories.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional
public class CartService {

    private final CartRepository cartRepository;

    @Autowired
    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Optional<Cart> getCartByUser(User user) {
        return cartRepository.findByUser(user);
    }

    public boolean cartExistsForUser(User user) {
        return cartRepository.existsByUser(user);
    }

    public Cart saveCart(Cart cart) {
        return cartRepository.save(cart);
    }

    public void deleteCart(Long cartId) {
        cartRepository.deleteById(cartId);
    }

    public Optional<Cart> getCartById(Long cartId) {
        return cartRepository.findById(cartId);
    }

}
