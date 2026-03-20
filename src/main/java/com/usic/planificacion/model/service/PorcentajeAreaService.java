package com.usic.planificacion.model.service;

import com.usic.planificacion.model.entity.PorcentajeArea;
import java.util.List;
import java.util.Map;

public interface PorcentajeAreaService {
    
    void calcularYGuardarPorcentajes();
    
    void calcularPorcentajesPorArea(Long idArea);
    
    void calcularPorcentajesPorGestion(Integer gestion);
    
    List<PorcentajeArea> obtenerPorcentajesPorGestion(Integer gestion);
    
    Map<String, List<Double>> obtenerDatosParaGrafico();

    List<Double> findTotalIndicador(Long idArea, Long idIndicador, Integer gestion);



    // List<Integer> listaTotalIndicadores();
    // List<Integer> listaMetaPorIndicador(Long id)
}