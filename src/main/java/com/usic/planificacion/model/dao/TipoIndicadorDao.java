package com.usic.planificacion.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usic.planificacion.model.entity.TipoIndicador;

public interface TipoIndicadorDao extends JpaRepository<TipoIndicador, Long>{
    
}
