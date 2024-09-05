package br.com.pagbanks.projeto_onboarding.controller;

import br.com.pagbanks.projeto_onboarding.dto.CartDto;
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
    public ResponseEntity<CartDto> saveCart(@RequestBody CartDto cartDto) {
        CartDto cart = cartService.save(cartDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(cart);
    }

    @GetMapping
    public ResponseEntity<List<CartDto>> list() {
        return ResponseEntity.ok(cartService.findAll());
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCartById(@PathVariable Long cartId) {
        CartDto cartDto = cartService.findById(cartId);

        return ResponseEntity.ok(cartDto);
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> delete(@PathVariable Long cartId) {
        cartService.delete(cartId);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{cartId}/remove/{itemId}")
    public ResponseEntity<CartDto> removeItemFromCart(@PathVariable Long cartId, @PathVariable Long itemId) {
        CartDto updateCart = cartService.removeItem(cartId, itemId);
        return ResponseEntity.ok(updateCart);
    }


    @PutMapping("/{cartId}/add/{itemId}")
    public ResponseEntity<CartDto> addItemFromCart(@PathVariable Long cartId, @PathVariable Long itemId) {
        CartDto updateCart = cartService.addItem(cartId, itemId);
        return ResponseEntity.ok(updateCart);
    }

}
