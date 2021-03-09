package com.controle.service;

import com.controle.model.ControleConta;
import com.controle.repository.ControleContaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Optional;

@RequiredArgsConstructor
@Configuration
@EnableScheduling
@Slf4j
public class ReiniciaLimiteSaque {
    private final ControleContaRepository controleContaRepository;
    private final ControleContaService controleContaService;

    @Scheduled(fixedDelay = 60000)
    public void verificaControleDeConta() {
        LocalDate ultimoDia = LocalDate.now().withMonth(3).with(TemporalAdjusters.lastDayOfMonth());
        log.info("Realizando verificação a cada 1- minuto");

        for (int i = 1; i < 7; i++) {
            Optional<ControleConta> pegaIdControle = controleContaRepository.findById((long) i);
            if (pegaIdControle.isEmpty())
                return;

            if (pegaIdControle.get().getLimeteSaque() <= 0
                    && LocalDate.now().equals(LocalDate.parse("2021-03-09"))
                    && pegaIdControle.get().getTipoConta().equals("pessoa fisica")) {

                ControleConta controleConta = ControleConta.builder()
                        .idConta((long) i)
                        .limeteSaque(5)
                        .tipoConta("pessoa fisica")
                        .build();
                log.info("Limite renovado da conta {}", i);
                controleContaService.atualizarLimiteSaqueInicioDeMes(controleConta);
            }
        }
    }
}
