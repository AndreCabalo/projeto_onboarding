package br.com.pagbanks.projeto_onboarding.service;

import br.com.pagbanks.projeto_onboarding.entity.Carrinho;
import br.com.pagbanks.projeto_onboarding.entity.Item;
import br.com.pagbanks.projeto_onboarding.exceptions.CarrinhoNotFoundException;
import br.com.pagbanks.projeto_onboarding.exceptions.ItemJaEstaNoCarrinhoException;
import br.com.pagbanks.projeto_onboarding.exceptions.QuantidadeIndisponivelException;
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

    @Autowired
    private ItemService itemService;

    public Carrinho save(Carrinho carrinho) {
        return carrinhoRepository.save(carrinho);
    }

    public Carrinho findById(Long id) {
        return carrinhoRepository.findById(id)
                .orElseThrow(()->new CarrinhoNotFoundException("Carrinho não encontrado"));
    }

    public List<Carrinho> findAll() {
        return carrinhoRepository.findAll();
    }

    public Carrinho addItem(Long idCarrinho, Item item) {
        Carrinho carrinho = findById(idCarrinho);

        if(item.getQuantidade() <= 0) {
            throw new QuantidadeIndisponivelException("Quantidade insuficiente em estoque");
        }
        if (carrinho.getListaItens().contains(item)) {
            throw new ItemJaEstaNoCarrinhoException("Item já adicionado ao carrinho");
        }
        item.setQuantidade(item.getQuantidade() - 1);
        item.setQuantidade(item.getQuantidade() - 1);
        carrinho.getListaItens().add(item);
        carrinho.setValorTotal((carrinho.getValorTotal()) + item.getPreco());

        return carrinhoRepository.save(carrinho);
    }

    public Carrinho removeItem(Long idCarrinho, Item item) {
        Carrinho carrinho = findById(idCarrinho);
        if (carrinho.getListaItens().contains(item)) {
            carrinho.getListaItens().remove(item);
            carrinho.setValorTotal((carrinho.getValorTotal()) - item.getPreco());
            itemService.aumentaEstoque(item.getId(), 1);
//            item.setQuantidadeEstoque(item.getQuantidadeEstoque() + 1);
            return carrinhoRepository.save(carrinho);
        }else {
            throw new CarrinhoNotFoundException("Carrinho não encontrado, item não removido");
        }
    }


}
