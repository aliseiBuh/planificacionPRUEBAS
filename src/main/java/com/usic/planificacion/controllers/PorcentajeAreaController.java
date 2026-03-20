package com.usic.planificacion.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.usic.planificacion.model.entity.Area;
import com.usic.planificacion.model.service.AreaService;
import com.usic.planificacion.model.service.PorcentajeAreaService;

import java.util.Map;
import java.util.List;


@Controller
@RequestMapping("/porcentajes")
public class PorcentajeAreaController {

    @Autowired
    private PorcentajeAreaService porcentajeAreaService;
@Autowired
private AreaService areaService;



   
    @GetMapping("/planificacion_anual")
public String areaVista(Model model) {
    model.addAttribute("area", new Area());
    model.addAttribute("listaAreas", areaService.findAll());


    

    return "grafica/planificacion_anual";
}

    @PostMapping("/calcular")
    @ResponseBody
    public ResponseEntity<String> calcularPorcentajes() {
        
        try {
            porcentajeAreaService.calcularYGuardarPorcentajes();
            return ResponseEntity.ok("Porcentajes calculados exitosamente");
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }

        
    }

    // @GetMapping("/datos-grafico")
    // @ResponseBody
    // public ResponseEntity<Map<String, List<Double>>> obtenerDatosGrafico() {
    //     // Map<String, List<Double>> datos = porcentajeAreaService.obtenerDatosParaGrafico();
    //     // return ResponseEntity.ok(datos);


    //     Map<String, List<Double>> datos = porcentajeAreaService.obtenerDatosParaGrafico();
    
    // // Si no hay datos, calcular primero
    // if (datos.isEmpty() || datos.values().stream().allMatch(List::isEmpty)) {
    //     porcentajeAreaService.calcularYGuardarPorcentajes();
    //     datos = porcentajeAreaService.obtenerDatosParaGrafico();
    // }
    
    // return ResponseEntity.ok(datos);

    // }
//     @GetMapping("/datos-grafico")
// @ResponseBody
// public ResponseEntity<Map<String, List<Double>>> obtenerDatosGrafico() {
//     // Obtener datos existentes
//     Map<String, List<Double>> datos = porcentajeAreaService.obtenerDatosParaGrafico();
    
//     // Si el Map está vacío o todas las listas tienen solo ceros
//     if (datos.isEmpty()) {
//         System.out.println("Controller: No hay datos, ejecutando cálculo...");
//         porcentajeAreaService.calcularYGuardarPorcentajes();
//         datos = porcentajeAreaService.obtenerDatosParaGrafico();
//     } else {
//         // Verificar si todos los valores son cero
//         boolean todosCeros = datos.values().stream()
//             .allMatch(lista -> lista.stream().allMatch(val -> val == null || val == 0.0));
        
//         if (todosCeros) {
//             System.out.println("Controller: Todos los valores son cero, recalculando...");
//             porcentajeAreaService.calcularYGuardarPorcentajes();
//             datos = porcentajeAreaService.obtenerDatosParaGrafico();
//         }
//     }
    
//     return ResponseEntity.ok(datos);
// }
    
// @GetMapping("/datos-grafico")
// @ResponseBody
// public ResponseEntity<Map<String, List<Double>>> obtenerDatosGrafico() {
//     // Ejecutar cálculo antes de obtener datos
//     porcentajeAreaService.calcularYGuardarPorcentajes();
    
//     // Obtener datos actualizados
//     Map<String, List<Double>> datos = porcentajeAreaService.obtenerDatosParaGrafico();
    
//     return ResponseEntity.ok(datos);
// }

@GetMapping("/datos-grafico-actualizados")
@ResponseBody
public ResponseEntity<Map<String, List<Double>>> obtenerDatosGraficoActualizados() {
    try {
        // 1. Calcular porcentajes
        porcentajeAreaService.calcularYGuardarPorcentajes();
        
        // 2. Obtener datos calculados
        Map<String, List<Double>> datos = porcentajeAreaService.obtenerDatosParaGrafico();
        
        // 3. Verificar que hay datos
        if (datos.isEmpty()) {
            return ResponseEntity.status(500).body(null);
        }
        
        System.out.println("Datos para gráfico generados:");
        datos.forEach((area, porcentajes) -> {
            System.out.println(area + ": " + porcentajes);
        });
        
        return ResponseEntity.ok(datos);
    } catch (Exception e) {
        System.err.println("Error en obtenerDatosGraficoActualizados: " + e.getMessage());
        e.printStackTrace();
        return ResponseEntity.status(500).body(null);
    }
}

}