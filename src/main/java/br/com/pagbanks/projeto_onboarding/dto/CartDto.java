package br.com.pagbanks.projeto_onboarding.dto;

import br.com.pagbanks.projeto_onboarding.entity.Item;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.CreatedDate;
import java.time.LocalDate;
import java.util.Set;

public record CartDto(

        Long id,
        @JsonProperty("list_items")
        Set<Item> listItens,
        @CreatedDate
        @JsonProperty("creation_date")
        LocalDate creationDate,
        @JsonProperty("total_value")
        Double totalValue

) {

        public CartDto {
                if (totalValue == null) {totalValue = 0.0;}
        }

}
