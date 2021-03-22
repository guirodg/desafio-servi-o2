package com.controle.externo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContaExterno {
    private int numeroConta;
    private int agencia;
    private double saldo;
}
