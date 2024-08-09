package br.com.pagbanks.projeto_onboarding.service;

import br.com.pagbanks.projeto_onboarding.entity.Item;
import br.com.pagbanks.projeto_onboarding.exceptions.DataFoundException;
import br.com.pagbanks.projeto_onboarding.repository.ItemRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Transactional
    public Item save(Item item) {
        log.info("m=save,msg=saving_item, item={}", item);
        return itemRepository.save(item);
    }

    @Transactional
    public Item update(Item item) {
        log.info("m=update,msg=update_item, item={}", item);
      var itemOptional = itemRepository.findById(item.getId());
        return itemRepository.save(item);
    }

    @Transactional
    public void delete(Long id) {
        Item item = findById(id);
        log.info("m=delete,msg=deleting_item, item={}", item);
        itemRepository.deleteById(id);
    }

    public List<Item> findAll() {
        List<Item> listItems = itemRepository.findAll().stream().toList();
        log.info("m=findAll,msg=findingAll_items, items={}", listItems);
        return itemRepository.findAll();
    }

    public Item findById(Long id) {
        Optional<Item> itemOptional = itemRepository.findById(id);
        log.info("m=findById,msg=findingById_item, item={}", itemOptional);
        if (itemOptional.isPresent()) {
            throw new DataFoundException("Item não encontrado");
        } else {
            return itemOptional.get();
        }
    }

    public Item aumentaEstoque(Long id, int amount) {
        Item item = findById(id);
        item.setAmount(item.getAmount() + amount);
        return itemRepository.save(item);
    }


}
