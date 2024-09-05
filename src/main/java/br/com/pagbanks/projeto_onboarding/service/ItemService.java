package br.com.pagbanks.projeto_onboarding.service;

import br.com.pagbanks.projeto_onboarding.dto.ItemDto;
import br.com.pagbanks.projeto_onboarding.entity.Item;
import br.com.pagbanks.projeto_onboarding.exceptions.ResourceNotFoundException;
import br.com.pagbanks.projeto_onboarding.mapper.ItemMapper;
import br.com.pagbanks.projeto_onboarding.repository.ItemRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;


    @Transactional
    public ItemDto save(@Valid ItemDto itemDto) {
        log.info("m=save, msg=saving_item, item={}", itemDto);
        Item item = ItemMapper.toItemFrom(itemDto);
        if (item.verifyNameNullability()) {
            throw new ResourceNotFoundException("Item name is null");
        }
        return ItemMapper.toItemDtoFrom(itemRepository.save(item));

    }

    @Transactional(readOnly = true)
    public ItemDto findById(Long id) {
        Optional<Item> itemOptional = itemRepository.findById(id);
        log.info("m=findById, msg=findingById_item, item={}", itemOptional);
        if (itemOptional.isEmpty()) {
            throw new ResourceNotFoundException("Item id not found, with id = " + id);
        }
        return ItemMapper.toItemDtoFrom(itemOptional.get());
    }

    @Transactional(readOnly = true)
    public List<ItemDto> findAll() {
        List<Item> listItems = itemRepository.findAll();
        log.info("m=findAll, msg=findingAll_items, items={}", listItems);
        return listItems.stream().map(ItemMapper::toItemDtoFrom).toList();
    }

    @Transactional
    public ItemDto update(Long id, ItemDto itemDto) {
        log.info("m=update, msg=update_item, item={}", itemDto);
        Optional<Item> existingItem = itemRepository.findById(id);
        if (existingItem.isEmpty()) {
            throw new ResourceNotFoundException("Item not found with id : " + id);
        }
        Item item = ItemMapper.toItemFrom(itemDto);
        item.update(existingItem.get(), item);
        return ItemMapper.toItemDtoFrom(itemRepository.save(existingItem.get()));
    }

    @Transactional
    public void delete(Long id) {
        Optional<Item> optionalItem = itemRepository.findById(id);
        if (optionalItem.isEmpty()) {
            throw new ResourceNotFoundException("Item not deleted, because item id not found !");
        }
        itemRepository.delete(optionalItem.get());
        log.info("m=delete, msg=deleting_item, item={}", itemRepository.findById(id));
    }

    @Transactional
    public ItemDto addAmount(Long id, int amount) {
        log.info("m=addAmount, msg=addAmount_item, itemId={}, amount{}", id, amount);
        Optional<Item> optionalItem = itemRepository.findById(id);
        if (optionalItem.isEmpty()) {
            throw new ResourceNotFoundException("Item id not found, with id = " + id);
        }
        optionalItem.get().increasesAmount(amount);
        return ItemMapper.toItemDtoFrom(itemRepository.save(optionalItem.get()));
    }
}
