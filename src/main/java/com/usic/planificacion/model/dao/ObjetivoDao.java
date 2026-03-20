package com.usic.planificacion.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.usic.planificacion.model.entity.Objetivo;

public interface ObjetivoDao extends JpaRepository<Objetivo, Long> {

    @Query("SELECT ob FROM Objetivo ob WHERE ob.politica.idPolitica = ?1 and ob.estado != 'ELIMINADO' ORDER BY ob.codigo ASC")
    List<Objetivo> listarObjetivoPorPolitica(Long idPolitica);

    @Query("SELECT ob FROM Objetivo ob WHERE ob.estado != 'ELIMINADO'")
    List<Objetivo> listarObjetivoActivos();

    @Query(value = """
                    select o.* FROM objetivo o
            LEFT JOIN politica p ON p.id_politica = o.id_politica
            LEFT JOIN area a ON a.id_area = p.id_area
            LEFT JOIN pei pe ON pe.id_pei = a.id_pei
            WHERE o."_estado" != 'ELIMINADO' and pe.id_pei = ?1 order by o.codigo asc;
                """, nativeQuery = true)
    List<Objetivo> listarObjetivoPorIdPei(Long idPolitica);

}
