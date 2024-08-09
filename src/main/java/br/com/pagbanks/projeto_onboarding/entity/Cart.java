package br.com.pagbanks.projeto_onboarding.entity;

import br.com.pagbanks.projeto_onboarding.dto.CartDto;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carrinhos")
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
    private List<Item> listItens = new ArrayList<>();
    @Column(name = "creation_date")
    private LocalDateTime creationDate;
    @Column(name = "total_value")
    private double totalValue;

    public Cart(CartDto cartDto) {
        this.listItens = cartDto.listItens();
        this.creationDate = cartDto.creationDate();
        this.totalValue = cartDto.totalValue();
    }

}
