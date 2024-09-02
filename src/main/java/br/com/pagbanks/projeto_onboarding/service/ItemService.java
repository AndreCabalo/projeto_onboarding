package br.com.pagbanks.projeto_onboarding.service;

import br.com.pagbanks.projeto_onboarding.entity.Item;
import br.com.pagbanks.projeto_onboarding.exceptions.ResourceNotFoundException;
import br.com.pagbanks.projeto_onboarding.repository.ItemRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
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
    public Item save(@Valid Item item) {
        log.info("m=save, msg=saving_item, item={}", item);
        if(item.getName() == null){
            throw new ResourceNotFoundException("Item name is null");
        }
        return itemRepository.save(item);
    }

    public List<Item> findAll() {
        List<Item> listItems = itemRepository.findAll();
        log.info("m=findAll, msg=findingAll_items, items={}", listItems);
        return listItems;
    }

    @Transactional
    public Item update(Long id, Item item) {
        log.info("m=update, msg=update_item, item={}", item);

        Item existingItem = itemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Item not found with id : " + id));

        if (item.getName() != null) {
            existingItem.setName(item.getName());
        }
        if (item.getAmount() != null) {
            existingItem.setAmount(item.getAmount());
        }
        if (item.getPrice() != null) {
            existingItem.setPrice(item.getPrice());
        }
        return itemRepository.save(existingItem);
    }

    @Transactional
    public void delete(Long id) {
        Optional<Item> optionalItem = itemRepository.findById(id);
        if (optionalItem.isPresent()) {
            itemRepository.delete(optionalItem.get());
            log.info("m=delete, msg=deleting_item, item={}", itemRepository.findById(id));
        } else {
            throw new ResourceNotFoundException("Item not deleted, because item id not found !");
        }
    }

    public Item findById(Long id) {
        Optional<Item> itemOptional = itemRepository.findById(id);
        log.info("m=findById, msg=findingById_item, item={}", itemOptional);
        if (itemOptional.isPresent()) {
            return itemOptional.get();
        } else {
            throw new ResourceNotFoundException("Item id not found, with id = " + id);
        }
    }

    @Transactional
    public Item addAmount(Long id, int amount) {
        log.info("m=addAmount, msg=addAmount_item, itemId={}, amount{}", id, amount);
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item id not found, with id = " + id));

        item.setAmount(item.getAmount() + amount);
        return itemRepository.save(item);
    }
}
