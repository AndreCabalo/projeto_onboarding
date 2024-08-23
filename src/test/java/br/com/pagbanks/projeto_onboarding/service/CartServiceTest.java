package br.com.pagbanks.projeto_onboarding.service;

import br.com.pagbanks.projeto_onboarding.dto.CartDto;
import br.com.pagbanks.projeto_onboarding.dto.ItemDto;
import br.com.pagbanks.projeto_onboarding.entity.Cart;
import br.com.pagbanks.projeto_onboarding.entity.Item;
import br.com.pagbanks.projeto_onboarding.exceptions.ItemAlreadyAddedException;
import br.com.pagbanks.projeto_onboarding.exceptions.ResourceNotFoundException;
import br.com.pagbanks.projeto_onboarding.mapper.CartMapper;
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
class CartServiceTest {

    @Autowired
    private CartService cartService;

    @Autowired
    private ItemService itemService;


    @Test
    @DisplayName("Should save cart, when all required fields are filled")
    void saveSuccess() {
        CartDto cartDto = new CartDto(1L,null,null,null);
        Cart savedCart = this.cartService.save(CartMapper.toCartFrom(cartDto));

        assertThat(savedCart).isNotNull();
    }

    @Test
    @DisplayName("Should find the cart, when the id exists")
    void findByIdSuccessWhenIdExists() {
        CartDto cartDto = new CartDto(1L,null,null,null);
        Cart savedCart = this.cartService.save(CartMapper.toCartFrom(cartDto));

        assertThat(cartService.findById(savedCart.getId())).isNotNull();
    }

    @Test
    @DisplayName("Shouldn't find the cart, when the id does not exists")
    void findByIdThrowWhenIdNonExists() {
        CartDto cartDto = new CartDto(1L,null,null,null);
        Cart savedCart = this.cartService.save(CartMapper.toCartFrom(cartDto));
        Long nonExistent = 9999L;

        assertThrows(ResourceNotFoundException.class, () -> {
            this.cartService.findById(nonExistent);
        });
    }

    @Test
    @DisplayName("Should find all carts, when any cart is saved")
    void findAllSuccessWhenAnyCartIsSaved() {
        CartDto cartDto = new CartDto(1L,null,null,null);
        Cart savedCart = cartService.save(CartMapper.toCartFrom(cartDto));
        List<Cart> cartList = cartService.findAll();

        assertThat(cartList).size().isGreaterThan(0);
    }

    @Test
    @DisplayName("Should find nothing, when nothing is saved")
    void findAllWithZeroCartsSaved() {
        List<Cart> cartList = cartService.findAll();
        for (Cart cart : cartList){
            cartService.delete(cart.getId());
        }
        assertThat(cartList).size().isZero();
    }

    @Test
    @DisplayName("Should add an item to the Cart, when the item id and cart id exists, and there is sufficient quantity")
    void addItemSuccess() {
        ItemDto itemDto = new ItemDto(1L,"ItemTestAddItemSucess",10.00,10);
        Item item = itemService.save(ItemMapper.toItemFrom(itemDto));

        CartDto cartDto = new CartDto(1L,null,null,null);
        Cart cart = cartService.save(CartMapper.toCartFrom(cartDto));
        cart = cartService.addItem(cart.getId(), item.getId());

        assertThat(cart.getListItens()).isNotNull();
    }

    @Test
    @DisplayName("Should not add an item to the Cart, when there is not enough quantity")
    void addItemThrowsEnoughQuantity() {
        ItemDto itemDto = new ItemDto(1L,"ItemTestAddItemThrowsEnoughQuantity",10.00,0);
        Item item = itemService.save(ItemMapper.toItemFrom(itemDto));

        CartDto cartDto = new CartDto(1L,null,null,null);
        Cart cart = cartService.save(CartMapper.toCartFrom(cartDto));

        assertThrows(ResourceNotFoundException.class,()-> {
            cartService.addItem(cart.getId(), item.getId());
        });
    }

    @Test
    @DisplayName("Should not add an item to the Cart, when the item has already been added")
    void addItemThrowsItemAlreadyAdded() {
        ItemDto itemDto = new ItemDto(1L,"ItemTestAddItemThrowsAlreadyAdded",10.00,10);
        Item item = itemService.save(ItemMapper.toItemFrom(itemDto));

        CartDto cartDto = new CartDto(1L,null,null,null);
        Cart cart = cartService.save(CartMapper.toCartFrom(cartDto));
        this.cartService.addItem(cart.getId(), item.getId());

        assertThrows(ItemAlreadyAddedException.class,()-> {
            cartService.addItem(cart.getId(), item.getId());
        });
    }

    @Test
    @DisplayName("Should not add an item to the Cart, when the item id is wrong")
    void addItemThrowsWrongItemId() {
        ItemDto itemDto = new ItemDto(1L,"ItemTestAddItemThrowsWrongItemId",10.00,10);
        Item item = itemService.save(ItemMapper.toItemFrom(itemDto));

        CartDto cartDto = new CartDto(1L,null,null,null);
        Cart cart = cartService.save(CartMapper.toCartFrom(cartDto));
        this.cartService.addItem(cart.getId(), item.getId());

        Long wrongItemId = 999L;

        assertThrows(ResourceNotFoundException.class,()-> {
            cartService.addItem(wrongItemId, item.getId());
        });
    }

    @Test
    @DisplayName("Should not add an item to the Cart, when the cart id is wrong")
    void addItemThrowsWrongCartId() {
        ItemDto itemDto = new ItemDto(1L,"ItemTestAddItemThrowsWrongCartId",10.00,10);
        Item item = itemService.save(ItemMapper.toItemFrom(itemDto));

        CartDto cartDto = new CartDto(1L,null,null,null);
        Cart cart = cartService.save(CartMapper.toCartFrom(cartDto));
        this.cartService.addItem(cart.getId(), item.getId());

        Long wrongCartId = 999L;

        assertThrows(ResourceNotFoundException.class,()-> {
            cartService.addItem(cart.getId(), wrongCartId);
        });
    }

    @Test
    @DisplayName("Should remove an item to the Cart")
    void removeItemSuccess() {
        ItemDto itemDto = new ItemDto(1L,"ItemTestRemoveItemSucess",10.00,10);
        Item item = itemService.save(ItemMapper.toItemFrom(itemDto));

        CartDto cartDto = new CartDto(1L,null,null,null);
        Cart cart = cartService.save(CartMapper.toCartFrom(cartDto));
        cart= cartService.addItem(cart.getId(), item.getId());
        cart= cartService.removeItem(cart.getId(),item.getId());
        assertThat(cart.getListItens()).isEmpty();
    }

    @Test
    @DisplayName("Should not remove an item to the Cart, when item not found in cart")
    void removeItemThrowsItemId() {
        ItemDto itemDto = new ItemDto(1L,"ItemTestRemoveItemThrowsItemId",10.00,10);
        Item item = itemService.save(ItemMapper.toItemFrom(itemDto));
        Long wrongItemId = 999L;

        CartDto cartDto = new CartDto(1L,null,null,null);
        Cart cart = cartService.save(CartMapper.toCartFrom(cartDto));
        this.cartService.addItem(cart.getId(), item.getId());

        assertThrows(ResourceNotFoundException.class,() -> {
            cartService.removeItem(cart.getId(),wrongItemId);
        });
    }

    @Test
    @DisplayName("Should not remove an item from the Cart, when the cart is not found")
    void removeItemThrowsCartNotId() {
        ItemDto itemDto = new ItemDto(1L,"ItemTestRemoveItemThrowsCartId",10.00,10);
        Item item = itemService.save(ItemMapper.toItemFrom(itemDto));
        Long wrongCartId = 999L;

        CartDto cartDto = new CartDto(1L,null,null,null);
        Cart cart = cartService.save(CartMapper.toCartFrom(cartDto));
        this.cartService.addItem(cart.getId(), item.getId());

        assertThrows(ResourceNotFoundException.class,() -> {
            cartService.removeItem(wrongCartId,item.getId());
        });
    }


    @Test
    @DisplayName("Should delete a cart, when the cart id is found")
    void deleteSuccess() {
        CartDto cartDto = new CartDto(1L,null,null,null);
        Cart cart = cartService.save(CartMapper.toCartFrom(cartDto));
        cartService.delete(cart.getId());
        assertThrows(ResourceNotFoundException.class,()->{
            cartService.findById(cart.getId());
        });
    }

    @Test
    @DisplayName("Should not delete a cart, when the cart id is not found")
    void deleteThrowsIdItem() {
        CartDto cartDto = new CartDto(1L,null,null,null);
        Cart cart = cartService.save(CartMapper.toCartFrom(cartDto));
        Long wrongCartId = 999L;        ;
        assertThrows(ResourceNotFoundException.class,()->{
            cartService.delete(wrongCartId);
        });
    }
}