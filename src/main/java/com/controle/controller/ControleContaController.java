package com.controle.controller;

import com.controle.dto.ControleContaRequest;
import com.controle.dto.ControleContaResponse;
import com.controle.service.ControleContaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("controle")
public class ControleContaController {
    private final ControleContaService controleContaService;

    @PostMapping
    public ResponseEntity<ControleContaResponse> salvar(@RequestBody ControleContaRequest controleContaRequest) {
        return new ResponseEntity<>(controleContaService.salvar(controleContaRequest), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<ControleContaResponse> atualizar(@RequestBody ControleContaRequest controleContaRequest) throws JsonProcessingException {
        return new ResponseEntity<>(controleContaService.atualizar(controleContaRequest), HttpStatus.NO_CONTENT);
    }
}
