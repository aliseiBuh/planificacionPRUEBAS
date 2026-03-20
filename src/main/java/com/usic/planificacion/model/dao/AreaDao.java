package com.usic.planificacion.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.usic.planificacion.model.entity.Area;

public interface AreaDao extends JpaRepository<Area, Long> {

    @Query("SELECT a FROM Area a WHERE a.pei.idPei = ?1 and a.estado != 'ELIMINADO'")
    List<Area> listarAreasPorPei(Long idPei);

    @Query("SELECT a FROM Area a WHERE a.pei.idPei = ?1 and a.idArea != ?2 and a.estado != 'ELIMINADO'")
    List<Area> listarAreasPorPeiExeptoIdArea(Long idPei, Long idArea);

}
