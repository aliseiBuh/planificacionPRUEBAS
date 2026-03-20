package com.usic.planificacion.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usic.planificacion.model.entity.TechoPresupuestario;

public interface TechoPresupuestarioDao extends JpaRepository<TechoPresupuestario, Long>{
    
}
