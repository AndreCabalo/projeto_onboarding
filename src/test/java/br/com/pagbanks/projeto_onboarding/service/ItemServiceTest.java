package br.com.pagbanks.projeto_onboarding.service;

import br.com.pagbanks.projeto_onboarding.dto.ItemDto;
import br.com.pagbanks.projeto_onboarding.entity.Item;
import br.com.pagbanks.projeto_onboarding.exceptions.ResourceNotFoundException;
import br.com.pagbanks.projeto_onboarding.mapper.ItemMapper;
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
        ItemDto itemDto = new ItemDto(1L, nameItem, 15.00, 1);

        Item savedItem = this.itemService.save(ItemMapper.toItemFrom(itemDto));
        assertThat(savedItem).isNotNull();
    }

    @Test
    @DisplayName("Shouldn't save the item, when name is null and throws exception")
    void saveItemThrowsBecauseNameIsNull() {
        String nameItem = null;
        ItemDto itemDto = new ItemDto(1L, nameItem, 15.00, 1);

        assertThrows(Exception.class, () -> {
                    this.itemService.save(ItemMapper.toItemFrom(itemDto));
                }
        );
    }

    @Test
    @DisplayName("Should find all items, when any item is saved")
    void findAllIsSuccessWhenAnyItemIsSaved() {
        String nameItem = "ItemTestFindAllSucess";
        ItemDto itemDto = new ItemDto(1L, nameItem, 15.00, 1);
        this.itemService.save(ItemMapper.toItemFrom(itemDto));

        assertThat(this.itemService.findAll().size()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Should find nothing, when nothing is saved")
    void findAllWithZeroItemsSaved() {
        List<Item> itemList = itemService.findAll();
        for (Item item : itemList) {
            itemService.delete(item.getId());
        }
        itemList = itemService.findAll();
        assertThat(itemList.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should update an exists item, when item exists")
    void updateSuccessBecauseItemExists() {
        ItemDto itemDto = new ItemDto(1L, "ItemTestUpdateSucess", 150.00, 10);
        itemService.save(ItemMapper.toItemFrom(itemDto));
        List<Item> listItems = itemService.findAll();
        Long idExistingOnDataBase = listItems.get(0).getId();
        Item existingItem = itemService.update(idExistingOnDataBase, ItemMapper.toItemFrom(itemDto));

        assertThat(existingItem).isNotNull();
    }

    @Test
    @DisplayName("Shouldn't update an exists item, when item does not exist")
    void updateThrowsBecauseItemNotFound() {
        ItemDto itemDto = new ItemDto(1L, "ItemTestUpdateThrownBecauseItemNotFound", 150.00, 10);
        Long idNonExistentOnDataBase = 9999L;

        assertThrows(ResourceNotFoundException.class, () -> {
                    this.itemService.update(idNonExistentOnDataBase, ItemMapper.toItemFrom(itemDto));
                }
        );
    }

    @Test
    @DisplayName("Should delete an exists item, when item exists")
    void deleteSuccessBecauseItemExists() {
        ItemDto itemDto = new ItemDto(1L, "ItemTestDeleteSucess", 150.00, 10);
        Item existingItem = itemService.save(ItemMapper.toItemFrom(itemDto));

        itemService.delete(existingItem.getId());
        assertThrows(ResourceNotFoundException.class, () -> {
                    this.itemService.findById(existingItem.getId());
                }
        );
    }

    @Test
    @DisplayName("Shouldn't delete an exists item, when item does not exist")
    void deleteThrowsBecauseItemNotFound() {
        ItemDto itemDto = new ItemDto(1L, "ItemTestDeleteThrows", 150.00, 10);
        Item existingItem = itemService.save(ItemMapper.toItemFrom(itemDto));
        Long idNonExistingOnDataBase = 500L;

        assertThrows(ResourceNotFoundException.class, () -> {
                    this.itemService.delete(idNonExistingOnDataBase);
                }
        );
    }


    @Test
    @DisplayName("Should update an exists item, when item exists")
    void addAmountSuccess() {
        ItemDto itemDto = new ItemDto(1L, "ItemTestAddAmountSucess", 150.00, 105);
        Item existingItem = itemService.save(ItemMapper.toItemFrom(itemDto));
        existingItem = itemService.addAmount(existingItem.getId(), 100);

        assertThat(existingItem.getAmount()).isEqualTo(205);
    }

    @Test
    @DisplayName("Shouldn't update an exists item, when item does not exists")
    void addAmountThrowItemNotFound() {
        ItemDto itemDto = new ItemDto(1L, "ItemTestAddAmountSucess", 150.00, 105);
        Item existingItem = itemService.save(ItemMapper.toItemFrom(itemDto));
        Long nonExistingId = 999L;

        assertThrows(ResourceNotFoundException.class, () -> {
            this.itemService.addAmount(nonExistingId, 100);
        });

    }

}