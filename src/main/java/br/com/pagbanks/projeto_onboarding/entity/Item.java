package br.com.pagbanks.projeto_onboarding.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "The name cannot be empty")
    private String name;
    private Double price;
    private Integer amount;
    @ManyToMany(mappedBy = "listItems")
    @JsonIgnore
    private List<Cart> carts = new ArrayList<>();

    public Item(Long id, String name, Double price, Integer amount) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.amount = amount;
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

    public Integer increasesAmount(Integer amount) {
        setAmount(getAmount() + 1);
        return getAmount();
    }

    public void decreasesAmount(Integer amount) {
        this.amount -= amount;
    }

    public boolean verifyNameNullability() {
        return this.name == null;
    }

    public void update(Item oldItem, Item updatedItem) {
        if (oldItem.getName() != null) {
            oldItem.setName(updatedItem.getName());
        }
        if (oldItem.getAmount() != null) {
            oldItem.setAmount(updatedItem.getAmount());
        }
        if (oldItem.getPrice() != null) {
            oldItem.setPrice(updatedItem.getPrice());
        }

    }


}
