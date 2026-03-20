package com.usic.planificacion.model.service;
import java.util.List;

import com.usic.planificacion.model.entity.Objetivo;

public interface ObjetivoService extends GenericoService <Objetivo, Long>{
    List<Objetivo> listarObjetivoPorPolitica(Long idPolitica);
    List<Objetivo> listarObjetivoPorIdPei(Long idPolitica);
    List<Objetivo> listarObjetivoActivos();
}
