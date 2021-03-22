package com.controle.dto;

import lombok.Data;

@Data
public class ControleContaResponse {
    private String cpfCliente;
    private int numeroConta;
    private int agencia;
    private int limeteSaque;
    private String tipoConta;
}
