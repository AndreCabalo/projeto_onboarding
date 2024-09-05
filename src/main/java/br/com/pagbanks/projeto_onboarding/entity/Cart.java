package br.com.pagbanks.projeto_onboarding.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "carts")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "carts_seq", sequenceName = "carts_seq", allocationSize = 1)
    private Long id;
    @ManyToMany
    @JoinTable(name = "carts_items", joinColumns = @JoinColumn(name = "cart_id"), inverseJoinColumns = @JoinColumn(name = "item_id"))
    @JsonIgnoreProperties("carts")
    @JsonProperty("list_items")
    private Set<Item> listItems;
    @CreatedDate
    @Column(name = "creation_date")
    @JsonProperty("creation_date")
    private LocalDate creationDate;

    @Transient
    private Double totalValue = 0.0;

    public Double getTotalValue() {
        if (listItems == null) {
            return totalValue = 0.0;
        } else {
            return totalValue = listItems.stream().mapToDouble(Item::getPrice).sum();
        }
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", listItems=" + listItems +
                ", creationDate=" + creationDate +
                ", totalValue=" + totalValue +
                '}';
    }

    public boolean listItemsContains(Object item) {
        return listItems != null && listItems.contains(item);
    }

    public void removeItem(Object item) {
        listItems.stream().filter(i -> i.equals(item)).findFirst().ifPresent(listItems::remove);
        totalValue -= ((Item) item).getPrice();
    }

    public void addItem(Object item) {
        listItems.add((Item) item);
        totalValue += ((Item) item).getPrice();
    }


}
