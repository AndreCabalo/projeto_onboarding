package br.com.pagbanks.projeto_onboarding.controller;

import br.com.pagbanks.projeto_onboarding.dto.CartDto;
import br.com.pagbanks.projeto_onboarding.entity.Cart;
import br.com.pagbanks.projeto_onboarding.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public Cart saveCart(@RequestBody @Valid CartDto cartDto) {
        return cartService.save(new Cart(cartDto));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
        public List<Cart> list(){
        return cartService.findAll().stream().toList();
    }





}
