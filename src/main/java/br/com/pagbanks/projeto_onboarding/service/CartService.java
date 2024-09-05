package br.com.pagbanks.projeto_onboarding.service;

import br.com.pagbanks.projeto_onboarding.dto.CartDto;
import br.com.pagbanks.projeto_onboarding.dto.ItemDto;
import br.com.pagbanks.projeto_onboarding.entity.Cart;
import br.com.pagbanks.projeto_onboarding.entity.Item;
import br.com.pagbanks.projeto_onboarding.exceptions.ItemAlreadyAddedException;
import br.com.pagbanks.projeto_onboarding.exceptions.ResourceNotFoundException;
import br.com.pagbanks.projeto_onboarding.mapper.CartMapper;
import br.com.pagbanks.projeto_onboarding.mapper.ItemMapper;
import br.com.pagbanks.projeto_onboarding.repository.CartRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ItemService itemService;

    @Transactional
    public CartDto save(CartDto cartDto) {
        log.info("m=save, msg=saving_cart, cart={}", cartDto);
        Cart cart = CartMapper.toCartFrom(cartDto);
        return CartMapper.toCartDtoFrom(cartRepository.save(cart));
    }

    @Transactional(readOnly = true)
    public CartDto findById(Long id) {
        Optional<Cart> cartOptional = cartRepository.findById(id);
        log.info("m=findById, msg=findById_carts, carts={}", cartOptional);
        if (cartOptional.isEmpty()) {
            throw new ResourceNotFoundException("Cart id not found!");
        }
        return CartMapper.toCartDtoFrom(cartOptional.get());
    }

    @Transactional(readOnly = true)
    public List<CartDto> findAll() {
        List<Cart> listCarts = cartRepository.findAll();
        log.info("m=findAll, msg=findAll_carts, carts={}", listCarts);
        return listCarts.stream().map(CartMapper::toCartDtoFrom).toList();
    }


    @Transactional
    public CartDto addItem(Long idCart, Long idItem) {
        Cart cart = cartRepository.findById(idCart).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        Item item = ItemMapper.toItemFrom(itemService.findById(idItem));
        if (item.getAmount() <= 0) {
            throw new ResourceNotFoundException("Insufficient quantity in stock");
        }
        if (cart.listItemsContains(item)) {
            throw new ItemAlreadyAddedException("Item already added to cart");
        }
        cart.addItem(item);
        item.decreasesAmount(1);
        itemService.save(ItemMapper.toItemDtoFrom(item));
        log.info("m=addItem, msg=addItem_carts, cart={} item={}", cart, item);
        return CartMapper.toCartDtoFrom(cartRepository.save(cart));
    }


    @Transactional
    public CartDto removeItem(Long idCart, Long idItem) {
        CartDto cartDto = findById(idCart);
        ItemDto itemDto = itemService.findById(idItem);
        Cart cart = CartMapper.toCartFrom(cartDto);
        Item item = ItemMapper.toItemFrom(itemDto);
        if (!cart.listItemsContains(item)) {
            throw new ResourceNotFoundException("Cannot remove item, because: Item not found in cart");
        }
        cart.removeItem(item);
        item.increasesAmount(1);
        itemService.save(ItemMapper.toItemDtoFrom(item));
        log.info("m=removeItem, msg=removeItem_carts, cart={} item={}", cart, item);
        return CartMapper.toCartDtoFrom(cartRepository.save(cart));

    }

    @Transactional
    public void delete(Long id) {
        Optional<Cart> optionalCart = cartRepository.findById(id);
        if (optionalCart.isEmpty()) {
            throw new ResourceNotFoundException("Cannot delete cart, because: Cart id not found");
        }
        cartRepository.delete(optionalCart.get());
    }

}
