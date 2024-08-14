package br.com.pagbanks.projeto_onboarding.entity;

import br.com.pagbanks.projeto_onboarding.dto.ItemDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "items_seq", sequenceName = "items_seq", allocationSize = 1)
    private Long id;
    private String name;
    private Double price;
    private Integer amount;
    @ManyToMany(mappedBy = "listItens")
    @JsonIgnore
    private List<Cart> carts = new ArrayList<>();

    public Item(ItemDto itemDto) {
        this.name = itemDto.name();
        this.price = itemDto.price();
        this.amount = itemDto.amount();
    }


    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", amount=" + amount +
                '}';
    }
}
