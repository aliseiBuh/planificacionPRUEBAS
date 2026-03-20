package com.usic.planificacion.controllers;

import com.usic.planificacion.model.entity.Area;
import com.usic.planificacion.model.entity.Indicador;
import com.usic.planificacion.model.entity.Objetivo;
import com.usic.planificacion.model.entity.Politica;
import com.usic.planificacion.model.service.AreaService;
import com.usic.planificacion.model.service.IndicadorService;
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
public class Planificacion_anualController {
  @GetMapping("/planificacion_anual-vista")
    public String inicio(Model model) {
        return "grafica/planificacion_anual-vista";
    }
    
  
    
}
