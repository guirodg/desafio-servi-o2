package com.controle.service;

import com.controle.externo.ContaExterno;
import com.controle.model.ControleConta;
import com.controle.repository.ControleContaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ControleContaService {
    private final ControleContaRepository controleContaRepository;

    public ControleConta salvar(ControleConta controleConta) {
        if (controleConta.getIdConta() <= 0 || controleConta.getLimeteSaque() <=0 || controleConta.getTipoConta().isEmpty())
            throw new IllegalArgumentException("Preencha todos os campos corretamente!");
        return controleContaRepository.save(controleConta);
    }

    public ControleConta atualizar(ControleConta controleConta) {
        Optional<ControleConta> controle = controleContaRepository.findById(controleConta.getIdConta());
        if (controle.isEmpty())
            throw new IllegalArgumentException("Não existe conta cadastrada");
        controle.get().setLimeteSaque(controle.get().getLimeteSaque() - 1);

        if(controle.get().getLimeteSaque() <= 0 && controle.get().getTipoConta().equals("pessoa fisica")){
            int saldoDescontar = 10;

            ContaExterno contaExterna = ContaExterno.builder().id(controleConta.getIdConta()).saldo(saldoDescontar).build();
            new RestTemplate().put("http://localhost:8080/contas/descontar",contaExterna);
        }

        if(controle.get().getLimeteSaque() <= 0 && controle.get().getTipoConta().equals("pessoa juridica")){
            int saldoDescontar = 10;

            ContaExterno contaExterna = ContaExterno.builder().id(controleConta.getIdConta()).saldo(saldoDescontar).build();
            new RestTemplate().put("http://localhost:8080/contas/descontar",contaExterna);
        }

        if(controle.get().getLimeteSaque() <= 0 && controle.get().getTipoConta().equals("governamental")){
            int saldoDescontar = 20;

            ContaExterno contaExterna = ContaExterno.builder().id(controleConta.getIdConta()).saldo(saldoDescontar).build();
            new RestTemplate().put("http://localhost:8080/contas/descontar",contaExterna);
        }

        return controleContaRepository.save(controle.get());
    }
}
