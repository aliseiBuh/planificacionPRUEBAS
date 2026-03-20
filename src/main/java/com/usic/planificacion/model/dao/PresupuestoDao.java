package com.usic.planificacion.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usic.planificacion.model.entity.Presupuesto;

public interface PresupuestoDao extends JpaRepository<Presupuesto, Long>{
    
}
