package br.com.pagbanks.projeto_onboarding.entity;

import br.com.pagbanks.projeto_onboarding.dto.CartDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "carts")
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
    @JoinTable(name = "carts_items",joinColumns = @JoinColumn(name = "cart_id"), inverseJoinColumns = @JoinColumn(name = "item_id"))
    @JsonIgnoreProperties("carts")
    @JsonProperty("list_items")
    private Set<Item> listItens;
    @CreatedDate
    @Column(name = "creation_date")
    @JsonProperty("creation_date")
    private LocalDate creationDate;
    @Column(name = "total_value")
    @JsonProperty("total_value")
    private Double totalValue = 0.0;

//    public Cart(CartDto cartDto) {
////      this.listItens = cartDto.listItens() != null ? cartDto.listItens() : new HashSet<Item>();
//        this.listItens = cartDto.listItens();
//        this.creationDate = cartDto.creationDate() != null ? cartDto.creationDate() : LocalDate.now();
//        this.totalValue = cartDto.totalValue() != null ? cartDto.totalValue() : 0.0;
//    }

//    public static Cart from(CartDto cartDto) {
//        return new Cart(cartDto.id(), cartDto.listItens(), cartDto.creationDate(), cartDto.totalValue());
//    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", listItens=" + listItens +
                ", creationDate=" + creationDate +
                ", totalValue=" + totalValue +
                '}';
    }
}
