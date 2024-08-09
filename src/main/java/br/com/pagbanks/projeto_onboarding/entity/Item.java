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
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "items_seq", sequenceName = "items_seq", allocationSize = 1)
    private Long id;
    private String nome;
    private double preco;
    @Column(name = "quantidade_estoque")
    private int quantidade;


    public Item(ItemDto itemDto) {
        this.nome = itemDto.nome();
        this.preco = itemDto.preco();
        this.quantidade = itemDto.quantidade();
    }
}
