package com.controle.service;

import com.controle.dto.ControleContaRequest;
import com.controle.dto.ControleContaRequestLimite;
import com.controle.dto.ControleContaResponse;
import com.controle.externo.ContaExterno;
import com.controle.mapper.ControleMapper;
import com.controle.model.ControleConta;
import com.controle.repository.ControleContaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ControleContaService {

    private final ControleContaRepository controleContaRepository;

    private final KafkaTemplate<String, ContaExterno> kafkaTemplate;

    public ControleContaResponse salvar(ControleContaRequest controleContaRequest) {
        ControleConta controleConta = ControleMapper.INSTANCE.toModel(controleContaRequest);
        controleContaRepository.save(controleConta);
        ControleContaResponse controleContaResponse = ControleMapper.INSTANCE.toDTO(controleConta);
        return controleContaResponse;
    }

    public ControleContaResponse atualizar(ControleContaRequest controleContaRequest) {
        ControleConta controle = controleContaRepository.findByCpfCliente(controleContaRequest.getCpfCliente());
        if (controle.getLimeteSaque() <= 0) {
            int saldoDescontar = 0;
            if (controle.getTipoConta().equalsIgnoreCase("PF"))
                saldoDescontar = 10;
            if (controle.getTipoConta().equalsIgnoreCase("PJ"))
                saldoDescontar = 10;
            if (controle.getTipoConta().equalsIgnoreCase("GOV"))
                saldoDescontar = 20;

            ContaExterno contaExterna = ContaExterno.builder()
                    .numeroConta(controleContaRequest.getNumeroConta())
                    .agencia(controle.getAgencia())
                    .saldo(saldoDescontar)
                    .build();
            kafkaTemplate.send("TOPIC_CONTA", contaExterna);
        }

        if (controle != null) {
            controle.setLimeteSaque(controle.getLimeteSaque() - 1);
            controleContaRepository.save(controle);
        }

        ControleContaResponse controleContaResponse = ControleMapper.INSTANCE.toDTO(controle);
        return controleContaResponse;
    }

    public ControleConta atualizarLimiteSaqueInicioDeMes(ControleContaRequestLimite controleContaRequestLimite) {
        ControleConta controle = controleContaRepository.findByCpfCliente(controleContaRequestLimite.getCpf());
        if (controle == null)
            throw new IllegalArgumentException("Não existe id do controle de conta");
        ControleConta controleConta = ControleMapper.INSTANCE.toModel(controleContaRequestLimite);
        controleConta.setAgencia(controle.getAgencia());
        controleConta.setCpfCliente(controle.getCpfCliente());
        controleConta.setTipoConta(controle.getTipoConta());
        return controleContaRepository.save(controleConta);
    }
}
