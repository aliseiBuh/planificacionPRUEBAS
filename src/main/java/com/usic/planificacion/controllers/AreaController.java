package com.usic.planificacion.controllers;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.usic.planificacion.anotaciones.ValidarUsuarioAutenticado;
import com.usic.planificacion.model.entity.Area;
import com.usic.planificacion.model.entity.Usuario;
import com.usic.planificacion.model.service.AreaService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/area")
public class AreaController {

    @Autowired
    private AreaService areaService; // TODO: Reemplazar por el servicio correspondiente

    @ValidarUsuarioAutenticado
    @GetMapping("/ventana")
    public String inicio() {
        return "area/ventana";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/tablaRegistros")
    public String tablaRegistros(Model model) {
        model.addAttribute("areas", areaService.findAll());
        return "area/tablaRegistros";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/formulario")
    public String formulario(Model model) {
        model.addAttribute("area", new Area());
        return "area/formulario";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/Registrar")
    public ResponseEntity<String> Registrar(@Validated Area area, HttpServletRequest request) {
        Usuario user = (Usuario) request.getSession().getAttribute("usuario");
        
        area.setRegistroIdUsuario(user.getIdUsuario());
        area.setRegistro(new Date());
        area.setEstado("ACTIVO");

        List<Area> listaAreas = areaService.listarAreasPorPei(area.getPei().getIdPei());
        int total = area.getPonderacion();
        for (Area area2 : listaAreas) {
            total = total + area2.getPonderacion();
        }
        //System.out.println("total: " +total);
        if (total > 100) {
            return ResponseEntity.ok("Con este valor de ponderacion sobrepasa el 100 %");    
        }
        areaService.save(area);

        return ResponseEntity.ok("Se realizó el registro correctamente");
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/formularioEdit/{id}")
    public String formulario(Model model, @PathVariable("id")Long id) {
        model.addAttribute("area", areaService.findById(id));
        model.addAttribute("edit", "true");
        return "area/formulario";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/Modificar")
    public ResponseEntity<String> Modificar(@Validated Area a, HttpServletRequest request) {
        Usuario user = (Usuario) request.getSession().getAttribute("usuario");

        Area area = areaService.findById(a.getIdArea());
        area.setNombre(a.getNombre());
        area.setCodigo(a.getCodigo());
        area.setPonderacion(a.getPonderacion());
        area.setModificacionIdUsuario(user.getIdUsuario());

        List<Area> listaAreas = areaService.listarAreasPorPeiExeptoIdArea(area.getPei().getIdPei(), area.getIdArea());
        int total = area.getPonderacion();
        for (Area area2 : listaAreas) {
            //System.out.println("ponderacion: " + area2.getPonderacion());
            total = total + area2.getPonderacion();
        }
        //System.out.println("total: " +total);
        if (total > 100) {
            return ResponseEntity.ok("Con este valor de ponderacion sobrepasa el 100 %");    
        }

        areaService.save(area);

        return ResponseEntity.ok("Se realizó el registro correctamente");
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(Model model, @PathVariable("id") Long id) {
        areaService.deleteById(id);
        return ResponseEntity.ok("Registro Eliminado");
    }
    
}
