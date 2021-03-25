package com.controle.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class ControleConta {
    @Id
    private String cpfCliente;
    private int numeroConta;
    private int agencia;
    private int limeteSaque;
    private String tipoConta;
}
