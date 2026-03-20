package com.usic.planificacion.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usic.planificacion.model.entity.TipoCalculo;

public interface TipoCalculoDao extends JpaRepository<TipoCalculo, Long>{
    
}
