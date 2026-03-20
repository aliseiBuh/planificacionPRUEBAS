package com.usic.planificacion.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.usic.planificacion.anotaciones.ValidarUsuarioAutenticado;
import com.usic.planificacion.model.entity.Objetivo;
import com.usic.planificacion.model.service.ObjetivoService;
import com.usic.planificacion.model.service.PoliticaService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/objetivo")
@RequiredArgsConstructor
public class objetivoController {

    private final ObjetivoService objetivoService;

    private final PoliticaService politicaService;

    @ValidarUsuarioAutenticado
    @GetMapping("/ventana")
    public String inicio() {
        return "objetivo/ventana";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/tablaRegistros")
    public String tablaRegistros(Model model) {
        model.addAttribute("objetivos", objetivoService.findAll());
        return "objetivo/tablaRegistros";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/formulario")
    public String formulario1(Model model) {
        model.addAttribute("objetivo", new Objetivo());
        model.addAttribute("politicas", politicaService.findAll());
        return "objetivo/formulario";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/formulario/{id}")
    public String formulario2(Model model, @PathVariable("id") Long idObjetivo) {
        model.addAttribute("objetivo", objetivoService.findById(idObjetivo));
        model.addAttribute("politicas", politicaService.findAll());
        model.addAttribute("edit", "true");
        return "objetivo/formulario";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/RegistrarObjetivo")
    public ResponseEntity<String> RegistrarPersona(HttpServletRequest request, @Validated Objetivo objetivo) {
        objetivo.setEstado("ACTIVO");

        List<Objetivo> listaObjetivos = objetivoService.listarObjetivoPorPolitica(objetivo.getPolitica().getIdPolitica());
        int total = objetivo.getPonderacion();
        for (Objetivo objetivo2 : listaObjetivos) {
            total = total + objetivo2.getPonderacion();
        }
        //System.out.println("total: " +total);
        if (total > 100) {
            return ResponseEntity.ok("Con este valor de ponderacion sobrepasa el 100 %");    
        }
        objetivoService.save(objetivo);
        return ResponseEntity.ok("Se realizó el registro correctamente");

    }

    @ValidarUsuarioAutenticado
    @PostMapping("/ModificarObjetivo")
    public ResponseEntity<String> ModificarPersona(HttpServletRequest request, @Validated Objetivo objetivo) {
        objetivo.setEstado("ACTIVO");
        List<Objetivo> listaObjetivos = objetivoService.listarObjetivoPorPolitica(objetivo.getPolitica().getIdPolitica());
        int total = objetivo.getPonderacion();
        for (Objetivo objetivo2 : listaObjetivos) {
            total = total + objetivo2.getPonderacion();
        }
        //System.out.println("total: " +total);
        if (total > 100) {
            return ResponseEntity.ok("Con este valor de ponderacion sobrepasa el 100 %");    
        }
        objetivoService.save(objetivo);
        return ResponseEntity.ok("Se realizó el registro correctamente");
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(Model model, @PathVariable("id") Long idObjetivo) {
        Objetivo objetivo = objetivoService.findById(idObjetivo);
        objetivo.setEstado("ELIMINADO");
        objetivoService.save(objetivo);
        return ResponseEntity.ok("Registro Eliminado");
    }
}
