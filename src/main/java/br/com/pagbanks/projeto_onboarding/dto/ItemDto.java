package br.com.pagbanks.projeto_onboarding.dto;


import br.com.pagbanks.projeto_onboarding.entity.Item;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

public record ItemDto(
        Long id,
        @NotBlank(message = "The name cannot be empty")
        String name,
        @DecimalMin(value = "0.0", inclusive = false, message = "The price cannot be zero or < 0")
        Double price,
        Integer amount
) {

//        public static ItemDto from(Item item){
//                return new ItemDto(item.getId(), item.getName(), item.getPrice(), item.getAmount());
//        }






}

