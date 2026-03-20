package com.usic.planificacion.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.usic.planificacion.model.dao.AreaDao;
import com.usic.planificacion.model.dao.InidicadorDao;
import com.usic.planificacion.model.dao.MetaDao;
import com.usic.planificacion.model.dao.PorcentajeAreaDao;
import com.usic.planificacion.model.entity.Area;
import com.usic.planificacion.model.entity.Indicador;
import com.usic.planificacion.model.entity.Meta;
import com.usic.planificacion.model.entity.Objetivo;
import com.usic.planificacion.model.entity.Politica;
import com.usic.planificacion.model.entity.PorcentajeArea;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PorcentajeAreaServiceImpl implements PorcentajeAreaService {

    @Autowired
    private AreaDao areaDao;

    @Autowired
    private InidicadorDao indicadorDao;

    @Autowired
    private MetaDao metaDao;

    @Autowired
    private PorcentajeAreaDao porcentajeAreaDao;





    

    // Metodo final de calculo
    @Override
    @Transactional
    public void calcularYGuardarPorcentajes() {
        // Obtener todas las áreas
        List<Area> areas = areaDao.findAll();

        // Gestiones a procesar
        List<Integer> gestiones = Arrays.asList(2021, 2022, 2023, 2024, 2025);

        for (Area area : areas) {
            for (Integer gestion : gestiones) {

                calcularYGuardarPorcentaje(area, gestion);

            }
        }
    }

    @Override
    @Transactional
    public void calcularPorcentajesPorArea(Long idArea) {

        Area area = areaDao.findById(idArea)
                .orElseThrow(() -> new RuntimeException("Área no encontrada"));

        List<Integer> gestiones = Arrays.asList(2021, 2022, 2023, 2024, 2025);

        for (Integer gestion : gestiones) {
            calcularYGuardarPorcentaje(area, gestion);
        }
    }

    @Override
    @Transactional
    public void calcularPorcentajesPorGestion(Integer gestion) {
        List<Area> areas = areaDao.findAll();

        for (Area area : areas) {
            calcularYGuardarPorcentaje(area, gestion);
        }

    }

    

    public void calcularYGuardarPorcentaje(Area area, Integer gestion) {
        // Obtener todos los indicadores del área
        List<Indicador> indicadores = obtenerIndicadoresPorArea(area);

        if (indicadores.isEmpty()) {
            return; // No hay indicadores para esta área
        }

        double sumaPorcentajes = 0.0;
        int contadorIndicadores = 0;

        for (Indicador indicador : indicadores) {
            // Buscar la meta correspondiente a esta gestión
            Meta meta = indicador.getMetas().stream()
                    .filter(m -> m.getGestion().equals(gestion) &&
                            !"ELIMINADO".equals(m.getEstado()))
                    .findFirst()
                    .orElse(null);

            if (meta != null && indicador.getTotal() != null && indicador.getTotal() != 0) {
                // Calcular porcentaje individual
                double porcentajeIndividual = 100*(meta.getResultado().doubleValue() /
                        indicador.getTotal().doubleValue()) ;

                // Aplicar validación: si supera 100%, se considera 20%
                if (porcentajeIndividual > 100) {
                    porcentajeIndividual = 20.0;
                }

                // Guardar en el indicador (totalDecimal)
                indicador.setTotalDecimal(porcentajeIndividual);
                indicadorDao.save(indicador);

                sumaPorcentajes += porcentajeIndividual;
                contadorIndicadores++;
            }
        }

        // Calcular promedio final
        if (contadorIndicadores > 0) {
            double porcentajeFinal = sumaPorcentajes / contadorIndicadores;

            // Buscar si ya existe un registro
            Optional<PorcentajeArea> existente = porcentajeAreaDao
                    .findByAreaAndGestion(area.getIdArea(), gestion);

            PorcentajeArea porcentajeArea;
            if (existente.isPresent()) {
                porcentajeArea = existente.get();
            } else {
                porcentajeArea = new PorcentajeArea();
                porcentajeArea.setArea(area);
                porcentajeArea.setGestion(gestion);
            }

            porcentajeArea.setPorcentajeFinal(porcentajeFinal);
            porcentajeArea.setCantidadIndicadores(contadorIndicadores);

            porcentajeAreaDao.save(porcentajeArea);
        }
    }



    private List<Indicador> obtenerIndicadoresPorArea(Area area) {
        // llama lista de indicadores
        List<Indicador> indicadores = indicadorDao.findAlll().stream()
                .filter(indicador -> "ACTIVO".equals(indicador.getEstado()))
                .collect(Collectors.toList());

        // Recorrer políticas del área
        for (Politica politica : area.getPoliticas()) {
            // Recorrer objetivos de cada política
            for (Objetivo objetivo : politica.getObjetivos()) {
                // Agregar todos los indicadores del objetivo
                indicadores.addAll(objetivo.getIndicadores());
            }
        }

        return indicadores;
    }

    @Override
    public List<PorcentajeArea> obtenerPorcentajesPorGestion(Integer gestion) {
        return porcentajeAreaDao.findByGestion(gestion);
    }

    // @Override
    // public Map<String, List<Double>> obtenerDatosParaGrafico() {
    //     Map<String, List<Double>> datos = new HashMap<>();

    //     // Obtener todas las áreas
    //     List<Area> areas = areaDao.findAll();
    //     List<Integer> gestiones = Arrays.asList(2021, 2022, 2023, 2024, 2025);

    //     for (Area area : areas) {
    //         List<Double> porcentajes = new ArrayList<>();

    //         for (Integer gestion : gestiones) {
    //             Optional<PorcentajeArea> porcentaje = porcentajeAreaDao
    //                     .findByAreaAndGestion(area.getIdArea(), gestion);

    //             porcentajes.add(porcentaje.map(PorcentajeArea::getPorcentajeFinal)
    //                     .orElse(0.0));
    //         }

    //         datos.put(area.getNombre(), porcentajes);
    //     }

    //     return datos;
    // }
    @Override
public Map<String, List<Double>> obtenerDatosParaGrafico() {
    // Verificar si hay datos en la base de datos
    List<PorcentajeArea> registros = porcentajeAreaDao.findAll();
    
    if (registros.isEmpty()) {
        System.out.println("No hay registros en porcentaje_area, calculando...");
        calcularYGuardarPorcentajes();
    }
    
    Map<String, List<Double>> datos = new HashMap<>();
    List<Area> areas = areaDao.findAll();
    List<Integer> gestiones = Arrays.asList(2021, 2022, 2023, 2024, 2025);

    for (Area area : areas) {
        List<Double> porcentajes = new ArrayList<>();

        for (Integer gestion : gestiones) {
            Optional<PorcentajeArea> porcentaje = porcentajeAreaDao
                    .findByAreaAndGestion(area.getIdArea(), gestion);

            // Si no existe, calcularlo
            if (!porcentaje.isPresent()) {
                calcularYGuardarPorcentaje(area, gestion);
                porcentaje = porcentajeAreaDao.findByAreaAndGestion(area.getIdArea(), gestion);
            }

            porcentajes.add(porcentaje.map(PorcentajeArea::getPorcentajeFinal)
                    .orElse(0.0));
        }

        datos.put(area.getNombre(), porcentajes);
    }

    return datos;
}

    @Override
    public List<Double> findTotalIndicador(Long idArea, Long idIndicador, Integer gestion) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findTotalIndicador'");
    }
// ---------------------------------------------------------------------------------------------------------------------
//     @Override
//     public List<Integer> listaTotalIndicadores() {
//         List<Integer> listaIndica= porcentajeAreaDao.listaTotalIndicadores();
//                 int i = 0;

//        for (Integer lista : listaTotalIndicadores()) {
//         i++;
//         System.out.println("mmmmmmmmmmmmmmmmmmmmmmmmmmmmmm"+lista);
       
//     }
//        return listaIndica;
// }
}