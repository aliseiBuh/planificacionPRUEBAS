package com.usic.planificacion.model.dao;

import com.usic.planificacion.model.entity.PorcentajeArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface PorcentajeAreaDao extends JpaRepository<PorcentajeArea, Long> {
    
    @Query("SELECT p FROM PorcentajeArea p WHERE p.area.idArea = :idArea AND p.gestion = :gestion")
    Optional<PorcentajeArea> findByAreaAndGestion(@Param("idArea") Long idArea, @Param("gestion") Integer gestion);
    
    @Query("SELECT p FROM PorcentajeArea p WHERE p.gestion = :gestion ORDER BY p.area.codigo")
    List<PorcentajeArea> findByGestion(@Param("gestion") Integer gestion);
    
    @Query("SELECT DISTINCT p.gestion FROM PorcentajeArea p ORDER BY p.gestion")
    List<Integer> findAllGestiones();
// experimental-----------------------------------------------------------------------------------------------------
   
@Query(value = "SELECT i.total FROM indicador i " +
            "WHERE i.id_tipo_calculo IN (1,2) " +
            "ORDER BY i.id_indicador ASC", nativeQuery = true)
    List<Integer> listaTotalIndicadores();     //LISTA LOS TOTAL INDICADORES CUYO TIPO DE CALCULO DE METAS ANUALES ES 'SUMATORIO'

    @Query(value = "SELECT m.total FROM meta m WHERE m.id_indicador = :idIndicador AND m.gestion = :gestion", 
           nativeQuery = true)

List<Integer> listaMetaPorIndicador(@Param("idIndicador") Integer idIndicador, 
                                        @Param("gestion") Integer gestion);

}