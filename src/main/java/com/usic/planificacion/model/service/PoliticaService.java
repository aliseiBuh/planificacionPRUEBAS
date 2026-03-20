package com.usic.planificacion.model.service;
import java.util.List;

import com.usic.planificacion.model.entity.Politica;

public interface PoliticaService extends GenericoService <Politica, Long>{
    List<Politica> listarPoliticaPorArea(Long idArea);
    List<Politica> listarPoliticaPorIdPei(Long idPei);
}
