package br.com.pagbanks.projeto_onboarding.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "carrinhos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Carrinho {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @SequenceGenerator(name = "carrinhos_seq", sequenceName = "carrinhos_seq", allocationSize = 1)
    private UUID id;
    @ManyToMany
    private List<Item> listaItens = new ArrayList<>();
    private LocalDateTime dataCriacao;
    private double valorTotal;

}
