package com.usic.planificacion.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.usic.planificacion.anotaciones.ValidarUsuarioAutenticado;
import com.usic.planificacion.model.entity.ArchivoAdjunto;
import com.usic.planificacion.model.entity.Avance;
import com.usic.planificacion.model.entity.Indicador;
import com.usic.planificacion.model.entity.Meta;
import com.usic.planificacion.model.entity.Persona;
import com.usic.planificacion.model.entity.Usuario;
import com.usic.planificacion.model.service.ArchivoAdjuntoService;
import com.usic.planificacion.model.service.AvanceService;
import com.usic.planificacion.model.service.IndicadorService;
import com.usic.planificacion.model.service.MetaService;
import com.usic.planificacion.model.service.PersonaService;
import com.usic.planificacion.model.service.UnidadFuncionalService;
import com.usic.planificacion.model.service.UsuarioService;
import com.usic.planificacion.model.service.UtilidadesService;

import jakarta.servlet.http.HttpServletRequest;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/avance")
public class AvanceController {

    private final AvanceService avanceService;
    private final IndicadorService indicadorService;
    private final MetaService metaService;
    private final UsuarioService usuarioService;
    private final PersonaService personaService;
    private final ArchivoAdjuntoService adjuntoService;
    private final UtilidadesService utilidadesService;
    private final UnidadFuncionalService unidadFuncionalService;

    @ValidarUsuarioAutenticado
    @GetMapping("/ventana")
    public String inicio(Model model) {
        model.addAttribute("unidades", unidadFuncionalService.findAll());
        return "avances/ventana";
    }

    @ValidarUsuarioAutenticado
    @GetMapping("/ventanaEvaluador")
    public String ventanaEvaluador(Model model) {
        model.addAttribute("unidades", unidadFuncionalService.findAll());
        return "avances/ventanaEvaluador";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/tablaRegistros")
    public String tablaRegistros(Model model, HttpServletRequest request) {
        Usuario user = (Usuario) request.getSession().getAttribute("usuario");
        Persona persona = personaService.buscarPersonaPorIdUsuario(user.getIdUsuario());
        model.addAttribute("avances", avanceService.listarAvancesPorIdUnidad(persona.getUnidadFuncional().getIdUnidadFuncional()));
        return "avances/tablaRegistros";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/tablaRegistrosAvancesEvaluar/{idUnidad}")
    public String tablaRegistrosAvancesEvaluar(Model model, HttpServletRequest request, @PathVariable("idUnidad")Long idUnidad) {
        model.addAttribute("evaluacion", "true");
        model.addAttribute("avances", avanceService.listarAvancesPorIdUnidad(idUnidad));
        return "avances/tablaRegistros";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/formulario")
    public String formulario(Model model, HttpServletRequest request) {
        Usuario user = (Usuario) request.getSession().getAttribute("usuario");
        Persona persona = personaService.buscarPersonaPorIdUsuario(user.getIdUsuario());
        model.addAttribute("indicadores",
                indicadorService.listarIndicadoresPorIdUnidad(persona.getUnidadFuncional().getIdUnidadFuncional()));
        model.addAttribute("avance", new Avance());
        return "avances/formulario";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/Registrar")
    public ResponseEntity<String> Registrar(@Validated Avance avance, HttpServletRequest request,
            @RequestParam("valorAvance") String valorAvance, @RequestParam("idIndicador") Long idIndicador,
            @RequestParam(value = "filepond", required = false) MultipartFile[] adjuntos) {

        Usuario user = (Usuario) request.getSession().getAttribute("usuario");
        Persona persona = personaService.buscarPersonaPorIdUsuario(user.getIdUsuario());
        log.info("Identificador del indicador: "+idIndicador);
        Indicador indicador = indicadorService.findById(idIndicador);

        double valorProgramado = 0.0;

        switch (indicador.getTipoCalculo().getNombre()) {
            case "Entero":
                avance.setEjecutado(Integer.parseInt(valorAvance));
                valorProgramado = Double.parseDouble(avance.getMeta().getResultado().toString());
                break;

            case "Porcentaje":
                avance.setEjecutadoDecimal(Double.parseDouble(valorAvance));
                valorProgramado = avance.getMeta().getResultadoDecimal();
                break;

            case "Promedio":
                avance.setEjecutado(Integer.parseInt(valorAvance));
                valorProgramado = Double.parseDouble(avance.getMeta().getResultado().toString());
                break;
        }

        avance.setUnidadFuncional(persona.getUnidadFuncional());
        avance.setRegistroIdUsuario(user.getIdUsuario());
        avance.setRegistro(new Date());
        avance.setEstado("REGISTRADO");

        Double gradoAvance = 0.0;
        double valorEjecutado = Double.parseDouble(valorAvance);
        if (valorProgramado > 0.0) {
            gradoAvance = (valorEjecutado / (valorProgramado))*100;    
        }
        System.out.println("GRADO DE AVANCE: " + gradoAvance);

        avance.setGradoAvance(gradoAvance);
        avanceService.save(avance);

        log.info("Identificador de la meta: "+avance.getMeta().getIdMeta());
        Meta meta = metaService.findById(avance.getMeta().getIdMeta());
        meta.setGradoAvance(meta.getGradoAvance() + avance.getGradoAvance());
        metaService.save(meta);

        System.out.println("CANTIDAD FILES: " + adjuntos.length);
        for (MultipartFile multipartFile : adjuntos) {
            if (!multipartFile.isEmpty()) {
                ArchivoAdjunto archivo = new ArchivoAdjunto();
                archivo.setAvance(avance);
                archivo.setNombreArchivo(multipartFile.getOriginalFilename());
                archivo.setRutaArchivo(utilidadesService.guardarArchivo(multipartFile));
                archivo.setTipoArchivo(multipartFile.getContentType());
                archivo.setEstado("ACTIVO");
                archivo.setRegistro(new Date());
                adjuntoService.save(archivo);
            }
        }

        return ResponseEntity.ok("Se realizó el registro correctamente");
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/formularioEdit/{id}")
    public String formulario(Model model, @PathVariable("id") Long id, HttpServletRequest request) {
        Usuario user = (Usuario) request.getSession().getAttribute("usuario");
        Avance avance = avanceService.findById(id);

        Persona persona = personaService.buscarPersonaPorIdUsuario(user.getIdUsuario());
        model.addAttribute("indicadores", indicadorService.listarIndicadoresPorIdUnidad(persona.getUnidadFuncional().getIdUnidadFuncional()));
        model.addAttribute("metas", metaService.listarMetasPorIndicador(avance.getMeta().getIndicador().getIdIndicador()));
        model.addAttribute("avance", avance);
        model.addAttribute("edit", "true");
        return "avances/formulario";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/Modificar")
    public ResponseEntity<String> Modificar(@Validated Avance a, HttpServletRequest request,
        @RequestParam("valorAvance") String valorAvance, @RequestParam("idIndicador") Long idIndicador,
            @RequestParam(value = "filepond", required = false) MultipartFile[] adjuntos) {

        Avance avance = avanceService.findById(a.getIdAvance());
        avance.setEvaluacionCualitativa(a.getEvaluacionCualitativa());
        Meta meta = metaService.findById(a.getMeta().getIdMeta());
        meta.setGradoAvance((meta.getGradoAvance() - avance.getGradoAvance()));

        Indicador indicador = indicadorService.findById(idIndicador);

        double valorProgramado = 0.0;

        switch (indicador.getTipoCalculo().getNombre()) {
            case "Entero":
                avance.setEjecutado(Integer.parseInt(valorAvance));
                valorProgramado = Double.parseDouble(a.getMeta().getResultado().toString());
                break;

            case "Porcentaje":
                avance.setEjecutadoDecimal(Double.parseDouble(valorAvance));
                valorProgramado = a.getMeta().getResultadoDecimal();
                break;

            case "Promedio":
                avance.setEjecutado(Integer.parseInt(valorAvance));
                valorProgramado = Double.parseDouble(a.getMeta().getResultado().toString());
                break;
        }

        avance.setModificacion(new Date());

        double valorEjecutado = Double.parseDouble(valorAvance);
        Double gradoAvance = 0.0;
        if (valorProgramado > 0.0) {
            gradoAvance = (valorEjecutado / (valorProgramado))*100;    
        }
        System.out.println("GRADO DE AVANCE: " + gradoAvance);

        avance.setGradoAvance(gradoAvance);
        avanceService.save(avance);

        
        meta.setGradoAvance(meta.getGradoAvance() + avance.getGradoAvance());
        metaService.save(meta);

        System.out.println("CANTIDAD FILES: " + adjuntos.length);
        for (MultipartFile multipartFile : adjuntos) {
            if (!multipartFile.isEmpty()) {
                ArchivoAdjunto archivo = new ArchivoAdjunto();
                archivo.setAvance(avance);
                archivo.setNombreArchivo(multipartFile.getOriginalFilename());
                archivo.setRutaArchivo(utilidadesService.guardarArchivo(multipartFile));
                archivo.setTipoArchivo(multipartFile.getContentType());
                archivo.setEstado("ACTIVO");
                archivo.setRegistro(new Date());
                adjuntoService.save(archivo);
            }
        }

        return ResponseEntity.ok("Se realizó el registro correctamente");
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(Model model, @PathVariable("id") Long id) {
        avanceService.deleteById(id);
        return ResponseEntity.ok("Registro Eliminado");
    }

    @PostMapping("/detallesIndicador/{idIndicador}")
    public ResponseEntity<List<String[]>> detallesIndicador(Model model,
            @PathVariable("idIndicador") Long idIndicador) {

        List<Meta> metas = metaService.listarMetasPorIndicador(idIndicador);
        List<String[]> nombresMetas = new ArrayList();
        for (Meta meta : metas) {
            nombresMetas.add(new String[] { String.valueOf(meta.getIdMeta()), String.valueOf(meta.getGestion()) });
        }

        return ResponseEntity.ok(nombresMetas);
    }

    @PostMapping("/detallesMeta/{idMeta}")
    public ResponseEntity<String[]> detallesMeta(Model model, @PathVariable("idMeta") Long idMeta) {

        String[] detalle = new String[2];

        Meta meta = metaService.findById(idMeta);
        Indicador indicador = indicadorService.findById(meta.getIndicador().getIdIndicador());
        detalle[0] = indicador.getTipoCalculo().getNombre();

        switch (indicador.getTipoCalculo().getNombre()) {
            case "Entero":
                detalle[1] = String.valueOf(meta.getResultado());
                break;

            case "Porcentaje":
                detalle[1] = String.valueOf(meta.getResultadoDecimal());
                break;

            case "Promedio":
                detalle[1] = String.valueOf(meta.getResultado());
                break;
        }

        return ResponseEntity.ok(detalle);
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/formularioAdjunto/{id}")
    public String formularioAdjunto(Model model, @PathVariable("id") Long id, HttpServletRequest request) {
        model.addAttribute("evaluacion", "true");
        model.addAttribute("avance", avanceService.findById(id));
        model.addAttribute("archivos", adjuntoService.listarArchivosPorIdAvance(id));
        return "avances/formularioAdjuntar";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/GuardarAdjunto")
    public ResponseEntity<String> GuardarAdjunto(@RequestParam("idAvance") Long idAvance, HttpServletRequest request,
            @RequestParam(value = "filepond", required = false) MultipartFile[] adjuntos) {
        // System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        Avance avance = avanceService.findById(idAvance);
        if (adjuntos != null && adjuntos.length > 0) {
            for (MultipartFile multipartFile : adjuntos) {
                if (!multipartFile.isEmpty()) {
                    ArchivoAdjunto archivo = new ArchivoAdjunto();
                    archivo.setAvance(avance);
                    archivo.setNombreArchivo(multipartFile.getOriginalFilename());
                    archivo.setRutaArchivo(utilidadesService.guardarArchivo(multipartFile));
                    archivo.setTipoArchivo(multipartFile.getContentType());
                    archivo.setEstado("ACTIVO");
                    archivo.setRegistro(new Date());
                    adjuntoService.save(archivo);
                }
            }
            return ResponseEntity.ok("Se realizó el registro correctamente");
        } else {
            System.out.println("No se recibieron archivos.");
            return ResponseEntity.ok("No se guardaron los archivos");
        }
    }

    @PostMapping("/formularioEvaluacion/{idAvance}")
    public String formularioEvaluacion(HttpServletRequest request, @PathVariable("idAvance")Long idAvance, Model model) {
        model.addAttribute("avance", avanceService.findById(idAvance));
        return "avances/formularioEvaluacion";
    }

    @PostMapping("/GuardarEvaluacion")
    public ResponseEntity<String> GuardarEvaluacion(HttpServletRequest request, Avance a) {
        Avance avance = avanceService.findById(a.getIdAvance());
        avance.setEvaluacionCualitativa(a.getEvaluacionCualitativa());
        avance.setEstado("EVALUADO");
        avanceService.save(avance);
        return ResponseEntity.ok("Se realizó el registro correctamente");
    }

    @PostMapping("/GuardarAdjunto2")
    public ResponseEntity<String> GuardarAdjunto2(HttpServletRequest request,
            @RequestParam(value = "filepond", required = false) MultipartFile[] adjuntos) {
        // System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

        if (adjuntos != null && adjuntos.length > 0) {
            for (MultipartFile multipartFile : adjuntos) {
                if (!multipartFile.isEmpty()) {
                    System.out.println(multipartFile.getOriginalFilename());
                }
            }
            return ResponseEntity.ok("Se realizó el registro correctamente");
        } else {
            System.out.println("No se recibieron archivos.");
            return ResponseEntity.ok("No se guardaron los archivos");
        }
    }

    @PostMapping("/NotificacionesAvances")
    public ResponseEntity<List<Avance>> NotifiacionesAvances(HttpServletRequest request) {
        // System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        Usuario user = (Usuario) request.getSession().getAttribute("usuario");
        Persona persona = personaService.buscarPersonaPorIdUsuario(user.getIdUsuario());
        return ResponseEntity.ok(avanceService.listarAvancesPorIdUnidad(persona.getUnidadFuncional().getIdUnidadFuncional()));
    }

}
