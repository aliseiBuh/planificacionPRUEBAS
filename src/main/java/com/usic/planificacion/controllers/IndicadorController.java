package com.usic.planificacion.controllers;

import java.util.ArrayList;
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
import com.usic.planificacion.model.entity.CategoriaIndicador;
import com.usic.planificacion.model.entity.Indicador;
import com.usic.planificacion.model.entity.Meta;
import com.usic.planificacion.model.entity.Objetivo;
import com.usic.planificacion.model.entity.Pei;
import com.usic.planificacion.model.entity.TipoCalculo;
import com.usic.planificacion.model.entity.TipoIndicador;
import com.usic.planificacion.model.entity.Usuario;
import com.usic.planificacion.model.service.CategoriaIndicadorService;
import com.usic.planificacion.model.service.IndicadorService;
import com.usic.planificacion.model.service.MetaService;
import com.usic.planificacion.model.service.ObjetivoService;
import com.usic.planificacion.model.service.TipoCalculoService;
import com.usic.planificacion.model.service.TipoIndicadorService;
import com.usic.planificacion.model.service.UsuarioService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/indicador")
public class IndicadorController {
    @Autowired
    private IndicadorService indicadorService;

    @Autowired
    private ObjetivoService objetivoService;

    @Autowired
    private CategoriaIndicadorService categoriaIndicadorService;

    @Autowired
    private TipoIndicadorService tipoIndicadorService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TipoCalculoService tipoCalculoService;

        @Autowired
    private MetaService metaService;

    @ValidarUsuarioAutenticado
    @GetMapping("/ventana")
    public String inicio() {
        return "indicador/ventana";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/tablaRegistros")
    public String tablaRegistros(Model model) {
        model.addAttribute("indicadores", indicadorService.findAlll());
        return "indicador/tablaRegistros";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/formulario")
    public String formulario(Model model) {
        model.addAttribute("indicador", new Indicador());
        model.addAttribute("objetivos", objetivoService.listarObjetivoActivos());
        model.addAttribute("tipos", tipoIndicadorService.findAll());
        model.addAttribute("categorias", categoriaIndicadorService.findAll());
        model.addAttribute("tipoCalculos", tipoCalculoService.findAll());
        return "indicador/formulario";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/Registrar")
    public ResponseEntity<String> Registrar(@Validated Indicador indicador, HttpServletRequest request) {

        Usuario user = (Usuario) request.getSession().getAttribute("usuario");

        indicador.setRegistroIdUsuario(user.getIdUsuario());
        indicador.setRegistro(new Date());
        indicador.setEstado("ACTIVO");
        indicador.setRegistroIdUsuario(user.getIdUsuario());

        List<Indicador> lista = indicadorService.listarIndicadorPorObjetivo(indicador.getObjetivo().getIdObjetivo());
        int total = indicador.getPonderacion();
        for (Indicador indicador2 : lista) {
            total = total + indicador2.getPonderacion();
        }
        System.out.println("total: " + total);
        if (total > 100) {
            return ResponseEntity.ok("Con este valor de ponderacion sobrepasa el 100 %");
        }
        indicador.setGestionBase((indicador.getObjetivo().getPolitica().getArea().getPei().getGestionInicio()-1));
        indicador.setLineaBase(0);
        indicador.setTotal(0);
        indicador.setGestionBase((indicador.getObjetivo().getPolitica().getArea().getPei().getGestionInicio()-1));
        indicadorService.save(indicador);

        for (int i = indicador.getObjetivo().getPolitica().getArea().getPei().getGestionInicio(); i <= indicador.getObjetivo().getPolitica().getArea().getPei().getGestionFin(); i++) {
            Meta meta = new Meta();
            meta.setIndicador(indicador);
            meta.setGestion(i);
            meta.setResultado(0);
            meta.setResultadoDecimal(0.0);
            meta.setGradoAvance(0.0);
            meta.setRegistroIdUsuario(user.getIdUsuario());
            meta.setRegistro(new Date());
            meta.setEstado("ACTIVO");
            metaService.save(meta);
        }

        return ResponseEntity.ok("Se realizó el registro correctamente");
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/formularioEdit/{id}")
    public String formulario(Model model, @PathVariable("id") Long id) {
        model.addAttribute("indicador", indicadorService.findById(id));
        model.addAttribute("objetivos", objetivoService.listarObjetivoActivos());
        model.addAttribute("tipos", tipoIndicadorService.findAll());
        model.addAttribute("categorias", categoriaIndicadorService.findAll());
        model.addAttribute("tipoCalculos", tipoCalculoService.findAll());
        model.addAttribute("edit", "true");
        return "indicador/formulario";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/Modificar")
    public ResponseEntity<String> Modificar(@Validated Indicador a, HttpServletRequest request) {

        try {
            Usuario user = (Usuario) request.getSession().getAttribute("usuario");

            Indicador indicador = indicadorService.findById(a.getIdIndicador());
            indicador.setCodigo(a.getCodigo());
            indicador.setDenominacion(a.getDenominacion());
            indicador.setPonderacion(a.getPonderacion());
            indicador.setObjetivo(a.getObjetivo());
            indicador.setCategoriaIndicador(a.getCategoriaIndicador());
            indicador.setTipoIndicador(a.getTipoIndicador());
            indicador.setTipoCalculo(a.getTipoCalculo());
            indicador.setModificacion(new Date());
            indicador.setModificacionIdUsuario(user.getIdUsuario());

            List<Indicador> lista = indicadorService.listarIndicadorPorObjetivoExeptoIdIndicador(indicador.getObjetivo().getIdObjetivo(), indicador.getIdIndicador());
            int total = indicador.getPonderacion();
            for (Indicador indicador2 : lista) {
                total = total + indicador2.getPonderacion();
            }
            System.out.println("total: " + total);
            if (total > 100) {
                return ResponseEntity.ok("Con este valor de ponderacion sobrepasa el 100 %");
            }

            indicador.setLineaBase(a.getLineaBase());
            indicador.setGestionBase(a.getGestionBase());
            indicadorService.save(indicador);

            return ResponseEntity.ok("Se realizó el registro correctamente");
        } catch (Exception e) {
            return ResponseEntity.ok(e.getMessage());
        }

    }

    @ValidarUsuarioAutenticado
    @PostMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(Model model, @PathVariable("id") Long id) {
        indicadorService.deleteById(id);
        return ResponseEntity.ok("Registro Eliminado");
    }
}
