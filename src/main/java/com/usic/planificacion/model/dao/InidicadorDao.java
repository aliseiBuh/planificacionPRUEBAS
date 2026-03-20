package com.usic.planificacion.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.usic.planificacion.model.entity.Indicador;

public interface InidicadorDao extends JpaRepository<Indicador, Long> {
    @Query("SELECT i FROM Indicador i WHERE i.objetivo.idObjetivo = ?1 and i.estado != 'ELIMINADO' order by i.codigo asc")
    List<Indicador> listarIndicadorPorObjetivo(Long idObjetivo);

    @Query("SELECT i FROM Indicador i WHERE i.objetivo.idObjetivo = ?1 and i.idIndicador != ?2 and i.estado != 'ELIMINADO'")
    List<Indicador> listarIndicadorPorObjetivoExeptoIdIndicador(Long idObjetivo, Long idIndicador);

    @Query(value = """
            select i.* from indicador i
            left join indicador_unidad iu ON iu.id_indicador = i.id_indicador
            left join unidad_funcional u on u.id_unidad_funcional = iu.id_unidad
            where u.id_unidad_funcional = ?1 and i."_estado" != 'ELIMINADO'
                 """, nativeQuery = true)
    List<Indicador> listarIndicadoresPorIdUnidad(Long idUnidad);

    @Query(value = """
            select i.* from indicador i
            where i."_estado" != 'ELIMINADO'
                 """, nativeQuery = true)
    List<Indicador> findAlll();

// ---------------------------------------------------------------------------------------------------------------------------------------------------

    @Query(value = "SELECT i.total FROM indicador i " +
            "WHERE i.id_tipo_calculo IN (1,2) " +
            "ORDER BY i.id_indicador ASC", nativeQuery = true)
    List<Integer> listaToraIndicadores();     //LISTA LOS TOTAL INDICADORES CUYO TIPO DE CALCULO DE METAS ANUALES ES 'SUMATORIO'

//     @Query(value = "SELECT i.total FROM indicador i " +
//             "WHERE i.id_tipo_calculo=3 " +
//             "ORDER BY i.id_indicador ASC", nativeQuery = true)
//     List<Integer> listaToraIndicadores();     //LISTA LOS TOTAL INDICADORES CUYO TIPO DE CALCULO DE METAS ANUALES ES 'SUMATORIO'


// }}
    }
