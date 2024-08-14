package br.com.pagbanks.projeto_onboarding.entity;

import br.com.pagbanks.projeto_onboarding.dto.ItemDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "items_seq", sequenceName = "items_seq", allocationSize = 1)
    private Long id;
    private String name;
    private Double price;
    private Integer amount;
    @ManyToMany(mappedBy = "listItens")
    private List<Cart> carts = new ArrayList<>();

    public Item(ItemDto itemDto) {
        this.name = itemDto.name();
        this.price = itemDto.price();
        this.amount = itemDto.amount();
    }

//    public void updateItem(Item item){
//        if(item.getName() != null){
//            this.name = item.getName();
//        }
//        if (item.getAmount() != null) {
//            this.amount = item.getAmount();
//        }
//        if(item.getPrice() != null){
//            this.price = item.getPrice();
//        }
//    }

}
