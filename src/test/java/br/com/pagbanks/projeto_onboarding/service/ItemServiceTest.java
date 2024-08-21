package br.com.pagbanks.projeto_onboarding.service;

import br.com.pagbanks.projeto_onboarding.dto.ItemDto;
import br.com.pagbanks.projeto_onboarding.entity.Item;
import br.com.pagbanks.projeto_onboarding.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@ActiveProfiles("test")
class ItemServiceTest {

    @Autowired
    private ItemService itemService;

    @Test
    @DisplayName("Should save the item, when all required fields are filled")
    void saveItemSuccess() {
        String nameItem = "ItemTestSaveItemSucess";
        ItemDto itemDto = new ItemDto(1L, nameItem, 15.00,1);

        Item savedItem= this.itemService.save(new Item(itemDto));
        assertThat(savedItem).isNotNull();
    }

    @Test
    @DisplayName("Shouldn't save the item, when name is null and throws exception")
    void saveItemThrowsBecauseNameIsNull() {
        String nameItem = null;
        ItemDto itemDto = new ItemDto(1L, nameItem, 15.00,1);

        assertThrows(Exception.class, () -> {
                    this.itemService.save(new Item(itemDto));
                }
        );
    }

    @Test
    @DisplayName("Should find all items, when any item is saved")
    void findAllIsSuccessWhenAnyItemIsSaved() {
        String nameItem = "ItemTestFindAllSucess";
        ItemDto itemDto = new ItemDto(1L, nameItem, 15.00,1);
        this.itemService.save(new Item(itemDto));

        assertThat(this.itemService.findAll().size()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Should find nothing, when nothing is saved")
    void findAllWithZeroItemsSaved() {
        List<Item> itemList = itemService.findAll().stream().toList();
        for (Item item : itemList) {
            itemService.delete(item.getId());
        }
        assertThat(this.itemService.findAll().size()).isZero();
    }

    @Test
    @DisplayName("Should update an exists item, when item exists")
    void updateSuccessBecauseItemExists() {
        ItemDto itemSaved =  new ItemDto(1L, "ItemTestUpdateSucess", 150.00,10);
        itemService.save(new Item(itemSaved));
        List<Item> listItems = itemService.findAll();
        Long idExistingOnDataBase = listItems.get(0).getId();
        Item existingItem = itemService.update(idExistingOnDataBase, new Item(itemSaved));

        assertThat(existingItem).isNotNull();
    }

    @Test
    @DisplayName("Shouldn't update an exists item, when item does not exist")
    void updateThrowsBecauseItemNotFound() {
        ItemDto itemSaved =  new ItemDto(1L, "ItemTestUpdateThrownBecauseItemNotFound", 150.00,10);
        Long idNonExistentOnDataBase = 9999L;

        assertThrows(ResourceNotFoundException.class, () -> {
                    this.itemService.update(idNonExistentOnDataBase, new Item(itemSaved));
                }
        );
    }

    @Test
    @DisplayName("Should delete an exists item, when item exists")
    void deleteSuccessBecauseItemExists() {
        ItemDto itemSaved =  new ItemDto(1L, "ItemTestDeleteSucess", 150.00,10);
        Item existingItem = itemService.save(new Item(itemSaved));

        itemService.delete(existingItem.getId());
        assertThrows(ResourceNotFoundException.class, () -> {
                    this.itemService.findById(existingItem.getId());
                }
        );
    }

    @Test
    @DisplayName("Shouldn't delete an exists item, when item does not exist")
    void deleteThrowsBecauseItemNotFound() {
        ItemDto itemSaved =  new ItemDto(1L, "ItemTestDeleteThrows", 150.00,10);
        Item existingItem = itemService.save(new Item(itemSaved));
        Long idNonExistingOnDataBase = 500L;

        assertThrows(ResourceNotFoundException.class, () -> {
                    this.itemService.delete(idNonExistingOnDataBase);
                }
        );
    }


    @Test
    @DisplayName("Should update an exists item, when item exists")
    void addAmountSuccess() {
        ItemDto itemSaved =  new ItemDto(1L, "ItemTestAddAmountSucess", 150.00,105);
        Item existingItem = itemService.save(new Item(itemSaved));
        existingItem = itemService.addAmount(existingItem.getId(),100);

        assertThat(existingItem.getAmount()).isEqualTo(205);
    }

    @Test
    @DisplayName("Shouldn't update an exists item, when item does not exists")
    void addAmountThrowItemNotFound() {
        ItemDto itemSaved =  new ItemDto(1L, "ItemTestAddAmountSucess", 150.00,105);
        Item existingItem = itemService.save(new Item(itemSaved));
        Long nonExistingId = 999L;

        assertThrows(ResourceNotFoundException.class, () -> {
            this.itemService.addAmount(nonExistingId,100);
        });

    }

}