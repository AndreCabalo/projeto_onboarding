package br.com.pagbanks.projeto_onboarding.controller;

import br.com.pagbanks.projeto_onboarding.dto.ItemDto;
import br.com.pagbanks.projeto_onboarding.entity.Item;
import br.com.pagbanks.projeto_onboarding.mapper.ItemMapper;
import br.com.pagbanks.projeto_onboarding.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping
    public ResponseEntity<Item> saveItem(@RequestBody @Valid ItemDto itemDto) {
        var item = itemService.save(ItemMapper.toItemFrom(itemDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    @GetMapping
    public ResponseEntity<List<Item>> list(){
        return ResponseEntity.ok(itemService.findAll());
    }

    @PutMapping
    public ResponseEntity<Item> update(@RequestBody @Valid ItemDto itemdto){
        var item = itemService.update(itemdto.id(), ItemMapper.toItemFrom(itemdto));
        return ResponseEntity.ok(item);
    }

    @PutMapping("/add/{itemId}/{amount}")
    public ResponseEntity<Item> addAmount(@PathVariable Long itemId, @PathVariable int amount){
        var item = itemService.addAmount(itemId, amount);
        return ResponseEntity.ok(item);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> delete(@PathVariable Long itemId){
        itemService.delete(itemId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Item> getItemById(@PathVariable Long itemId) {
        Item item = itemService.findById(itemId);
        return ResponseEntity.ok(item);
    }

}
