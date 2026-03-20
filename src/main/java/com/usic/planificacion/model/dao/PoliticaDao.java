package com.usic.planificacion.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.usic.planificacion.model.entity.Politica;

public interface PoliticaDao extends JpaRepository<Politica, Long>{
    
    @Query("SELECT p FROM Politica p WHERE p.area.idArea = ?1 and p.estado != 'ELIMINADO' ORDER BY p.codigo ASC")
    List<Politica> listarPoliticaPorArea(Long idArea);  //lista de politicas que pertenezcan a idArea

    @Query("SELECT p FROM Politica p WHERE p.area.pei.idPei = ?1 and p.estado != 'ELIMINADO'")
    List<Politica> listarPoliticaPorIdPei(Long idPei);
}
