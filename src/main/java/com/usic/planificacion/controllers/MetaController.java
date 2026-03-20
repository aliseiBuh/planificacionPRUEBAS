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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.usic.planificacion.anotaciones.ValidarUsuarioAutenticado;
import com.usic.planificacion.model.entity.Indicador;
import com.usic.planificacion.model.entity.Meta;
import com.usic.planificacion.model.entity.Pei;
import com.usic.planificacion.model.entity.UnidadFuncional;
import com.usic.planificacion.model.entity.Usuario;
import com.usic.planificacion.model.service.IndicadorService;
import com.usic.planificacion.model.service.MetaService;
import com.usic.planificacion.model.service.ResultadoService;
import com.usic.planificacion.model.service.UnidadFuncionalService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/meta")
public class MetaController {

    @Autowired
    private MetaService metaService;

    @Autowired
    private IndicadorService indicadorService;

    @Autowired
    private ResultadoService resultadoService;

    @Autowired
    private UnidadFuncionalService funcionalService;

    @ValidarUsuarioAutenticado
    @GetMapping("/ventana")
    public String inicio() {
        return "meta/ventana";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/tablaRegistros")
    public String tablaRegistros(Model model) {
        model.addAttribute("listaRegistros", resultadoService.findAlll());
        // model.addAttribute("meta",new Meta());

        // System.out.println("ID INDICADORRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR"+idIndicador);
        return "meta/tablaRegistros";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/formularioLineaBase/{id}")
    public String formulario(Model model, @PathVariable("id") Long id) {
        model.addAttribute("indicador", indicadorService.findById(id));
        model.addAttribute("edit", "true");
        return "meta/formularioLineaBase";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/ModificarLineaBase")
    public ResponseEntity<String> Modificar(@Validated Indicador a, HttpServletRequest request) {

        try {
            Indicador indicador = indicadorService.findById(a.getIdIndicador());
            indicador.setLineaBase(a.getLineaBase());
            indicador.setGestionBase(a.getGestionBase());
            indicadorService.save(indicador);

            return ResponseEntity.ok("Se realizó el registro correctamente");
        } catch (Exception e) {
            return ResponseEntity.ok(e.getMessage());
        }

    }

    @ValidarUsuarioAutenticado
    @PostMapping("/formularioMetaAnual/{idIndicador}")
    public String formularioMetaAnual(Model model, @PathVariable("idIndicador") Long idIndicador) {
        model.addAttribute("idIndicador", idIndicador);
        model.addAttribute("meta", new Meta());
        model.addAttribute("metas", metaService.listarMetasPorIndicador(idIndicador));
        Pei pei = indicadorService.findById(idIndicador).getObjetivo().getPolitica().getArea().getPei();
        List<Integer> gestiones = new ArrayList();
        for (int i = pei.getGestionInicio(); i <= pei.getGestionFin(); i++) {
            gestiones.add(i);
        } 
        System.out.println("------------------------------------GESTIONES:"+gestiones);
        model.addAttribute("gestiones", gestiones);
        return "meta/formulario";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/Registrar")
    public ResponseEntity<String> Registrar(@Validated Meta meta, HttpServletRequest request,
            @RequestParam("idIndicador") Long idIndicador) {

        Indicador indicador = indicadorService.findById(idIndicador);
        Integer total = indicador.getTotal() + meta.getResultado();
        indicador.setTotal(total);
        indicadorService.save(indicador);

        Usuario user = (Usuario) request.getSession().getAttribute("usuario");

        meta.setIndicador(indicador);
        meta.setRegistroIdUsuario(user.getIdUsuario());
        meta.setRegistro(new Date());
        meta.setEstado("ACTIVO");
        meta.setGradoAvance(0.0);
        metaService.save(meta);

        return ResponseEntity.ok("Se realizó el registro correctamente");
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/formularioMetaAnualEdit/{idMeta}")
    public String formularioMetaAnualEdit(Model model, @PathVariable("idMeta") Long idMeta) {

        Meta meta = metaService.findById(idMeta);
        Long idIndicador = meta.getIndicador().getIdIndicador();

        model.addAttribute("idIndicador", idIndicador);
        model.addAttribute("meta", meta);
        model.addAttribute("metas", metaService.listarMetasPorIndicador(idIndicador));
        Pei pei = indicadorService.findById(idIndicador).getObjetivo().getPolitica().getArea().getPei();
        List<Integer> gestiones = new ArrayList();
        for (int i = pei.getGestionInicio(); i <= pei.getGestionFin(); i++) {
            gestiones.add(i);
        }
        model.addAttribute("gestiones", gestiones);
        model.addAttribute("edit", "true");
        return "meta/formulario";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/Modificar")
    public ResponseEntity<String> Modificar(@Validated Meta m, HttpServletRequest request) {

        Meta meta = metaService.findById(m.getIdMeta());
        Indicador indicador = indicadorService.findById(meta.getIndicador().getIdIndicador());

        switch (indicador.getTipoCalculo().getNombre()) {
            case "Entero":
                meta.setGestion(m.getGestion());
                meta.setResultado(m.getResultado());
                metaService.save(meta);
                break;

            case "Porcentaje":
                meta.setGestion(m.getGestion());
                meta.setResultadoDecimal(m.getResultadoDecimal());
                metaService.save(meta);
                break;

            case "Promedio":
                meta.setGestion(m.getGestion());
                meta.setResultado(m.getResultado());
                metaService.save(meta);
                break;

            default:
                return ResponseEntity.ok("El indicador debe tener un tipo de calculo");

        }

        // // if (indicador.getTipoCalculo().getNombre() == null ||
        // // indicador.getTipoCalculo().getNombre().equals("")) {
        // // return ResponseEntity.ok("debe seleccionar un tipo de calculo");
        // // }

        // switch (indicador.getTipoCalculo().getNombre()) {
        // case "Entero":
        // Integer total = 0;
        // total = indicador.getTotal() - meta.getResultado();
        // total = total + m.getResultado();
        // indicador.setTotal(total);
        // indicadorService.save(indicador);
        // meta.setGestion(m.getGestion());
        // meta.setResultado(m.getResultado());
        // metaService.save(meta);
        // break;

        // case "Porcentaje":
        // // Integer totalPorcentaje = 0;
        // // totalPorcentaje = indicador.getTotal() - meta.getResultado();
        // // totalPorcentaje = totalPorcentaje + m.getResultado();
        // // if (totalPorcentaje > 100) {
        // // return ResponseEntity.ok("Con este valor sobrepasa el 100 %");
        // // }
        // // indicador.setTotal(totalPorcentaje);
        // // indicadorService.save(indicador);
        // // meta.setGestion(m.getGestion());
        // // meta.setResultado(m.getResultado());
        // // metaService.save(meta);
        // double totalPorcentaje = 0.0;
        // totalPorcentaje = indicador.getTotalDecimal() - meta.getResultadoDecimal();
        // totalPorcentaje = totalPorcentaje + m.getResultadoDecimal();
        // if (totalPorcentaje > 100.0) {
        // return ResponseEntity.ok("Con este valor sobrepasa el 100 %");
        // }
        // indicador.setTotalDecimal(totalPorcentaje);
        // indicadorService.save(indicador);
        // meta.setGestion(m.getGestion());
        // meta.setResultadoDecimal(m.getResultadoDecimal());
        // metaService.save(meta);
        // break;

        // case "Promedio":

        // meta.setGestion(m.getGestion());
        // meta.setResultado(m.getResultado());
        // metaService.save(meta);
        // double promedio = 0.0;
        // Integer numerador = 0;
        // for (Meta metaPromedio :
        // metaService.listarMetasPorIndicador(indicador.getIdIndicador())) {
        // numerador = numerador + metaPromedio.getResultado();
        // }
        // promedio = (double) numerador /
        // metaService.listarMetasPorIndicador(indicador.getIdIndicador()).size();
        // double decimalParte = promedio - Math.floor(promedio);
        // Integer totalPromedio = 0;
        // if (decimalParte >= 0.5) {
        // totalPromedio = (int) Math.ceil(promedio); // Redondear hacia arriba
        // } else if (decimalParte == 0.4) {
        // totalPromedio = (int) Math.floor(promedio); // Redondear hacia abajo
        // } else {
        // totalPromedio = (int) Math.floor(promedio); // Redondear hacia abajo
        // }
        // // System.out.println("PROMEDIO: "+totalPromedio);
        // indicador.setTotal(totalPromedio);
        // indicadorService.save(indicador);
        // break;

        // case "Promedio de Porcentaje":

        // meta.setGestion(m.getGestion());
        // meta.setResultadoDecimal(m.getResultadoDecimal());
        // metaService.save(meta);
        // double promedio2 = 0.0;
        // double numerador2 = 0.0;
        // for (Meta metaPromedio :
        // metaService.listarMetasPorIndicador(indicador.getIdIndicador())) {
        // numerador2 = numerador2 + metaPromedio.getResultadoDecimal();
        // }
        // promedio2 = numerador2 /
        // metaService.listarMetasPorIndicador(indicador.getIdIndicador()).size();

        // System.out.println("PROMEDIO: " + promedio2);
        // indicador.setTotalDecimal(promedio2);
        // indicadorService.save(indicador);
        // break;

        // default:
        // return ResponseEntity.ok("El indicador debe tener un tipo de calculo");

        // }

        return ResponseEntity.ok("Se realizó el registro correctamente");
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(Model model, @PathVariable("id") Long id) {
        metaService.deleteById(id);
        return ResponseEntity.ok("Registro Eliminado");
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/formularioTotalEdit/{idIndicador}")
    public String formularioTotalEdit(Model model, @PathVariable("idIndicador") Long idIndicador) {

        model.addAttribute("indicador", indicadorService.findById(idIndicador));
        model.addAttribute("edit", "true");
        return "meta/formularioTotal";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/ModificarTotal")
    public ResponseEntity<String> ModificarTotal(@Validated Indicador i, HttpServletRequest request) {

        Indicador indicador = indicadorService.findById(i.getIdIndicador());

        switch (indicador.getTipoCalculo().getNombre()) {
            case "Entero":
                indicador.setTotal(i.getTotal());
                indicadorService.save(indicador);
                break;

            case "Porcentaje":
                indicador.setTotalDecimal(i.getTotalDecimal());
                indicadorService.save(indicador);
                break;

            case "Promedio":
                indicador.setTotal(i.getTotal());
                indicadorService.save(indicador);
                break;

            default:
                return ResponseEntity.ok("El indicador debe tener un tipo de calculo registrado");

        }

        return ResponseEntity.ok("Se realizó el registro correctamente");
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/formularioResponsable/{id}")
    public String formularioResponsable(Model model, @PathVariable("id") Long id) {
        model.addAttribute("indicador", indicadorService.findById(id));
        model.addAttribute("unidades", funcionalService.findAll());
        return "meta/formularioResponsable";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/AgregarResponsable")
    public ResponseEntity<String> AgregarResponsable(@Validated Indicador i, HttpServletRequest request) {

        Indicador indicador = indicadorService.findById(i.getIdIndicador());
        // indicador.setUnidadFuncional(i.getUnidadFuncional());
        indicador.setResponsables(i.getResponsables());
        indicadorService.save(indicador);

        return ResponseEntity.ok("Se realizó el registro correctamente");
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/agregarUnidadSelect")
    public ResponseEntity<String[]> agregarUnidadSelect(@RequestParam("nombre") String nombre,
            HttpServletRequest request) {

        Usuario user = (Usuario) request.getSession().getAttribute("usuario");
        UnidadFuncional unidadFuncional = funcionalService.buscarUnidadPorNombre(nombre);
        if (unidadFuncional == null) {
            unidadFuncional = new UnidadFuncional();
            unidadFuncional.setNombre(nombre);
            // unidadFuncional.setSigla(sigla);
            unidadFuncional.setEstado("ACTIVO");
            unidadFuncional.setRegistro(new Date());
            unidadFuncional.setRegistroIdUsuario(user.getIdUsuario());
            funcionalService.save(unidadFuncional);
        }

        String[] unidad = { unidadFuncional.getIdUnidadFuncional().toString(), unidadFuncional.getNombre() };

        return ResponseEntity.ok(unidad);
    }

}
