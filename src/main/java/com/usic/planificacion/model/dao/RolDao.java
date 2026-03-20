package com.usic.planificacion.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usic.planificacion.model.entity.Rol;

public interface RolDao extends JpaRepository<Rol, Long>{
    Rol findByNombre (String nombre);
}
