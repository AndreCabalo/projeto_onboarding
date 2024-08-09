package br.com.pagbanks.projeto_onboarding.entity;

import br.com.pagbanks.projeto_onboarding.dto.ItemDto;
import jakarta.persistence.*;
import lombok.*;


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
    private double price;
    private int amount;

    public Item(ItemDto itemDto) {
        this.name = itemDto.name();
        this.price = itemDto.price();
        this.amount = itemDto.amount();
    }

}
