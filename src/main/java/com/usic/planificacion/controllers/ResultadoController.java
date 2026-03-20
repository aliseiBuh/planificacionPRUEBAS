package com.usic.planificacion.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.usic.planificacion.anotaciones.ValidarUsuarioAutenticado;
import com.usic.planificacion.model.entity.Resultado;
import com.usic.planificacion.model.service.IndicadorService;
import com.usic.planificacion.model.service.ResultadoService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/resultado")
@RequiredArgsConstructor
public class ResultadoController {

    private final ResultadoService resultadoService;
    private final IndicadorService indicadorService;

    @ValidarUsuarioAutenticado
    @GetMapping("/ventana")
    public String inicio() {
        return "resultado/ventana";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/tablaRegistros")
    public String tablaRegistros(Model model) {
        model.addAttribute("resultados", resultadoService.findAlll());
        return "resultado/tablaRegistros";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/formulario")
    public String formulario3(Model model) {
        model.addAttribute("resultado", new Resultado());
        model.addAttribute("indicadores", indicadorService.findAlll());
        return "resultado/formulario";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/formulario/{id}")
    public String formulario4(Model model, @PathVariable("id") Long idResultado) {
        model.addAttribute("resultado", resultadoService.findById(idResultado));
        model.addAttribute("indicadores", indicadorService.findAlll());
        model.addAttribute("edit", "true");
        return "resultado/formulario";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/RegistrarResultado")
    public ResponseEntity<String> RegistrarPersona(HttpServletRequest request, @Validated Resultado resultado) {
        resultado.setEstado("ACTIVO");
        resultadoService.save(resultado);
        return ResponseEntity.ok("Se realizó el registro correctamente");

    }

    @ValidarUsuarioAutenticado
    @PostMapping("/ModificarResultado")
    public ResponseEntity<String> ModificarResultado(HttpServletRequest request, @Validated Resultado resultado) {
        resultado.setEstado("ACTIVO");
        resultadoService.save(resultado);
        return ResponseEntity.ok("Se realizó el registro correctamente");
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(Model model, @PathVariable("id") Long idResultado) {
    Resultado resultado = resultadoService.findById(idResultado);
    resultado.setEstado("ELIMINADO");
    resultadoService.save(resultado);
    return ResponseEntity.ok("Registro Eliminado");
    }
}
