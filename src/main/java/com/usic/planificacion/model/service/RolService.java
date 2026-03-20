package com.usic.planificacion.model.service;

import com.usic.planificacion.model.entity.Rol;

public interface RolService extends GenericoService <Rol, Long>{
    Rol buscaPorNombre(String nombre);
}
