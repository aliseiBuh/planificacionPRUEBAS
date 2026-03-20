package com.usic.planificacion.controllers;

import com.usic.planificacion.model.entity.Area;
import com.usic.planificacion.model.entity.Indicador;
import com.usic.planificacion.model.entity.Objetivo;
import com.usic.planificacion.model.entity.Politica;
import com.usic.planificacion.model.entity.Resultado;
import com.usic.planificacion.model.service.AreaService;
import com.usic.planificacion.model.service.IndicadorService;
import com.usic.planificacion.model.service.MetaService;
import com.usic.planificacion.model.service.ObjetivoService;
import com.usic.planificacion.model.service.PoliticaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/grafica")
public class GraficaController {

    private final AreaService areaService;
    private final PoliticaService politicaService;
    private final ObjetivoService objetivoService;
    private final IndicadorService indicadorService;
    private final MetaService metaService;

    @GetMapping("/ventana")
    public String inicio(Model model) {
        model.addAttribute("listaAreas", areaService.findAll());

        return "grafica/ventana";
    }
    
@GetMapping("/planificacion_anual")
public String areaVista(Model model) {
    model.addAttribute("area", new Area());
    model.addAttribute("listaAreas", areaService.findAll());
    model.addAttribute("listaMetas", metaService.listaMetasP());

    
        model.addAttribute("indicador", new Indicador());
                model.addAttribute("resultado", new Resultado());



    return "grafica/planificacion_anual";
}
    @PostMapping("/obtenerObjetivos/{idArea}")
    @ResponseBody
    public ResponseEntity<?> obtenerPoliticas(@PathVariable Long idArea) {
        List<Politica> lista = politicaService.listarPoliticaPorArea(idArea);
        return ResponseEntity.ok(lista);
    }

    @PostMapping("/obtenerObjetivosEstrategicos/{idObjetivo}")
    @ResponseBody
    public ResponseEntity<?> obtenerDatosObjetivoEstrategico(@PathVariable Long idObjetivo) {
        return ResponseEntity.ok(indicadorService.listarIndicadorPorObjetivo(idObjetivo));
    }

}
