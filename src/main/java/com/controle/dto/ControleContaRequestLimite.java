package com.controle.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ControleContaRequestLimite {
    private String cpf;
    private int numeroConta;
    private int limeteSaque;
}
