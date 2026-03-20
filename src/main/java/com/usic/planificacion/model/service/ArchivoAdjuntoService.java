package com.usic.planificacion.model.service;

import java.util.List;

import com.usic.planificacion.model.entity.ArchivoAdjunto;

public interface ArchivoAdjuntoService extends GenericoService <ArchivoAdjunto, Long>{
    List<ArchivoAdjunto> listarArchivosPorIdAvance(Long idAvance);
}
