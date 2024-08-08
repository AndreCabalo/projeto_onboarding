package br.com.pagbanks.projeto_onboarding.entity;

import br.com.pagbanks.projeto_onboarding.dto.ItemDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;


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
    private UUID id;
    private String nome;
    private double preco;
    private int quantidadeEstoque;


    public Item(ItemDto itemDto) {
        this.nome = itemDto.nome();
        this.preco = itemDto.preco();
        this.quantidadeEstoque = itemDto.quantidadeEstoque();
    }
}
