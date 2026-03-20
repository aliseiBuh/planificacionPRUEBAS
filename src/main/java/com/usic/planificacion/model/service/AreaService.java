package com.usic.planificacion.model.service;

import java.util.List;

import com.usic.planificacion.model.entity.Area;

public interface AreaService extends GenericoService <Area, Long>{
    List<Area> listarAreasPorPei(Long idPei);
    List<Area> listarAreasPorPeiExeptoIdArea(Long idPei, Long idArea);
}
