package br.com.pagbanks.projeto_onboarding.dto;

import br.com.pagbanks.projeto_onboarding.entity.Item;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CarrinhoDto(UUID id, List<Item> listaItens, LocalDateTime dataCriacao, double valorTotal) {
}
