package com.controle.mapper;

import com.controle.dto.ControleContaRequest;
import com.controle.dto.ControleContaRequestLimite;
import com.controle.dto.ControleContaResponse;
import com.controle.model.ControleConta;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class ControleMapper {
    public static final ControleMapper INSTANCE = Mappers.getMapper(ControleMapper.class);

    public abstract ControleConta toModel(ControleContaRequest controleContaRequest);

    public abstract ControleConta toModel(ControleContaRequestLimite controleContaRequestLimite);

    public abstract ControleContaResponse toDTO(ControleConta controleConta);
}
