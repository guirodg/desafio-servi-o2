package com.controle.controller;

import com.controle.model.ControleConta;
import com.controle.service.ControleContaService;
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
    public ResponseEntity<ControleConta> salvar(@RequestBody ControleConta controleConta) {
        return new ResponseEntity<>(controleContaService.salvar(controleConta), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity atualizar(@RequestBody ControleConta controleConta) {
        return new ResponseEntity<>(controleContaService.atualizar(controleConta), HttpStatus.NO_CONTENT);

    }
}
