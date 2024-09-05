package br.com.pagbanks.projeto_onboarding.service;

import br.com.pagbanks.projeto_onboarding.dto.CartDto;
import br.com.pagbanks.projeto_onboarding.dto.ItemDto;
import br.com.pagbanks.projeto_onboarding.entity.Item;
import br.com.pagbanks.projeto_onboarding.exceptions.ItemAlreadyAddedException;
import br.com.pagbanks.projeto_onboarding.exceptions.ResourceNotFoundException;
import br.com.pagbanks.projeto_onboarding.mapper.CartMapper;
import br.com.pagbanks.projeto_onboarding.mapper.ItemMapper;
import br.com.pagbanks.projeto_onboarding.repository.CartRepository;
import br.com.pagbanks.projeto_onboarding.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @InjectMocks
    private CartService cartService;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ItemService itemService;

    @Mock
    private ItemRepository itemRepository;

    private ItemDto itemDto;
    private CartDto cartDto;
    private CartDto cartDtoWithItem;

    @BeforeEach
    void setUp() {
        cartDto = new CartDto(1L, new HashSet<>(), null, 0.0);
        itemDto = new ItemDto(1L, "TestItem", 19.99, 10);
        Set<Item> items = new HashSet<>();
        items.add(ItemMapper.toItemFrom(itemDto));
        cartDtoWithItem = new CartDto(2L, items, null, 0.0);
    }

    @Test
    @DisplayName("Should save cart, without throwing any exception")
    void saveSuccess() {
        when(cartRepository.save(CartMapper.toCartFrom(cartDto))).thenReturn(CartMapper.toCartFrom(cartDto));

        var response = assertDoesNotThrow(() -> cartService.save(cartDto));

        assertNotNull(response);
        assertThat(response).isEqualTo(cartDto);
    }

    @Test
    @DisplayName("Should find the cart, when the id exists")
    void findByIdSuccessWhenIdExists() {
        when(cartRepository.findById(1L)).thenReturn(Optional.of(CartMapper.toCartFrom(cartDto)));

        var response = assertDoesNotThrow(() -> cartService.findById(1L));

        assertNotNull(response);
        assertThat(response).isEqualTo(cartDto);
        verify(cartRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    @DisplayName("Shouldn't find the cart, when the id does not exists")
    void findByIdThrowWhenIdNonExists() {
        when(cartRepository.findById(9999L)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> {
            cartService.findById(9999L);
        });

        verify(cartRepository, Mockito.times(1)).findById(9999L);
    }

    @Test
    @DisplayName("Should find all carts, when any cart is saved")
    void findAllSuccessWhenAnyCartIsSaved() {
        when(cartRepository.findAll()).thenReturn(List.of(CartMapper.toCartFrom(cartDto)));

        List<CartDto> response = assertDoesNotThrow(() -> cartService.findAll());

        assertNotNull(response);
        assertThat(response.size()).isGreaterThan(0);
        verify(cartRepository, Mockito.times(1)).findAll();
    }

    @Test
    @DisplayName("Should find nothing, when nothing is saved")
    void findAllWithZeroCartsSaved() {
        when(cartRepository.findAll()).thenReturn(List.of());

        List<CartDto> response = assertDoesNotThrow(() -> cartService.findAll());

        assertNotNull(response);
        assertThat(response.size()).isZero();
        verify(cartRepository, Mockito.times(1)).findAll();
    }

    @Test
    @DisplayName("Should add an item to the Cart, when the item id and cart id exist, and there is sufficient quantity")
    void addItemSuccess() {
        when(cartRepository.findById(anyLong())).thenReturn(Optional.of(CartMapper.toCartFrom(cartDto)));
        when(itemService.findById(anyLong())).thenReturn(itemDto);
        when(cartRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        assertDoesNotThrow(
                () -> cartService.addItem(cartDto.id(),
                        itemDto.id())
        );

        assertThat(cartDto.listItems()).isNotNull();
        verify(cartRepository, Mockito.times(1)).findById(anyLong());
        verify(itemService, Mockito.times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Should not add an item to the Cart, when there is not enough quantity")
    void addItemThrowsEnoughQuantity() {
        ItemMapper.toItemFrom(itemDto).setAmount(0);

        assertThrows(ResourceNotFoundException.class, () -> {
            cartService.addItem(1L, 1L);
        });

        assertThat(CartMapper.toCartFrom(cartDto).getListItems()).size().isEqualTo(0);
        verify(cartRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should not add an item to the Cart, when the item has already been added")
    void addItemThrowsItemAlreadyAdded() {
        when(cartRepository.findById(1L)).thenReturn(Optional.of(CartMapper.toCartFrom(cartDto)));
        when(itemService.findById(1L)).thenThrow(ItemAlreadyAddedException.class);

        assertThrows(ItemAlreadyAddedException.class, () -> {
            cartService.addItem(1L, 1L);
        });

        verify(cartRepository, Mockito.times(1)).findById(1L);
        verify(itemService, Mockito.times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should not add an item to the Cart, when the item id is wrong")
    void addItemThrowsWrongItemId() {
        when(cartRepository.findById(1L)).thenReturn(Optional.of(CartMapper.toCartFrom(cartDto)));
        when(itemService.findById(999L)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> {
            cartService.addItem(1L, 999L);
        });

        verify(cartRepository, Mockito.times(1)).findById(1L);
        verify(itemService, Mockito.times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should not add an item to the Cart, when the cart id is wrong")
    void addItemThrowsWrongCartId() {
        when(cartRepository.findById(999L)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> {
            cartService.addItem(999L, 1L);
        });

        verify(cartRepository, Mockito.times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should remove an item to the Cart")
    void removeItemSuccess() {
        when(cartRepository.findById(anyLong())).thenReturn(Optional.of(CartMapper.toCartFrom(cartDtoWithItem)));
        when(itemService.findById(anyLong())).thenReturn(itemDto);
        when(cartRepository.save(any())).thenReturn(CartMapper.toCartFrom(cartDtoWithItem));

        assertDoesNotThrow(() -> cartService.removeItem(CartMapper.toCartFrom(cartDtoWithItem).getId(),
                ItemMapper.toItemFrom(itemDto).getId()));

        assertThat(cartDtoWithItem.listItems().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should not remove an item to the Cart, when item not found in cart")
    void removeItemThrowsItemId() {
        when(cartRepository.findById(1L)).thenReturn(Optional.of(CartMapper.toCartFrom(cartDto)));
        when(itemService.findById(999L)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> {
            cartService.removeItem(1L, 999L);
        });

        verify(cartRepository, Mockito.times(1)).findById(1L);
        verify(itemService, Mockito.times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should not remove an item from the Cart, when the cart is not found")
    void removeItemThrowsCartId() {
        when(cartRepository.findById(999L)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> {
            cartService.removeItem(999L, 1L);
        });

        verify(cartRepository, Mockito.times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should delete a cart, when the cart id is found")
    void deleteSuccess() {
        when(cartRepository.findById(1L)).thenReturn(Optional.of(CartMapper.toCartFrom(cartDto)));
        Mockito.doNothing().when(cartRepository).delete(CartMapper.toCartFrom(cartDto));

        assertDoesNotThrow(() -> cartService.delete(1L));

        verify(cartRepository, Mockito.times(1)).delete(CartMapper.toCartFrom(cartDto));
    }

    @Test
    @DisplayName("Should not delete a cart, when the cart id is not found")
    void deleteThrowsIdCart() {
        when(cartRepository.findById(999L)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> {
            cartService.delete(999L);
        });

        verify(cartRepository, Mockito.times(1)).findById(999L);
    }

}