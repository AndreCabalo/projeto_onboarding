package br.com.pagbanks.projeto_onboarding.controller;

import br.com.pagbanks.projeto_onboarding.dto.CartDto;
import br.com.pagbanks.projeto_onboarding.entity.Cart;
import br.com.pagbanks.projeto_onboarding.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public void saveCart(@RequestBody @Valid CartDto cartDto) {
        cartService.save(new Cart(cartDto));
    }





}
