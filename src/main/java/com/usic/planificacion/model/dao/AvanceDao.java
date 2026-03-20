package com.usic.planificacion.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.usic.planificacion.model.entity.Avance;

public interface AvanceDao extends JpaRepository<Avance, Long> {

    @Query("select a from Avance a where a.unidadFuncional.idUnidadFuncional = ?1 and a.estado != 'ELIMINADO'")
    List<Avance> listarAvancesPorIdUnidad(Long IdUnidad);
    
}
