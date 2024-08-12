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

    public List<Item> findAll() {
        List<Item> listItems = itemRepository.findAll().stream().toList();
        log.info("m=findAll,msg=findingAll_items, items={}", listItems);
        return itemRepository.findAll();
    }

    @Transactional
    public Item update(Item item) {
        log.info("m=update,msg=update_item, item={}", item);
        Optional<Item> optionalItem = itemRepository.findById(item.getId());
        if(optionalItem.isPresent()){
            return itemRepository.save(item);
        }else {
            throw new RuntimeException("Item not found!");
        }
    }


    @Transactional
    public void delete(Long id) {
        Optional<Item> optionalItem = itemRepository.findById(id);
        if(optionalItem.isPresent()){
         itemRepository.delete(optionalItem.get());
        }else {
            throw new RuntimeException("Item not found!");
        }
        log.info("m=delete,msg=deleting_item, item={}", itemRepository.findById(id));
    }



    public Item findById(Long id) {
        Optional<Item> itemOptional = itemRepository.findById(id);
        log.info("m=findById,msg=findingById_item, item={}", itemOptional);
        if (itemOptional.isPresent()) {
            return itemOptional.get();
        } else {
            throw new DataFoundException("Item não encontrado");
        }
    }

    public Item aumentaEstoque(Long id, int amount) {
        Item item = findById(id);
        item.setAmount(item.getAmount() + amount);
        return itemRepository.save(item);
    }


}
