package com.controle.dto;

import lombok.Data;

@Data
public class ControleContaRequest {
    private String cpfCliente;
    private int numeroConta;
    private int agencia;
    private int limeteSaque;
    private String tipoConta;
}
