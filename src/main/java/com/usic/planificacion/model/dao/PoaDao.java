package com.usic.planificacion.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usic.planificacion.model.entity.Poa;

public interface PoaDao extends JpaRepository<Poa, Long>{
    
}
