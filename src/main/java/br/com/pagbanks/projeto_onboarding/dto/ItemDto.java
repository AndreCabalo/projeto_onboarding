package br.com.pagbanks.projeto_onboarding.dto;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

public record ItemDto(
        Long id,
        @NotBlank(message = "O nome não pode ser vazio")
        String nome,
        @DecimalMin(value = "0.0", inclusive = false, message = "O preço deve ser maior que zero")
        double preco,
        int quantidade
) {
}

