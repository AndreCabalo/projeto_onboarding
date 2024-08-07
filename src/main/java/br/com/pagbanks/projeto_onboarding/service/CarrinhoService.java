package br.com.pagbanks.projeto_onboarding.service;

import br.com.pagbanks.projeto_onboarding.model.Carrinho;
import br.com.pagbanks.projeto_onboarding.model.Item;
import br.com.pagbanks.projeto_onboarding.repository.CarrinhoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CarrinhoService {

    @Autowired
    private CarrinhoRepository carrinhoRepository;

    public Carrinho save(Carrinho carrinho) {
        return carrinhoRepository.save(carrinho);
    }

    public Optional<Carrinho> findById(UUID id) {
        return carrinhoRepository.findById(id);
    }

    public List<Carrinho> findAll() {
        return carrinhoRepository.findAll();
    }

    public Carrinho addItem(UUID idCarrinho, Item Item) {
        Optional<Carrinho> OptionalCarrinho = carrinhoRepository.findById(idCarrinho);
        if (OptionalCarrinho.isPresent()) {
            Carrinho carrinho = OptionalCarrinho.get();
            carrinho.getListaItens().add(Item);
            carrinho.setValorTotal((carrinho.getValorTotal()) + Item.getPreco());
            return carrinhoRepository.save(carrinho);
        }
        return null;
    }

    public Carrinho removeItem(UUID idCarrinho, Item item) {
        Optional<Carrinho> optionalCarrinho = carrinhoRepository.findById(idCarrinho);
        if (optionalCarrinho.isPresent()) {
            Carrinho carrinho = optionalCarrinho.get();
            carrinho.getListaItens().remove(item);
            carrinho.setValorTotal((carrinho.getValorTotal()) - item.getPreco());
            return carrinhoRepository.save(carrinho);
        }
        return null;
    }
}
