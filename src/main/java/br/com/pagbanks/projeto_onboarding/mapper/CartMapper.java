package br.com.pagbanks.projeto_onboarding.mapper;

import br.com.pagbanks.projeto_onboarding.dto.CartDto;
import br.com.pagbanks.projeto_onboarding.entity.Cart;

public class CartMapper {

    public static CartDto toCartDtoFrom(Cart cart) {
        return new CartDto(cart.getId(), cart.getListItems(), cart.getCreationDate(), cart.getTotalValue());
    }

    public static Cart toCartFrom(CartDto cartDto) {
        return new Cart(cartDto.id(), cartDto.listItems(), cartDto.creationDate(), cartDto.totalValue());
    }
}
