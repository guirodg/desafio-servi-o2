package com.controle.repository;

import com.controle.model.ControleConta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ControleContaRepository extends JpaRepository<ControleConta, String> {

    ControleConta findByCpfCliente(String cpf);
}
