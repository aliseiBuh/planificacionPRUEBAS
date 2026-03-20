package com.usic.planificacion.model.service;

import java.util.List;

import com.usic.planificacion.model.entity.Avance;

public interface AvanceService extends GenericoService <Avance, Long>{
    List<Avance> listarAvancesPorIdUnidad(Long IdUnidad);
}
