package br.com.pagbanks.projeto_onboarding.dto;

import java.util.UUID;

public record ItemDto(UUID id, String nome, double preco, int quantidadeEstoque) {
}
