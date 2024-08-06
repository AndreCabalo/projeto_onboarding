package br.com.pagbanks.projeto_onboarding.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;


@Entity
@Table(name = "items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @SequenceGenerator(name = "items_seq", sequenceName = "items_seq", allocationSize = 1)
    private UUID id;
    private String nome;
    private double preco;
    private int quantidadeEstoque;

}
