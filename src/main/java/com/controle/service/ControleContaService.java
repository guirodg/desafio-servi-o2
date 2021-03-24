package com.controle.service;

import com.controle.dto.ControleContaRequest;
import com.controle.dto.ControleContaRequestLimite;
import com.controle.dto.ControleContaResponse;
import com.controle.externo.ContaExterno;
import com.controle.mapper.ControleMapper;
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

    public ControleContaResponse salvar(ControleContaRequest controleContaRequest) {

        ControleConta controleConta = ControleMapper.INSTANCE.toModel(controleContaRequest);
        controleContaRepository.save(controleConta);
        ControleContaResponse controleContaResponse = ControleMapper.INSTANCE.toDTO(controleConta);
        return controleContaResponse;
    }

    public ControleContaResponse atualizar(ControleContaRequest controleContaRequest) throws JsonProcessingException {
        Optional<ControleConta> controle = controleContaRepository.findById(controleContaRequest.getNumeroConta());

        if (controle.get().getLimeteSaque() <= 0) {
            int saldoDescontar = 0;
            if (controle.get().getTipoConta().equalsIgnoreCase("PF"))
                saldoDescontar = 10;
            if (controle.get().getTipoConta().equalsIgnoreCase("PJ"))
                saldoDescontar = 10;
            if (controle.get().getTipoConta().equalsIgnoreCase("GOV"))
                saldoDescontar = 20;

            ContaExterno contaExterna = ContaExterno.builder()
                    .numeroConta(controleContaRequest.getNumeroConta())
                    .agencia(controle.get().getAgencia())
                    .saldo(saldoDescontar)
                    .build();
            ObjectMapper objectMapper = new ObjectMapper();
            String convertObjetoString = objectMapper.writeValueAsString(contaExterna);
            kafkaTemplate.send("TOPIC_CONTA", convertObjetoString);
        }

        if (controle.isPresent()) {
            controle.get().setLimeteSaque(controle.get().getLimeteSaque() - 1);
            controleContaRepository.save(controle.get());
        }

        ControleContaResponse controleContaResponse = ControleMapper.INSTANCE.toDTO(controle.get());

        return controleContaResponse;
    }

    public ControleConta atualizarLimiteSaqueInicioDeMes(ControleContaRequestLimite controleContaRequestLimite) {
        Optional<ControleConta> controle = controleContaRepository.findById(controleContaRequestLimite.getNumeroConta());
        if (controle.isEmpty())
            throw new IllegalArgumentException("Não existe id do controle de conta");
        ControleConta controleConta = ControleMapper.INSTANCE.toModel(controleContaRequestLimite);
        controleConta.setAgencia(controle.get().getAgencia());
        controleConta.setCpfCliente(controle.get().getCpfCliente());
        controleConta.setTipoConta(controle.get().getTipoConta());
        return controleContaRepository.save(controleConta);
    }
}
