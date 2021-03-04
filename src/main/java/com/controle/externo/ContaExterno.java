package com.controle.externo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContaExterno {
    private Long id;
    private double saldo;
}
