package br.com.pagbanks.projeto_onboarding.controller;

import br.com.pagbanks.projeto_onboarding.dto.CarrinhoDto;
import br.com.pagbanks.projeto_onboarding.entity.Carrinho;
import br.com.pagbanks.projeto_onboarding.service.CarrinhoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/carrinho")
public class CarrinhoController {

    @Autowired
    private CarrinhoService carrinhoService;


    @PostMapping
    @Transactional
    public void criarCarrinho(@RequestBody @Valid CarrinhoDto carrinhoDto) {
        carrinhoService.save(new Carrinho(carrinhoDto));
    }
}
