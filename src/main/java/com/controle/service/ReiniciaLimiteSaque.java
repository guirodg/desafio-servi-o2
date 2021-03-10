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
import java.util.List;
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
        LocalDate ultimoDiaMes = LocalDate.now().withMonth(3).with(TemporalAdjusters.lastDayOfMonth());
        log.info("Realizando verificação a cada 1- minuto");

        List<ControleConta> controleContas = controleContaRepository.findAll();
        long count = controleContas.stream().count();

        for (int i = 1; i < count + 1; i++) {
            Optional<ControleConta> pegaIdControle = controleContaRepository.findById((long) i);
            if (pegaIdControle.isEmpty())
                return;

            int limiteSaque = 0;
            String tipo = "";
            if (pegaIdControle.get().getTipoConta().equals("pessoa fisica")) {
                limiteSaque = 5;
                tipo = "pessoa fisica";
            }
            if (pegaIdControle.get().getTipoConta().equals("pessoa juridica")) {
                limiteSaque = 50;
                tipo = "pessoa juridica";
            }
            if (pegaIdControle.get().getTipoConta().equals("governamental")) {
                limiteSaque = 250;
                tipo = "governamental";
            }

            if (pegaIdControle.get().getLimeteSaque() <= 0 && LocalDate.now().equals(ultimoDiaMes)) {
                ControleConta controleConta = ControleConta.builder()
                        .idConta((long) i)
                        .limeteSaque(limiteSaque)
                        .tipoConta(tipo)
                        .build();
                log.info("Limite renovado da conta - pessoa fisica {}", i);
                controleContaService.atualizarLimiteSaqueInicioDeMes(controleConta);
            }
        }
    }
}
