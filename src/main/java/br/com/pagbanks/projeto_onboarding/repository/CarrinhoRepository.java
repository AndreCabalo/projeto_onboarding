package br.com.pagbanks.projeto_onboarding.repository;

import br.com.pagbanks.projeto_onboarding.model.Carrinho;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CarrinhoRepository extends JpaRepository<Carrinho, UUID> {
}
