package br.com.pagbanks.projeto_onboarding.repository;

import br.com.pagbanks.projeto_onboarding.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

}
