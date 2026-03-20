package com.usic.planificacion.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usic.planificacion.model.entity.CategoriaIndicador;

public interface CategoriaIndicadorDao extends JpaRepository<CategoriaIndicador, Long>{
    
}
