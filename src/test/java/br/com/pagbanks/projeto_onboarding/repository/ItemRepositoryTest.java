package br.com.pagbanks.projeto_onboarding.repository;

import br.com.pagbanks.projeto_onboarding.dto.ItemDto;
import br.com.pagbanks.projeto_onboarding.entity.Item;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Should find item by name, from DB")
    void findByNameSucess() {
        String nameItem = "ItemTest1";
        ItemDto itemDto = new ItemDto(1L, nameItem, 15.00,1);
        this.createItem(itemDto);

        Optional<Item> foundItem = Optional.ofNullable(this.itemRepository.findByName(nameItem));

        assertThat(foundItem.isPresent()).isTrue();

    }

    @Test
    @DisplayName("Should not find item by name, from DB")
    void findByNameFail() {
        String nameItem = "ItemTest1";
        String wrongNameItem = "ItemTest2";
        ItemDto itemDto = new ItemDto(1L, wrongNameItem, 15.00,1);
        Optional<Item> foundItem = Optional.ofNullable(this.itemRepository.findByName(nameItem));

        assertThat(foundItem.isEmpty()).isTrue();

    }

    private Item createItem(ItemDto itemDto) {
        Item newItem = new Item(itemDto);
        this.entityManager.persist(newItem);
        return newItem;
    }
}