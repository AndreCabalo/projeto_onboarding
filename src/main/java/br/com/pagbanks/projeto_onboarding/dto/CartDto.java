package br.com.pagbanks.projeto_onboarding.dto;

import br.com.pagbanks.projeto_onboarding.entity.Cart;
import br.com.pagbanks.projeto_onboarding.entity.Item;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.Set;


public record CartDto(

        Long id,
        @JsonProperty("list_items")
        Set<Item> listItens,
        @JsonProperty("creation_date")
        LocalDate creationDate,
        @JsonProperty("total_value")
        Double totalValue

) {

        public CartDto {
                if (creationDate == null) {
                        creationDate = LocalDate.now();
                }
                if (totalValue == null) {
                        totalValue = 0.0;
                }
        }

        public CartDto(Cart cart){
                this(cart.getId(),cart.getListItens(),cart.getCreationDate(),cart.getTotalValue());
        }
}
