package br.com.pagbanks.projeto_onboarding.repository;

import br.com.pagbanks.projeto_onboarding.entity.Carrinho;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CarrinhoRepository extends JpaRepository<Carrinho, Long> {
}
