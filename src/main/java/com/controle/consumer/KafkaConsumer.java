package com.controle.consumer;

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
        System.out.println("Message = " + message);
        ObjectMapper objectMapper = new ObjectMapper();
        ControleConta jsonCapturaMsg = objectMapper.readValue(message, ControleConta.class);

        ControleConta build = ControleConta.builder().idConta(jsonCapturaMsg.getIdConta())
                .limeteSaque(jsonCapturaMsg.getLimeteSaque()).tipoConta(jsonCapturaMsg.getTipoConta()).build();

        controleContaService.salvar(build);
    }
}