package br.com.pagbanks.projeto_onboarding.entity;

import br.com.pagbanks.projeto_onboarding.dto.CartDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "carts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "carts_seq", sequenceName = "carts_seq", allocationSize = 1)
    private Long id;
    @ManyToMany
    @Column(name = "list_items")
    @JsonProperty("list_items")
    private Set<Item> listItens = new HashSet<Item>();
    @Column(name = "creation_date")
    @JsonProperty("creation_date")
    private LocalDate creationDate;
    @Column(name = "total_value")
    @JsonProperty("total_value")
    private Double totalValue = 0.0;

    public Cart(CartDto cartDto) {
        this.listItens = cartDto.listItens() != null ? cartDto.listItens() : new HashSet<Item>();
        this.creationDate = cartDto.creationDate() != null ? cartDto.creationDate() : LocalDate.now();
        this.totalValue = cartDto.totalValue() != null ? cartDto.totalValue() : 0.0;
    }

//    @PostLoad
//    @PostPersist
//    @PostUpdate
//    private void ensureCreationDate(){
//        if(this.creationDate == null) {
//            this.creationDate = LocalDate.now();
//        }
//    }

}
