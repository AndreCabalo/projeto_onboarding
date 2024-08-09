package br.com.pagbanks.projeto_onboarding.service;

import br.com.pagbanks.projeto_onboarding.entity.Cart;
import br.com.pagbanks.projeto_onboarding.entity.Item;
import br.com.pagbanks.projeto_onboarding.exceptions.CarrinhoNotFoundException;
import br.com.pagbanks.projeto_onboarding.exceptions.ItemJaEstaNoCarrinhoException;
import br.com.pagbanks.projeto_onboarding.exceptions.QuantidadeIndisponivelException;
import br.com.pagbanks.projeto_onboarding.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ItemService itemService;

    public Cart save(Cart cart) {
        return cartRepository.save(cart);
    }

    public Cart findById(Long id) {
        return cartRepository.findById(id)
                .orElseThrow(()->new CarrinhoNotFoundException("Cart not found"));
    }

    public List<Cart> findAll() {
        return cartRepository.findAll();
    }

    public Cart addItem(Long idCart, Item item) {
        Cart cart = findById(idCart);

        if(item.getAmount() <= 0) {
            throw new QuantidadeIndisponivelException("Insufficiente quantity in stock");
        }
        if (cart.getListItens().contains(item)) {
            throw new ItemJaEstaNoCarrinhoException("Item already added to cart");
        }
        item.setAmount(item.getAmount() - 1);
        item.setAmount(item.getAmount() - 1);
        cart.getListItens().add(item);
        cart.setTotalValue((cart.getTotalValue()) + item.getPrice());

        return cartRepository.save(cart);
    }

    public Cart removeItem(Long idCart, Item item) {
        Cart cart = findById(idCart);
        if (cart.getListItens().contains(item)) {
            cart.getListItens().remove(item);
            cart.setTotalValue((cart.getTotalValue()) - item.getPrice());
            itemService.aumentaEstoque(item.getId(), 1);
            return cartRepository.save(cart);
        }else {
            throw new CarrinhoNotFoundException("Item not found in cart");
        }
    }


}
