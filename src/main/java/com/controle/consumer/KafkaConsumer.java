package com.controle.consumer;

import com.controle.dto.ControleContaRequest;
import com.controle.model.ControleConta;
import com.controle.service.ControleContaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class KafkaConsumer {
    private final ControleContaService controleContaService;

    @KafkaListener(topics = "TOPIC_BANCO", groupId = "GROUP_BANCO")
    public void consume(String message) throws JsonProcessingException {
        System.out.println("Conta cadastrada com sucesso! seu limite de saque = " + message);
        ObjectMapper objectMapper = new ObjectMapper();
        ControleContaRequest controleConta = objectMapper.readValue(message, ControleContaRequest.class);

//        ControleConta controleConta = ControleConta.builder().idConta(jsonCapturaMsg.getIdConta())
//                .limeteSaque(jsonCapturaMsg.getLimeteSaque()).tipoConta(jsonCapturaMsg.getTipoConta()).build();
        controleContaService.salvar(controleConta);
    }
}
