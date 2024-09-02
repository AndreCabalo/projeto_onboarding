package br.com.pagbanks.projeto_onboarding.service;

import br.com.pagbanks.projeto_onboarding.entity.Item;
import br.com.pagbanks.projeto_onboarding.exceptions.ResourceNotFoundException;
import br.com.pagbanks.projeto_onboarding.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @InjectMocks
    private ItemService itemService;

    @Mock
    private ItemRepository itemRepository;

    private Item item;

    @BeforeEach
    void setUp() {
        item = new Item(1L, "ItemTest", 15.00, 1);
    }

    @Test
    @DisplayName("Should save the item, without throwing any exception")
    void saveItemSuccess() {
        when(itemRepository.save(item)).thenReturn(item);

        Item response = assertDoesNotThrow(() -> itemService.save(item));

        assertNotNull(response);
        assertThat(response).isEqualTo(item);
        verify(itemRepository, Mockito.times(1)).save(item);
    }

    @Test
    @DisplayName("Shouldn't save the item, when name is null and throws exception MethodArgumentNotValidException")
    void saveItemThrowsBecauseNameIsNull() {
        item.setName(null);

        assertThrows(ResourceNotFoundException.class, () -> {
            itemService.save(item);
        });

        verify(itemRepository, Mockito.times(0)).save(item);
    }

    @Test
    @DisplayName("Should find all items, when any item is saved")
    void findAllIsSuccessWhenAnyItemIsSaved() {
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<Item> response = assertDoesNotThrow(() -> itemService.findAll());

        assertNotNull(response);
        assertThat(response.size()).isGreaterThan(0);
        verify(itemRepository, Mockito.times(1)).findAll();
    }

    @Test
    @DisplayName("Should find nothing, when nothing is saved")
    void findAllWithZeroItemsSaved() {
        when(itemRepository.findAll()).thenReturn(List.of());

        List<Item> response = assertDoesNotThrow(() -> itemService.findAll());

        assertNotNull(response);
        assertThat(response.size()).isEqualTo(0);
        verify(itemRepository, Mockito.times(1)).findAll();
    }

    @Test
    @DisplayName("Should find the item, when the id exists")
    void findByIdSuccessWhenIdExists() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        Item response = assertDoesNotThrow(() -> itemService.findById(1L));

        assertNotNull(response);
        assertThat(response).isEqualTo(item);
        verify(itemRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    @DisplayName("Shouldn't find the item, when the id does not exists")
    void findByIdThrowWhenIdNonExists() {
        when(itemRepository.findById(9999L)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> {
            itemService.findById(9999L);
        });

        verify(itemRepository, Mockito.times(1)).findById(9999L);
    }

    @Test
    @DisplayName("Should update an exists item, when item exists")
    void updateSuccessBecauseItemExists() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.save(item)).thenReturn(item);

        Item response = assertDoesNotThrow(() -> itemService.update(1L, item));

        assertNotNull(response);
        assertThat(response).isEqualTo(item);
        verify(itemRepository, Mockito.times(1)).findById(1L);
        verify(itemRepository, Mockito.times(1)).save(item);

    }

    @Test
    @DisplayName("Shouldn't update an exists item, when item does not exist")
    void updateThrowsBecauseItemNotFound() {
        when(itemRepository.findById(9999L)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> {
            itemService.update(9999L, item);
        });

        verify(itemRepository, Mockito.times(1)).findById(9999L);
    }

    @Test
    @DisplayName("Should delete an exists item, when item exists")
    void deleteSuccessBecauseItemExists() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        doNothing().when(itemRepository).delete(item);

        assertDoesNotThrow(() -> itemService.delete(1L));

        verify(itemRepository, Mockito.times(1)).delete(item);
    }

    @Test
    @DisplayName("Shouldn't delete an exists item, when item does not exist")
    void deleteThrowsBecauseItemNotFound() {
        when(itemRepository.findById(9999L)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> {
            itemService.delete(9999L);
        });

        verify(itemRepository, Mockito.times(1)).findById(9999L);
        verify(itemRepository, Mockito.times(0)).delete(item);
    }

    @Test
    @DisplayName("Should add an amount to the item, when item exists")
    void addAmountSuccess() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.save(item)).thenReturn(item);

        assertDoesNotThrow(() -> itemService.addAmount(1L, 1));

        assertThat(item.getAmount()).isEqualTo(2);
        verify(itemRepository, Mockito.times(1)).findById(1L);
        verify(itemRepository, Mockito.times(1)).save(item);
    }

    @Test
    @DisplayName("Shouldn't add an amount to the item, when item does not exist")
    void addAmountThrowsBecauseItemNotFound() {
        when(itemRepository.findById(9999L)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> {
            itemService.addAmount(9999L, 1);
        });

        verify(itemRepository, Mockito.times(1)).findById(9999L);
    }
}