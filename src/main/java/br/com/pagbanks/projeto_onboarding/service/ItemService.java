package br.com.pagbanks.projeto_onboarding.service;

import br.com.pagbanks.projeto_onboarding.entity.Item;
import br.com.pagbanks.projeto_onboarding.exceptions.DataFoundException;
import br.com.pagbanks.projeto_onboarding.exceptions.QuantidadeIndisponivelException;
import br.com.pagbanks.projeto_onboarding.repository.ItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Transactional
    public Item save(Item item) {
        return itemRepository.save(item);
    }

    @Transactional
    public Item update(Item item) {
      var itemOptional = itemRepository.findById(item.getId());
        return itemRepository.save(item);
    }

    @Transactional
    public void delete(Long id) {
        Item item = findById(id);
        itemRepository.deleteById(id);
    }

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public Item findById(Long id) {
        Optional<Item> itemOptional = itemRepository.findById(id);
        if (itemOptional.isPresent()) {
            throw new DataFoundException("Item não encontrado");
        } else {
            return itemOptional.get();
        }
    }

    public Item aumentaEstoque(Long id, int quantidade) {
        Item item = findById(id);
        item.setQuantidadeEstoque(item.getQuantidadeEstoque() + quantidade);
        return itemRepository.save(item);
    }


}
