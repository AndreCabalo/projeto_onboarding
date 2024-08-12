package br.com.pagbanks.projeto_onboarding.controller;

import br.com.pagbanks.projeto_onboarding.dto.ItemDto;
import br.com.pagbanks.projeto_onboarding.entity.Item;
import br.com.pagbanks.projeto_onboarding.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemService itemService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public Item saveItem(@RequestBody @Valid ItemDto itemDto) {
        return itemService.save(new Item(itemDto));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Item> list(){
        return itemService.findAll().stream().toList();
    }

//    @PutMapping
//    @Transactional
//    public Item update(@RequestBody @Valid Item item){
//        return itemService.update(item);
//    }

    @PutMapping
    @Transactional
    public Item update(@RequestBody @Valid Item item){
        return itemService.update(item.getId(), item);
    }


    @DeleteMapping("delete/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long itemId){
        itemService.delete(itemId);
    }

    @GetMapping("/find/{itemId}")
    public ResponseEntity<Item> getItemById(@PathVariable Long itemId) {
        return ResponseEntity.ok(itemService.findById((itemId)));
    }

}
