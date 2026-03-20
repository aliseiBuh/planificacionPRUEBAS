package com.usic.planificacion.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

import com.usic.planificacion.anotaciones.ValidarUsuarioAutenticado;
import com.usic.planificacion.model.entity.Persona;
import com.usic.planificacion.model.entity.TechoPresupuestario;
import com.usic.planificacion.model.entity.Usuario;
import com.usic.planificacion.model.service.PresupuestoService;
import com.usic.planificacion.model.service.TechoPresupuestarioService;
import com.usic.planificacion.model.service.UnidadFuncionalService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDate;
import java.util.Date;

@Controller
@RequestMapping("/techoPresupuestario")
@RequiredArgsConstructor
public class TechoPresupuestarioController {

    private final PresupuestoService presupuestoService;

    private final TechoPresupuestarioService techoPresupuestarioService;

    private final UnidadFuncionalService unidadFuncionalService;

    @ValidarUsuarioAutenticado
    @GetMapping("/ventana")
    public String inicio() {
        return "techoPresupuestario/ventana";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/formulario")
    public String formulario(Model model) {
        model.addAttribute("techoPresupuestario", new TechoPresupuestario());
        model.addAttribute("unidades", unidadFuncionalService.findAll());
        return "techoPresupuestario/formulario";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/tablaRegistros")
    public String tablaRegistros(Model model) {
        model.addAttribute("techosPresupuestarios", techoPresupuestarioService.findAll());
        return "techoPresupuestario/tablaRegistros";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/RegistrarPresupuestario")
    public ResponseEntity<String> RegistrarPresupuestario(HttpServletRequest request,
            @Validated TechoPresupuestario techoPresupuestario) {
        try {
            String gestion = String.valueOf(LocalDate.now().getYear());
            
            techoPresupuestario.setGestion(Integer.parseInt(gestion));
            techoPresupuestario.setRegistro(new Date());
            techoPresupuestario.setEstado("A");
            techoPresupuestario.setMontoEjecutado(0.00);
            techoPresupuestarioService.save(techoPresupuestario);

            return ResponseEntity.ok("Se realizó el registro correctamente");
        } catch (Exception e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/ModificarPresupuestario")
    public ResponseEntity<String> ModificarPresupuestario(HttpServletRequest request,
            @Validated TechoPresupuestario techo) {
        try {
            TechoPresupuestario techoPresupuestario = techoPresupuestarioService.findById(techo.getIdTechoPresupuesto());
            techoPresupuestario.setUnidadFuncional(techo.getUnidadFuncional());
            techoPresupuestario.setMontoAsignado(techo.getMontoAsignado());
            techoPresupuestarioService.save(techoPresupuestario);

            return ResponseEntity.ok("Se realizó el registro correctamente");
        } catch (Exception e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(Model model, @PathVariable("id") Long idPerson) {
        
        return ResponseEntity.ok("Registro Eliminado");
    }

}
