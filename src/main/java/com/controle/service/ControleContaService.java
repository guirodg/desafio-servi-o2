package com.controle.service;

import com.controle.externo.ContaExterno;
import com.controle.model.ControleConta;
import com.controle.repository.ControleContaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ControleContaService {
    private final ControleContaRepository controleContaRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public ControleConta salvar(ControleConta controleConta) {
        if (controleConta.getIdConta() <= 0 || controleConta.getLimeteSaque() <= 0 || controleConta.getTipoConta().isEmpty())
            throw new IllegalArgumentException("Preencha todos os campos corretamente!");
        return controleContaRepository.save(controleConta);
    }

    public ControleConta atualizar(ControleConta controleConta) throws JsonProcessingException {
        Optional<ControleConta> controle = controleContaRepository.findById(controleConta.getIdConta());
        if (controle.isEmpty())
            throw new IllegalArgumentException("Não existe conta cadastrada");
        controle.get().setLimeteSaque(controle.get().getLimeteSaque() - 1);

        if (controle.get().getLimeteSaque() <= 0 && controle.get().getTipoConta().equals("pessoa fisica")) {
            int saldoDescontar = 10;
            ContaExterno contaExterna = ContaExterno.builder().id(controleConta.getIdConta()).saldo(saldoDescontar).build();
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonContaExterna = objectMapper.writeValueAsString(contaExterna);
            kafkaTemplate.send("TOPIC_CONTA", jsonContaExterna);
        }

        if (controle.get().getLimeteSaque() <= 0 && controle.get().getTipoConta().equals("pessoa juridica")) {
            int saldoDescontar = 10;
            ContaExterno contaExterna = ContaExterno.builder().id(controleConta.getIdConta()).saldo(saldoDescontar).build();
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonContaExterna = objectMapper.writeValueAsString(contaExterna);
            kafkaTemplate.send("TOPIC_CONTA", jsonContaExterna);
        }

        if (controle.get().getLimeteSaque() <= 0 && controle.get().getTipoConta().equals("governamental")) {
            int saldoDescontar = 20;
            ContaExterno contaExterna = ContaExterno.builder().id(controleConta.getIdConta()).saldo(saldoDescontar).build();
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonContaExterna = objectMapper.writeValueAsString(contaExterna);
            kafkaTemplate.send("TOPIC_CONTA", jsonContaExterna);
        }
        return controleContaRepository.save(controle.get());
    }

    public ControleConta atualizarLimiteSaqueInicioDeMes(ControleConta controleConta) {
        Optional<ControleConta> controle = controleContaRepository.findById(controleConta.getIdConta());
        if (controle.isEmpty())
            throw new IllegalArgumentException("Não existe id do controle de conta");

        return controleContaRepository.save(controleConta);
    }
}
