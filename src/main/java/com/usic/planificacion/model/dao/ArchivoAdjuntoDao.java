package com.usic.planificacion.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.usic.planificacion.model.entity.ArchivoAdjunto;

public interface ArchivoAdjuntoDao extends JpaRepository<ArchivoAdjunto, Long>{
    @Query("SELECT a FROM ArchivoAdjunto a WHERE a.avance.idAvance = ?1 and a.estado != 'ELIMINADO'")
    List<ArchivoAdjunto> listarArchivosPorIdAvance(Long idAvance);
}
