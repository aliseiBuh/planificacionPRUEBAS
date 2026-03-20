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
import com.usic.planificacion.model.entity.Politica;
import com.usic.planificacion.model.service.AreaService;
import com.usic.planificacion.model.service.PoliticaService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/politica")
public class PoliticaController {

    private final PoliticaService politicaService;
    private final AreaService areaService;

    @ValidarUsuarioAutenticado
    @GetMapping("/ventana")
    public String inicio() {
        return "politica/ventana";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/tablaRegistros")
    public String tablaRegistros(Model model){
        model.addAttribute("listaRegistros", politicaService.findAll());
        return "politica/tablaRegistros";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/formulario")
    public String formulario(Model model){
        model.addAttribute("objetoPolitica", new Politica());
        model.addAttribute("listaAreasEstrategicas", areaService.findAll());
        return "politica/formulario";
    }

    @PostMapping("/guardar")
    public ResponseEntity<String> registrar(@Validated Politica politica){
        politica.setEstado("ACTIVO");

        List<Politica> listaPoliticas = politicaService.listarPoliticaPorArea(politica.getArea().getIdArea());
        int total = politica.getPonderacion();
        for (Politica politica2 : listaPoliticas) {
            total = total + politica2.getPonderacion();
        }
        //System.out.println("total: " +total);
        if (total > 100) {
            return ResponseEntity.ok("Con este valor de ponderacion sobrepasa el 100 %");    
        }

        politicaService.save(politica);
        return ResponseEntity.ok("Se realizó el registro correctamente");
    }

    @PostMapping("/formularioEdit/{idPolitica}")
    public String formularioEdicion(Model model, @PathVariable Long idPolitica){
        model.addAttribute("objetoPolitica", politicaService.findById(idPolitica));
        model.addAttribute("listaAreasEstrategicas", areaService.findAll());
        model.addAttribute("edit", "true");
        return "politica/formulario";
    }

    @PostMapping("/eliminar/{idPolitica}")
    public ResponseEntity<String> eliminar(@PathVariable Long idPolitica){
        Politica politica = politicaService.findById(idPolitica);
        politica.setEstado("ELIMINADO");
        politicaService.save(politica);
        return ResponseEntity.ok("Registro Eliminado");
    }

}
