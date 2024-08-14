package br.com.pagbanks.projeto_onboarding.service;

import br.com.pagbanks.projeto_onboarding.entity.Cart;
import br.com.pagbanks.projeto_onboarding.entity.Item;
import br.com.pagbanks.projeto_onboarding.exceptions.CarrinhoNotFoundException;
import br.com.pagbanks.projeto_onboarding.exceptions.DataFoundException;
import br.com.pagbanks.projeto_onboarding.exceptions.ItemJaEstaNoCarrinhoException;
import br.com.pagbanks.projeto_onboarding.exceptions.QuantidadeIndisponivelException;
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
        log.info("m=save,msg=saving_cart, cart={}", cart);
        return cartRepository.save(cart);
    }

    public Cart findById(Long id) {
        Optional<Cart> cartOptional = cartRepository.findById(id);
        log.info("m=findById,msg=findById_carts, carts={}",cartOptional);
        if (cartOptional.isPresent()){
            return cartOptional.get();
        } else {
            throw new DataFoundException("Cart id not found!");
        }
    }

    public List<Cart> findAll() {
        List<Cart> listCarts = cartRepository.findAll().stream().toList();
        log.info("m=findAll,msg=findindAll_carts, carts={}",listCarts);
        return cartRepository.findAll();
    }

    public Cart addItem(Long idCart, Long idItem) {
        Cart cart = findById(idCart);
        Item item = itemService.findById(idItem);
        if(item.getAmount() <= 0) {
            throw new QuantidadeIndisponivelException("Insufficiente quantity in stock");
        }else{
            if (cart.getListItens().contains(item)) {
                throw new ItemJaEstaNoCarrinhoException("Item already added to cart");
            }else {
                item.setAmount(item.getAmount() - 1);
                item.setAmount(item.getAmount() - 1);
                cart.getListItens().add(item);
                cart.setTotalValue((cart.getTotalValue()) + item.getPrice());
                return cartRepository.save(cart);
            }
        }
    }

    @Transactional
    public Cart removeItem(Long idCart, Long idItem) {
        Cart cart = findById(idCart);
        Item item = itemService.findById(idItem);
        if (cart.getListItens().contains(item)) {
            cart.getListItens().remove(item);
            cart.setTotalValue((cart.getTotalValue()) - item.getPrice());
            itemService.addAmount(item.getId(), 1);
            return cartRepository.save(cart);
        }else {
            throw new CarrinhoNotFoundException("Item not found in cart");
        }
    }

    @Transactional
    public void delete(Long id){
        Optional<Cart> optionalCart = cartRepository.findById(id);
        if(optionalCart.isPresent()){
            cartRepository.delete(optionalCart.get());
        }else {
            throw new RuntimeException("Cart not found!");
        }
    }

}
