package com.usic.planificacion.model.service;

import java.util.List;

import com.usic.planificacion.model.entity.UnidadFuncional;

public interface UnidadFuncionalService extends GenericoService <UnidadFuncional, Long>{
    UnidadFuncional buscarUnidadPorNombre(String nombre);
    List<UnidadFuncional> listarUnidadPorIdIndicador(Long idIndicador);
}
