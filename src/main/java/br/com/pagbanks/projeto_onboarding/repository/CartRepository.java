package br.com.pagbanks.projeto_onboarding.repository;

import br.com.pagbanks.projeto_onboarding.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

}
