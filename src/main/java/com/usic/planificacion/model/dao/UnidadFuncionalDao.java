package com.usic.planificacion.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.usic.planificacion.model.entity.UnidadFuncional;

public interface UnidadFuncionalDao extends JpaRepository<UnidadFuncional, Long> {
    @Query("SELECT u FROM UnidadFuncional u WHERE u.nombre = ?1 and u.estado != 'ELIMINADO'")
    UnidadFuncional buscarUnidadPorNombre(String nombre);

    @Query("""
             SELECT u FROM UnidadFuncional u
            JOIN u.indicadores i
            WHERE i.idIndicador = ?1 AND u.estado != 'ELIMINADO'
            """)
    List<UnidadFuncional> listarUnidadPorIdIndicador(Long idIndicador);

}
