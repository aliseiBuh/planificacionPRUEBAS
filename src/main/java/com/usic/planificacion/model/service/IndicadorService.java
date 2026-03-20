package com.usic.planificacion.model.service;

import java.util.List;

import com.usic.planificacion.model.entity.Indicador;

public interface IndicadorService extends GenericoService <Indicador, Long>{
    List<Indicador> listarIndicadorPorObjetivo(Long idObjetivo);
    List<Indicador> listarIndicadorPorObjetivoExeptoIdIndicador(Long idObjetivo, Long idIndicador);
    List<Indicador> listarIndicadoresPorIdUnidad(Long idUnidad);
    List<Indicador> findAlll();
}
