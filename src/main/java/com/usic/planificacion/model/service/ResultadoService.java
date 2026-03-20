package com.usic.planificacion.model.service;

import java.util.List;

import com.usic.planificacion.model.entity.Resultado;

public interface ResultadoService extends GenericoService <Resultado, Long>{
    Resultado resultadoPorIdIndicador(Long IdIndicador);
    List<Resultado> findAlll();
}
