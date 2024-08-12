package br.com.pagbanks.projeto_onboarding.dto;

import br.com.pagbanks.projeto_onboarding.entity.Item;

import java.time.LocalDateTime;
import java.util.List;


public record CartDto(

        Long id,
        List<Item> listItens,
        LocalDateTime creationDate,
        Double totalValue

) {
}
