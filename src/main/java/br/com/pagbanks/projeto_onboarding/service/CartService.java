package br.com.pagbanks.projeto_onboarding.service;

import br.com.pagbanks.projeto_onboarding.entity.Cart;
import br.com.pagbanks.projeto_onboarding.entity.Item;
import br.com.pagbanks.projeto_onboarding.exceptions.ItemAlreadyAddedException;
import br.com.pagbanks.projeto_onboarding.exceptions.ResourceNotFoundException;
import br.com.pagbanks.projeto_onboarding.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Cart save(Cart cart) {
        log.info("m=save, msg=saving_cart, cart={}", cart);
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart findById(Long id) {
        Optional<Cart> cartOptional = cartRepository.findById(id);
        log.info("m=findById, msg=findById_carts, carts={}", cartOptional);
        if (cartOptional.isPresent()) {
            return cartOptional.get();
        } else {
            throw new ResourceNotFoundException("Cart id not found!");
        }
    }

    public List<Cart> findAll() {
        List<Cart> listCarts = cartRepository.findAll();
        log.info("m=findAll, msg=findindAll_carts, carts={}", listCarts);
        return listCarts;
    }

    @Transactional
    public Cart addItem(Long idCart, Long idItem) {
        Cart cart = findById(idCart);
        Item item = itemService.findById(idItem);
        if (item.getAmount() <= 0) {
            throw new ResourceNotFoundException("Insufficient quantity in stock");
        } else {
            if (cart.getListItens().contains(item)) {
                throw new ItemAlreadyAddedException("Item already added to cart");
            } else {
                item.setAmount(item.getAmount() - 1);
                cart.getListItens().add(item);
                cart.setTotalValue(cart.getTotalValue() + item.getPrice());
                log.info("m=addItem, msg=addItem_carts, cart={} item={}", cart, item);
                return cartRepository.save(cart);
            }
        }
    }

    @Transactional
    public Cart removeItem(Long idCart, Long idItem) {
        Cart cart = findById(idCart);
        Item item = itemService.findById(idItem);
        if (cart.getListItens().contains(item)) {
            Item cartItem = cart.getListItens().stream().filter(i -> i.getId().equals(idItem)).findFirst().get();
            cart.getListItens().remove(cartItem);
            cart.setTotalValue((cart.getTotalValue()) - item.getPrice());
            log.info("m=removeItem, msg=removeItem_carts, cart={} item={}", cart, item);
            itemService.addAmount(item.getId(), 1);
            return cartRepository.save(cart);
        } else {
            throw new ResourceNotFoundException("Cannot remove item, because: Item not found in cart");
        }
    }

    @Transactional
    public void delete(Long id) {
        Optional<Cart> optionalCart = cartRepository.findById(id);
        if (optionalCart.isPresent()) {
            cartRepository.delete(optionalCart.get());
        } else {
            throw new ResourceNotFoundException("Cannot delete cart, because: Cart id not found");
        }
    }

}
