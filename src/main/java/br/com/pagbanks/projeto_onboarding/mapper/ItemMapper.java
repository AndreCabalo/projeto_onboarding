package br.com.pagbanks.projeto_onboarding.mapper;

import br.com.pagbanks.projeto_onboarding.dto.ItemDto;
import br.com.pagbanks.projeto_onboarding.entity.Item;

public class ItemMapper {

    public static ItemDto toItemDtoFrom(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getPrice(), item.getAmount());
    }

    public static Item toItemFrom(ItemDto itemDto) {
        return new Item(itemDto.id(), itemDto.name(), itemDto.price(), itemDto.amount());
    }
}
