package com.usic.planificacion.model.service;

import java.util.List;

import com.usic.planificacion.model.entity.Meta;

public interface MetaService extends GenericoService <Meta, Long>{
    List<Meta> listarMetasPorIndicador(Long idIndicador);
    List<Meta> listaMetasP();
}
