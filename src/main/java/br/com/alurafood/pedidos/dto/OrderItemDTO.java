package br.com.alurafood.pedidos.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {

    private Long id;
    private Integer quantity;
    private String description;
}
