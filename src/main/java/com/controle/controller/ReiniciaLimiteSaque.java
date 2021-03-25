package com.controle.controller;

import com.controle.dto.ControleContaRequestLimite;
import com.controle.model.ControleConta;
import com.controle.repository.ControleContaRepository;
import com.controle.service.ControleContaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableScheduling
@Slf4j
public class ReiniciaLimiteSaque {
    private final ControleContaRepository controleContaRepository;
    private final ControleContaService controleContaService;

    @Scheduled(fixedDelay = 43200000)
    public void verificaControleDeConta() {
        LocalDate ultimoDiaMes = LocalDate.now().withMonth(3).with(TemporalAdjusters.lastDayOfMonth());
        log.info("Realizando verificação a cada 1- minuto");

        List<ControleConta> controleContas = controleContaRepository.findAll();

        int limiteSaque = 0;
        int numeroConta = 0;
        String cpf = "";
        for (ControleConta conta : controleContas) {
            if (conta.getTipoConta().equalsIgnoreCase("PF")) {
                limiteSaque = 5;
                numeroConta = conta.getNumeroConta();
                cpf = conta.getCpfCliente();
            }

            if (conta.getTipoConta().equalsIgnoreCase("PJ")) {
                limiteSaque = 50;
                numeroConta = conta.getNumeroConta();
                cpf = conta.getCpfCliente();
            }

            if (conta.getTipoConta().equalsIgnoreCase("GOV")) {
                limiteSaque = 250;
                numeroConta = conta.getNumeroConta();
                cpf = conta.getCpfCliente();
            }
            ControleContaRequestLimite contaRequestLimite = ControleContaRequestLimite.builder()
                    .numeroConta(numeroConta)
                    .limeteSaque(limiteSaque)
                    .cpf(cpf)
                    .build();
            controleContaService.atualizarLimiteSaqueInicioDeMes(contaRequestLimite);
        }
    }
}
