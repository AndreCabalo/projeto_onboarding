package br.com.pagbanks.projeto_onboarding.service;

import br.com.pagbanks.projeto_onboarding.entity.Cart;
import br.com.pagbanks.projeto_onboarding.entity.Item;
import br.com.pagbanks.projeto_onboarding.exceptions.ItemAlreadyAddedException;
import br.com.pagbanks.projeto_onboarding.exceptions.ResourceNotFoundException;
import br.com.pagbanks.projeto_onboarding.repository.CartRepository;
import br.com.pagbanks.projeto_onboarding.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

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

    private Item item;
    private Cart cart;
    private Cart cartWithItem;

    @BeforeEach
    void setUp() {
        cart = new Cart(1L, new HashSet<>(), null, 0.0);
        item = new Item(1L, "TestItem", 19.99, 10);
        Set<Item> items = new HashSet<>();
        items.add(item);
        cartWithItem = new Cart(2L, items, null, 0.0);
    }

    @Test
    @DisplayName("Should save cart, without throwing any exception")
    void saveSuccess() {
        when(cartRepository.save(cart)).thenReturn(cart);

        var response = assertDoesNotThrow(() -> cartService.save(cart));

        assertNotNull(response);
        assertThat(response).isEqualTo(cart);
        verify(cartRepository, Mockito.times(1)).save(cart);
    }

    @Test
    @DisplayName("Should find the cart, when the id exists")
    void findByIdSuccessWhenIdExists() {
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));

        var response = assertDoesNotThrow(() -> cartService.findById(1L));

        assertNotNull(response);
        assertThat(response).isEqualTo(cart);
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
        when(cartRepository.findAll()).thenReturn(List.of(cart));

        List<Cart> response = assertDoesNotThrow(() -> cartService.findAll());

        assertNotNull(response);
        assertThat(response.size()).isGreaterThan(0);
        verify(cartRepository, Mockito.times(1)).findAll();
    }

    @Test
    @DisplayName("Should find nothing, when nothing is saved")
    void findAllWithZeroCartsSaved() {
        when(cartRepository.findAll()).thenReturn(List.of());

        List<Cart> response = assertDoesNotThrow(() -> cartService.findAll());

        assertNotNull(response);
        assertThat(response.size()).isZero();
        verify(cartRepository, Mockito.times(1)).findAll();
    }

    //TODO Falta este teste tbm
    @Test
    @DisplayName("Should add an item to the Cart, when the item id and cart id exist, and there is sufficient quantity")
    void addItemSuccess() {
        when(cartRepository.findById(anyLong())).thenReturn(Optional.of(cart));
        when(itemService.findById(anyLong())).thenReturn(item);
        when(cartRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        assertDoesNotThrow(() -> cartService.addItem(cart.getId(), item.getId()));

        assertThat(cart.getListItems()).isNotNull();
        verify(cartRepository, Mockito.times(1)).findById(anyLong());
        verify(itemService, Mockito.times(1)).findById(anyLong());
        verify(cartRepository, Mockito.times(1)).save(cart);
    }

    @Test
    @DisplayName("Should not add an item to the Cart, when there is not enough quantity")
    void addItemThrowsEnoughQuantity() {
        item.setAmount(0);

        assertThrows(ResourceNotFoundException.class, () -> {
            cartService.addItem(1L, 1L);
        });

        assertThat(cart.getListItems()).size().isEqualTo(0);
        verify(cartRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should not add an item to the Cart, when the item has already been added")
    void addItemThrowsItemAlreadyAdded() {
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
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
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
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
        when(cartRepository.findById(anyLong())).thenReturn(Optional.of(cartWithItem));
        when(itemService.findById(anyLong())).thenReturn(item);
        when(cartRepository.save(any())).thenReturn(cartWithItem);

        assertDoesNotThrow(() -> cartService.removeItem(cartWithItem.getId(), item.getId()));

        ArgumentCaptor<Cart> cartAC = ArgumentCaptor.forClass(Cart.class);
        verify(cartRepository, Mockito.times(1)).findById(anyLong());
        verify(itemService, Mockito.times(1)).findById(anyLong());
        verify(cartRepository, Mockito.times(1)).save(cartAC.capture());
        Cart cartSaved = cartAC.getValue();
        assertThat(cartSaved.getListItems().size()).isEqualTo(0);

    }

    @Test
    @DisplayName("Should not remove an item to the Cart, when item not found in cart")
    void removeItemThrowsItemId() {
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
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
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        Mockito.doNothing().when(cartRepository).delete(cart);

        assertDoesNotThrow(() -> cartService.delete(1L));

        verify(cartRepository, Mockito.times(1)).delete(cart);
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