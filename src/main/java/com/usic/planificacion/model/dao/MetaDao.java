package com.usic.planificacion.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.usic.planificacion.model.entity.Meta;

public interface MetaDao extends JpaRepository<Meta, Long>{
     @Query("SELECT m FROM Meta m WHERE m.indicador.idIndicador = ?1 and m.estado != 'ELIMINADO' ORDER BY m.idMeta ASC")
    List<Meta> listarMetasPorIndicador(Long idIndicador);

    @Query("SELECT m FROM Meta m ORDER BY m.idMeta ASC")
    List<Meta> listaMetasP();

    

    // @Query("select m.resultado from Meta m WHERE m.id_indicador=?1 and gestion=?2 ")
    // List<Integer> totalMeta; 


}
