package br.com.pagbanks.projeto_onboarding.controller;

import br.com.pagbanks.projeto_onboarding.dto.CartDto;
import br.com.pagbanks.projeto_onboarding.entity.Cart;
import br.com.pagbanks.projeto_onboarding.entity.Item;
import br.com.pagbanks.projeto_onboarding.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public Cart saveCart(@RequestBody CartDto cartDto) {
        return cartService.save(new Cart(cartDto));
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
        public List<Cart> list(){
        return cartService.findAll().stream().toList();
    }

    @GetMapping("/{cartId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Cart> getCartById(@PathVariable Long cartId){
        return ResponseEntity.ok(cartService.findById(cartId));
    }

    @DeleteMapping("/{cartId}")
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long cartId){
        cartService.delete(cartId);
    }

    @PutMapping("/{cartId}/remove/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public ResponseEntity<Cart> removeItemFromCart(@PathVariable Long cartId, @PathVariable Long itemId){
        Cart updateCart = cartService.removeItem(cartId,itemId);
        return ResponseEntity.ok(updateCart);
    }

    @PutMapping("/{cartId}/add/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public ResponseEntity<Cart> admItemFromCart(@PathVariable Long cartId, @PathVariable Long itemId){
        Cart updateCart = cartService.addItem(cartId,itemId);
        return ResponseEntity.ok(updateCart);
    }

}
