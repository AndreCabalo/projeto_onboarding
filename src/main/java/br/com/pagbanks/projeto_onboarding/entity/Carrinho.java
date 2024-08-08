package br.com.pagbanks.projeto_onboarding.entity;

import br.com.pagbanks.projeto_onboarding.dto.CarrinhoDto;
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
@EqualsAndHashCode(of = "id")
public class Carrinho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "carrinhos_seq", sequenceName = "carrinhos_seq", allocationSize = 1)
    @Column(name = "IDT_CARRINHO")
    private UUID id;

    @ManyToMany
    private List<Item> listaItens = new ArrayList<>();
    @Column(name = "DAT_CREATION")
    private LocalDateTime dataCriacao;

    private double valorTotal;

    public Carrinho(CarrinhoDto carrinhoDto) {
        this.listaItens = carrinhoDto.listaItens();
        this.dataCriacao = carrinhoDto.dataCriacao();
        this.valorTotal = carrinhoDto.valorTotal();
    }

}
