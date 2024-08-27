package br.com.pagbanks.projeto_onboarding.controller;

import br.com.pagbanks.projeto_onboarding.dto.CartDto;
import br.com.pagbanks.projeto_onboarding.entity.Cart;
import br.com.pagbanks.projeto_onboarding.mapper.CartMapper;
import br.com.pagbanks.projeto_onboarding.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping
    public ResponseEntity<Cart> saveCart(@RequestBody CartDto cartDto) {
        var cart = cartService.save(CartMapper.toCartFrom(cartDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(cart);
    }

    @GetMapping
    public ResponseEntity<List<Cart>> list() {
        return ResponseEntity.ok(cartService.findAll());
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<Cart> getCartById(@PathVariable Long cartId) {
        Cart cart = cartService.findById(cartId);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> delete(@PathVariable Long cartId) {
        cartService.delete(cartId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{cartId}/remove/{itemId}")
    public ResponseEntity<Cart> removeItemFromCart(@PathVariable Long cartId, @PathVariable Long itemId) {
        Cart updateCart = cartService.removeItem(cartId, itemId);
        return ResponseEntity.ok(updateCart);
    }

    @PutMapping("/{cartId}/add/{itemId}")
    public ResponseEntity<Cart> addItemFromCart(@PathVariable Long cartId, @PathVariable Long itemId) {
        Cart updateCart = cartService.addItem(cartId, itemId);
        return ResponseEntity.ok(updateCart);
    }

}
