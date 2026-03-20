package com.usic.planificacion.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.usic.planificacion.model.entity.Resultado;

public interface ResultadoDao extends JpaRepository<Resultado, Long>{
    @Query("SELECT r FROM Resultado r WHERE r.indicador.idIndicador = ?1 AND r.estado != 'ELIMINADO'")
    Resultado resultadoPorIdIndicador(Long IdIndicador);

    

    @Query(value = """
            select i.* from resultado i
            where i."_estado" != 'ELIMINADO'
                 """, nativeQuery = true)
    List<Resultado> findAlll();


}
