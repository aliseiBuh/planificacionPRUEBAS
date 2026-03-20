// package com.usic.planificacion.controllers;

// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;

// import com.usic.planificacion.model.entity.Area;
// import com.usic.planificacion.model.entity.Indicador;
// import com.usic.planificacion.model.entity.Meta;
// import com.usic.planificacion.model.entity.PorcentajeArea;
// import com.usic.planificacion.model.service.AreaService;
// import com.usic.planificacion.model.service.IndicadorService;
// import com.usic.planificacion.model.service.MetaService;
// import com.usic.planificacion.model.service.ObjetivoService;
// import com.usic.planificacion.model.service.PoliticaService;


// @Controller
// @RequiredArgsConstructor
// @RequestMapping("/laboratorios")
// public class PruebasController {
//     @Autowired
//       private final AreaService areaService;
//           @Autowired
//     private final PoliticaService politicaService;
//     private final ObjetivoService objetivoService;
//     private final IndicadorService indicadorService;
//     private final MetaService metaService;


//     @GetMapping("/pruebas")
//     public void calcularYGuardarPorcentaje(Area area, Integer gestion) {
//         // Obtener todos los indicadores del área
//         List<Indicador> indicadores = obtenerIndicadoresPorArea(area);

//         if (indicadores.isEmpty()) {
//             return; // No hay indicadores para esta área
//         }

//         double sumaPorcentajes = 0.0;
//         int contadorIndicadores = 0;

//         for (Indicador indicador : indicadores) {
//             // Buscar la meta correspondiente a esta gestión
//             Meta meta = indicador.getMetas().stream()
//                     .filter(m -> m.getGestion().equals(gestion) &&
//                             !"ELIMINADO".equals(m.getEstado()))
//                     .findFirst()
//                     .orElse(null);

//             if (meta != null && indicador.getTotal() != null && indicador.getTotal() != 0) {
//                 // Calcular porcentaje individual
//                 double porcentajeIndividual = 100*(meta.getResultado().doubleValue() /
//                         indicador.getTotal().doubleValue()) ;

//                 // Aplicar validación: si supera 100%, se considera 20%
//                 if (porcentajeIndividual > 100) {
//                     porcentajeIndividual = 20.0;
//                 }

//                 // Guardar en el indicador (totalDecimal)
//                 indicador.setTotalDecimal(porcentajeIndividual);
//                 indicadorDao.save(indicador);

//                 sumaPorcentajes += porcentajeIndividual;
//                 contadorIndicadores++;
//             }
//         }

//         // Calcular promedio final
//         if (contadorIndicadores > 0) {
//             double porcentajeFinal = sumaPorcentajes / contadorIndicadores;

//             // Buscar si ya existe un registro
//             Optional<PorcentajeArea> existente = porcentajeAreaDao
//                     .findByAreaAndGestion(area.getIdArea(), gestion);

//             PorcentajeArea porcentajeArea;
//             if (existente.isPresent()) {
//                 porcentajeArea = existente.get();
//             } else {
//                 porcentajeArea = new PorcentajeArea();
//                 porcentajeArea.setArea(area);
//                 porcentajeArea.setGestion(gestion);
//             }

//             porcentajeArea.setPorcentajeFinal(porcentajeFinal);
//             porcentajeArea.setCantidadIndicadores(contadorIndicadores);

//             porcentajeAreaDao.save(porcentajeArea);
//         }
//     }
    
// }
