package com.usic.planificacion.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.usic.planificacion.anotaciones.ValidarUsuarioAutenticado;
import com.usic.planificacion.model.entity.Area;
import com.usic.planificacion.model.entity.Indicador;
import com.usic.planificacion.model.entity.Meta;
import com.usic.planificacion.model.entity.Objetivo;
import com.usic.planificacion.model.entity.Pei;
import com.usic.planificacion.model.entity.Persona;
import com.usic.planificacion.model.entity.Politica;
import com.usic.planificacion.model.entity.Resultado;
import com.usic.planificacion.model.entity.UnidadFuncional;
import com.usic.planificacion.model.service.AreaService;
import com.usic.planificacion.model.service.IndicadorService;
import com.usic.planificacion.model.service.ObjetivoService;
import com.usic.planificacion.model.service.PeiService;
import com.usic.planificacion.model.service.PoliticaService;
import com.usic.planificacion.model.service.ResultadoService;
import com.usic.planificacion.model.service.UnidadFuncionalService;
import com.usic.planificacion.model.service.UtilidadesService;
import org.springframework.http.MediaType;

import jakarta.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JRException;

import org.springframework.web.bind.annotation.RequestBody;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/pei")
public class PeiController {

    @Autowired
    private UtilidadesService utilidadesService;

    @Autowired
    private PeiService peiService;

    @Autowired
    private AreaService areaService;

    @Autowired
    private ObjetivoService objetivoService;

    @Autowired
    private IndicadorService indicadorService;

    @Autowired
    private PoliticaService politicaService;

    @Autowired
    private ResultadoService resultadoService;

    @Autowired
    private UnidadFuncionalService unidadFuncionalService;

    @ValidarUsuarioAutenticado
    @GetMapping("/ventana")
    public String inicio() {
        return "pei/ventana";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/tablaRegistros")
    public String tablaRegistros(Model model) {
        model.addAttribute("peis", peiService.findAll());
        return "pei/tablaRegistros";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/formulario")
    public String formulario(Model model) {
        model.addAttribute("pei", new Pei());
        return "pei/formulario";
    }

    @PostMapping("/Registrar")
    public ResponseEntity<String> Registrar(@Validated Pei pei
    // @RequestParam("fechaInicio") String fechaInicio, @RequestParam("fechaFin")
    // String fechaFin
    ) {
        // Crear un formato de fecha para el formato recibido
        // SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");

        // try {
        // // Parsear la cadena original a un objeto Date
        // Date dateInicio = inputFormat.parse(fechaInicio);
        // Date dateFin = inputFormat.parse(fechaFin);

        // // Imprimir la fecha formateada para depuración
        // SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        // String formattedDateStr = outputFormat.format(dateInicio);
        // String formattedDateStrFin = outputFormat.format(dateFin);
        // System.out.println("Fecha formateada: " + formattedDateStr);

        // // Aquí se establece directamente el objeto Date
        // pei.setFechaInicio(dateInicio); // Asumiendo que `fechaFin` también es un
        // objeto Date
        // pei.setFechaFin(dateFin); // Asumiendo que `fechaFin` también es un objeto
        // Date

        // } catch (ParseException e) {
        // // Imprimir el error para depurar
        // System.err.println("Error al parsear la fecha: " + e.getMessage());
        // return ResponseEntity.badRequest().body("Fecha inválida: " + e.getMessage());
        // }

        pei.setEstado("ACTIVO");
        pei.setRegistro(new Date());
        peiService.save(pei);

        return ResponseEntity.ok("Se realizó el registro correctamente");
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/formularioEdit/{idPei}")
    public String formulario(Model model, @PathVariable("idPei") Long idPei) {
        model.addAttribute("pei", peiService.findById(idPei));
        model.addAttribute("edit", "true");
        return "pei/formulario";
    }

    @PostMapping("/Modificar")
    public ResponseEntity<String> Modificar(@Validated Pei p
    // @RequestParam("fechaInicio") String fechaInicio, @RequestParam("fechaFin")
    // String fechaFin
    ) {
        // Crear un formato de fecha para el formato recibido
        // SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
        Pei pei = peiService.findById(p.getIdPei());
        pei.setMision(p.getMision());
        pei.setVision(p.getVision());
        pei.setGestionInicio(p.getGestionInicio());
        pei.setGestionFin(p.getGestionFin());
        // try {
        // // Parsear la cadena original a un objeto Date
        // Date dateInicio = inputFormat.parse(fechaInicio);
        // Date dateFin = inputFormat.parse(fechaFin);

        // // Imprimir la fecha formateada para depuración
        // SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        // String formattedDateStr = outputFormat.format(dateInicio);
        // String formattedDateStrFin = outputFormat.format(dateFin);
        // System.out.println("Fecha formateada: " + formattedDateStr);

        // // Aquí se establece directamente el objeto Date
        // pei.setFechaInicio(dateInicio); // Asumiendo que `fechaFin` también es un
        // objeto Date
        // pei.setFechaFin(dateFin); // Asumiendo que `fechaFin` también es un objeto
        // Date

        // } catch (ParseException e) {
        // // Imprimir el error para depurar
        // System.err.println("Error al parsear la fecha: " + e.getMessage());
        // return ResponseEntity.badRequest().body("Fecha inválida: " + e.getMessage());
        // }

        peiService.save(pei);

        return ResponseEntity.ok("Se realizó el registro correctamente");
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(Model model, @PathVariable("id") Long idPei) {
        peiService.deleteById(idPei);
        return ResponseEntity.ok("Registro Eliminado");
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/formularioArea/{idPei}")
    public String formularioArea(Model model, @PathVariable("idPei") Long idPei) {
        model.addAttribute("pei", peiService.findById(idPei));
        model.addAttribute("area", new Area());
        model.addAttribute("areas", areaService.listarAreasPorPei(idPei));
        return "pei/formularioArea";
    }

    @PostMapping("/formularioAreaEdit/{idPei}/{idArea}")
    public String formularioAreaEdit(Model model, @PathVariable("idPei") Long idPei,
            @PathVariable("idArea") Long idArea) {
        model.addAttribute("pei", peiService.findById(idPei));
        model.addAttribute("area", areaService.findById(idArea));
        model.addAttribute("areas", areaService.listarAreasPorPei(idPei));
        model.addAttribute("edit", "true");
        return "pei/formularioArea";
    }

    @PostMapping("/RegistrarAreas")
    public ResponseEntity<String> RegistrarAreas(@RequestParam("pei") Long idPei, @RequestParam("areas") Long[] areas) {
        // TODO: process POST request
        Pei pei = peiService.findById(idPei);
        for (Long areaId : areas) {
            Area area = areaService.findById(areaId);
            area.setPei(pei);
            areaService.save(area);
        }
        return ResponseEntity.ok("Se realizó el registro correctamente");
    }

    @GetMapping("/verMatriz/{idPei}")
    public String cargarMatriz(Model model, @PathVariable Long idPei) {
        log.info("Metodo: " + idPei);
        List<Area> listaAreas = areaService.listarAreasPorPei(idPei);
        listaAreas.forEach(area -> area.getPoliticas()
                .forEach(politica -> politica.getObjetivos().forEach(objetivo -> objetivo.getIndicadores())));
        model.addAttribute("listaAreas", listaAreas);
        return "pei/matriz";
    }

    @GetMapping("/verMatriz-independiente/{idPei}")
    public String cargarMatrizIndependiente(Model model, @PathVariable Long idPei) {
        log.info("Metodo: " + idPei);
        List<Area> listaAreas = areaService.listarAreasPorPei(idPei);
        listaAreas.forEach(area -> area.getPoliticas().forEach(politica -> politica.getObjetivos()
                .forEach(objetivo -> objetivo.getIndicadores().forEach(indicador -> indicador.getResultados()))));
        model.addAttribute("listaAreas", listaAreas);
        return "pei/matriz-independiente";
    }

    @GetMapping("/verMatrizPeiPDF/{idPei}")
    public ResponseEntity<ByteArrayResource> verMatrizPeiPDF(Model model,
            HttpServletRequest request, @PathVariable(value = "idPei") Long idPei)
            throws SQLException {

        String rutaJaspert = "planificacion_matriz_pei.jrxml";

        Pei pei = peiService.findById(idPei);

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("idPei", pei.getIdPei());

        Path jasperPath = Paths.get(rutaJaspert);
        String rutaJasper = jasperPath.toString();

        ByteArrayOutputStream stream;
        try {
            stream = utilidadesService.compilarAndExportarReporte(rutaJasper, parametros);
            byte[] bytes = stream.toByteArray();
            ByteArrayResource resource = new ByteArrayResource(bytes);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline;filename=" + "MATRIZ PEI.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .contentLength(bytes.length)
                    .body(resource);
        } catch (IOException | JRException e) {
            // Manejo de excepciones comunes
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Devolver un estado de error
        }
    }

    @GetMapping("/verIndicadoresPeiPDF/{idPei}")
    public ResponseEntity<ByteArrayResource> verIndicadoresPeiPDF(Model model,
            HttpServletRequest request, @PathVariable(value = "idPei") Long idPei)
            throws SQLException {

        Pei pei = peiService.findById(idPei);

        OutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.LETTER, 45, 40, 55, 70);

        List<Objetivo> listaObjetivos = objetivoService.listarObjetivoPorIdPei(idPei);

        try {
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            // Configuración de fuente y estilos
            BaseFont base = BaseFont.createFont(
                    Paths.get("").toAbsolutePath().toString()
                            + "/src/main/resources/static/assets/fuente/bookman-old-style.ttf",
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font negritaTit = new Font(base, 12, Font.BOLD);
            Font negrita = new Font(base, 10, Font.BOLD);
            Font normal = new Font(base, 10);

            document.open();
            String titu = "Indicadores PEI " + pei.getGestionInicio() + " - " + pei.getGestionFin();
            Paragraph titulo = new Paragraph(titu, negritaTit);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingBefore(0);// salto de filas antes de parrafo
            titulo.setSpacingAfter(1);// salto de filas despues de parrafo
            document.add(titulo);

            PdfPTable table = new PdfPTable(5); // Número de columnas
            float[] columnWidths = { 0.7f, 2.5f, 2.5f, 1f, 1f }; // Ajusta los valores según tus necesidades
            table.setWidths(columnWidths);
            table.setWidthPercentage(100);
            table.setSpacingBefore(20f);

            // Encabezados de la tabla
            String[] headers = { "Nro", "Objetivo", "Indicador", "Tipo", "Categoria" };
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, negrita));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }
            table.setHeaderRows(1);
            // Agregar filas con datos de votantesDto
            int cont = 1;
            for (Objetivo objetivo : listaObjetivos) {
                List<Indicador> listaIndicadores = indicadorService
                        .listarIndicadorPorObjetivo(objetivo.getIdObjetivo());

                if (listaIndicadores.isEmpty()) {
                    // Si no hay indicadores, imprimimos solo una fila vacía con el número y el objetivo
                    PdfPCell nro = new PdfPCell(new Phrase(String.valueOf(cont++), negrita));
                    nro.setHorizontalAlignment(Element.ALIGN_CENTER);
                    nro.setFixedHeight(24f);
                    table.addCell(nro);

                    PdfPCell cellObjetivo = new PdfPCell(
                            new Phrase("CodOE " + objetivo.getCodigo() + ": " + objetivo.getDescripcion(), normal));
                    table.addCell(cellObjetivo);

                    // Celdas vacías para las columnas restantes
                    table.addCell(new PdfPCell(new Phrase("-", normal)));
                    table.addCell(new PdfPCell(new Phrase("-", normal)));
                    table.addCell(new PdfPCell(new Phrase("-", normal)));

                } else {
                    // Primera fila con rowspan
                    PdfPCell nro = new PdfPCell(new Phrase(String.valueOf(cont++), negrita));
                    nro.setHorizontalAlignment(Element.ALIGN_CENTER);
                    nro.setFixedHeight(24f);
                    nro.setRowspan(listaIndicadores.size());
                    table.addCell(nro);

                    PdfPCell cellObjetivo = new PdfPCell();
                    cellObjetivo.setRowspan(listaIndicadores.size());
                    cellObjetivo.addElement(
                            new Phrase("CodOE " + objetivo.getCodigo() + ": " + objetivo.getDescripcion(), normal));
                    table.addCell(cellObjetivo);

                    // Imprimir indicadores
                    boolean primera = true;
                    for (Indicador indicador : listaIndicadores) {
                        if (!primera) {
                            // Solo agregamos las columnas siguientes si no es la primera fila del grupo
                            // (las dos primeras ya están agregadas con rowspan)
                        }
                        primera = false;

                        table.addCell(new PdfPCell(
                                new Phrase("CodIE " + indicador.getCodigo() + ": " + indicador.getDenominacion(), normal)));
                        table.addCell(new PdfPCell(
                                new Phrase(indicador.getTipoIndicador().getNombre(), normal)));
                        table.addCell(new PdfPCell(
                                new Phrase(indicador.getCategoriaIndicador().getNombre(), normal)));
                    }
                }
            }
            document.add(table);

            document.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Devolver un estado de error
        }

        // Convertir OutputStream a byte array
        byte[] bytes = ((ByteArrayOutputStream) outputStream).toByteArray();
        ByteArrayResource resource = new ByteArrayResource(bytes);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=INDICADORES PEI.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(bytes.length)
                .body(resource);
    }

    @GetMapping("/verPoliticasPeiPDF/{idPei}")
    public ResponseEntity<ByteArrayResource> verPoliticasPeiPDF(Model model,
            HttpServletRequest request, @PathVariable(value = "idPei") Long idPei)
            throws SQLException {

        Pei pei = peiService.findById(idPei);

        OutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.LETTER, 45, 40, 55, 70);

        List<Area> listAreas = areaService.listarAreasPorPei(idPei);

        try {
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            // Configuración de fuente y estilos
            BaseFont base = BaseFont.createFont(
                    Paths.get("").toAbsolutePath().toString()
                            + "/src/main/resources/static/assets/fuente/bookman-old-style.ttf",
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font negritaTit = new Font(base, 12, Font.BOLD);
            Font negrita = new Font(base, 10, Font.BOLD);
            Font normal = new Font(base, 10);

            document.open();
            String titu = "Politicas PEI " + pei.getGestionInicio() + " - " + pei.getGestionFin();
            Paragraph titulo = new Paragraph(titu, negritaTit);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingBefore(0);// salto de filas antes de parrafo
            titulo.setSpacingAfter(1);// salto de filas despues de parrafo
            document.add(titulo);

            PdfPTable table = new PdfPTable(3); // Número de columnas
            float[] columnWidths = { 0.7f, 2.5f, 2.5f }; // Ajusta los valores según tus necesidades
            table.setWidths(columnWidths);
            table.setWidthPercentage(100);
            table.setSpacingBefore(20f);

            // Encabezados de la tabla
            String[] headers = { "Nro", "Area", "Politica" };
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, negrita));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }
            table.setHeaderRows(1);
            // Agregar filas con datos de votantesDto
            int cont = 1;
            for (Area area : listAreas) {
                List<Politica> listaPoliticas = area.getPoliticas();
                PdfPCell nro = new PdfPCell(new Phrase(String.valueOf(cont++), negrita));
                nro.setHorizontalAlignment(Element.ALIGN_CENTER);
                nro.setFixedHeight(24f);
                nro.setRowspan(listaPoliticas.size());
                table.addCell(nro);
                PdfPCell cellObjetivo = new PdfPCell();
                cellObjetivo.setRowspan(listaPoliticas.size());
                cellObjetivo.addElement(new Phrase("CodAE " + area.getCodigo() + ": " + area.getNombre(), normal));
                table.addCell(cellObjetivo);
                for (Politica politica : listaPoliticas) {

                    table.addCell(new PdfPCell(
                            new Phrase("CodPE " + politica.getCodigo() + ": " + politica.getDescripcion(), normal)));
                }
            }
            document.add(table);

            document.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Devolver un estado de error
        }

        // Convertir OutputStream a byte array
        byte[] bytes = ((ByteArrayOutputStream) outputStream).toByteArray();
        ByteArrayResource resource = new ByteArrayResource(bytes);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=POLITICAS PEI.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(bytes.length)
                .body(resource);
    }

    @GetMapping("/verObjetivoPeiPDF/{idPei}")
    public ResponseEntity<ByteArrayResource> verObjetivoPeiPDF(Model model,
            HttpServletRequest request, @PathVariable(value = "idPei") Long idPei)
            throws SQLException {

        Pei pei = peiService.findById(idPei);

        OutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.LETTER, 45, 40, 55, 70);

        List<Politica> politicas = politicaService.listarPoliticaPorIdPei(idPei);

        try {
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            // Configuración de fuente y estilos
            BaseFont base = BaseFont.createFont(
                    Paths.get("").toAbsolutePath().toString()
                            + "/src/main/resources/static/assets/fuente/bookman-old-style.ttf",
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font negritaTit = new Font(base, 12, Font.BOLD);
            Font negrita = new Font(base, 10, Font.BOLD);
            Font normal = new Font(base, 10);

            document.open();
            String titu = "Objetivos PEI " + pei.getGestionInicio() + " - " + pei.getGestionFin();
            Paragraph titulo = new Paragraph(titu, negritaTit);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingBefore(0);// salto de filas antes de parrafo
            titulo.setSpacingAfter(1);// salto de filas despues de parrafo
            document.add(titulo);

            PdfPTable table = new PdfPTable(3); // Número de columnas
            float[] columnWidths = { 0.7f, 2.5f, 2.5f }; // Ajusta los valores según tus necesidades
            table.setWidths(columnWidths);
            table.setWidthPercentage(100);
            table.setSpacingBefore(20f);

            // Encabezados de la tabla
            String[] headers = { "Nro", "Politica", "Objetivos" };
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, negrita));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }
            table.setHeaderRows(1);
            // Agregar filas con datos de votantesDto
            int cont = 1;
            for (Politica politica : politicas) {
                List<Objetivo> listaObjetivos = objetivoService.listarObjetivoPorPolitica(politica.getIdPolitica());
                PdfPCell nro = new PdfPCell(new Phrase(String.valueOf(cont++), negrita));
                nro.setHorizontalAlignment(Element.ALIGN_CENTER);
                nro.setFixedHeight(24f);
                nro.setRowspan(listaObjetivos.size());
                table.addCell(nro);
                PdfPCell cellObjetivo = new PdfPCell();
                cellObjetivo.setRowspan(listaObjetivos.size());
                cellObjetivo.addElement(
                        new Phrase("CodPD " + politica.getCodigo() + ": " + politica.getDescripcion(), normal));
                table.addCell(cellObjetivo);
                for (Objetivo objetivo : listaObjetivos) {
                    table.addCell(new PdfPCell(
                            new Phrase("CodOE " + objetivo.getCodigo() + ": " + objetivo.getDescripcion(), normal)));
                }
            }
            document.add(table);

            document.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Devolver un estado de error
        }

        // Convertir OutputStream a byte array
        byte[] bytes = ((ByteArrayOutputStream) outputStream).toByteArray();
        ByteArrayResource resource = new ByteArrayResource(bytes);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=OBJETIVOS PEI.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(bytes.length)
                .body(resource);
    }

    @GetMapping("/verMatrizPeiPDF2/{idPei}")
    public ResponseEntity<ByteArrayResource> verMatrizPeiPDF2(Model model,
            HttpServletRequest request, @PathVariable(value = "idPei") Long idPei)
            throws SQLException {

        Pei pei = peiService.findById(idPei);

        OutputStream outputStream = new ByteArrayOutputStream();
        Rectangle TamanoHojaDefinida = new Rectangle(612, 936).rotate();// 8.5 x 13 pulgadas
        Document document = new Document(TamanoHojaDefinida, 8, 8, 55, 60);

        List<Area> listAreas = areaService.listarAreasPorPei(idPei);

        try {
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            // Configuración de fuente y estilos
            BaseFont base = BaseFont.createFont(
                    Paths.get("").toAbsolutePath().toString()
                            + "/src/main/resources/static/assets/fuente/bookman-old-style.ttf",
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font negritaTit = new Font(base, 12, Font.BOLD);
            Font negrita = new Font(base, 7, Font.BOLD);//9
            Font negritaPequeño = new Font(base, 7, Font.BOLD);//8
            Font normal = new Font(base, 7);//10
            Font normalPequeño = new Font(base, 7);//8

            document.open();
            String titu = "PEI " + pei.getGestionInicio() + " - " + pei.getGestionFin();
            Paragraph titulo = new Paragraph(titu, negritaTit);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingBefore(0);// salto de filas antes de parrafo
            titulo.setSpacingAfter(1);// salto de filas despues de parrafo
            document.add(titulo);
            int[] gestiones = new int[pei.getGestionFin() - pei.getGestionInicio() + 1];
            for (int i = 0; i < gestiones.length; i++) {
                gestiones[i] = pei.getGestionInicio() + i;
            }

            // System.out.println("gestiones: " + gestiones.length);
            for (Area area : listAreas) {

                // System.out.println(16 + gestiones.length+2);
                int totalColumns = 16 + gestiones.length + 2;
                PdfPTable table = new PdfPTable(totalColumns);

                // Definir los anchos de columnas
                float[] columnWidths = { 0.3f, 0.5f, 0.3f, 0.5f, 2f, 0.3f, 0.5f, 2f, 0.3f, 0.5f, 2f, 0.3f, 0.3f,
                    0.3f, 2f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 1.5f };
                // Arrays.fill(columnWidths, 0, 7, 1f); // Anchos para las primeras columnas
                // Arrays.fill(columnWidths, 7, 7 + gestiones.length, 0.5f); // Anchos para años (gestiones)
                // Arrays.fill(columnWidths, 7 + gestiones.length, totalColumns, 1.5f); // Últimos detalles
                table.setWidths(columnWidths);
                table.setWidthPercentage(100);
                table.setSpacingBefore(20f);

                // Primera fila (encabezados agrupados)
                PdfPCell cell = new PdfPCell(new Phrase("Área Estratégica", negritaPequeño));
                cell.setRowspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell.setRotation(90);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Pond.", negrita));
                cell.setRowspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell.setRotation(90);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Política de Desarrollo", negritaPequeño));
                cell.setColspan(3);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Objetivo Estratégico", negritaPequeño));
                cell.setColspan(3);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Indicador Estratégico", negritaPequeño));
                cell.setColspan(5);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Resultado o Producto esperado al " + pei.getGestionFin(), negritaPequeño));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Línea Base " + (pei.getGestionInicio()-1), negritaPequeño));
                cell.setRowspan(2);
                cell.setRotation(90);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Programación Anual de Metas", negritaPequeño));
                cell.setColspan(gestiones.length);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Total", negrita));
                cell.setRowspan(2);
                cell.setRotation(90);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Responsable", negrita));
                cell.setRowspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Cod PD", negrita));
                cell.setRotation(90);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Pond.", negrita));
                cell.setRotation(90);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Descripción", negrita));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Cod OE", negrita));
                cell.setRotation(90);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Pond.", negrita));
                cell.setRotation(90);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Descripción", negrita));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Cod IE", negrita));
                cell.setRotation(90);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Pond.", negrita));
                cell.setRotation(90);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Denominación", negrita));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Tipo", negrita));
                cell.setRotation(90);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Categoría", negrita));
                cell.setRotation(90);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Cod RE", negrita));
                cell.setRotation(90);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Denominación", negrita));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);

                // Segunda fila (subencabezados para gestiones y detalles)
                for (int gestion : gestiones) {
                    cell = new PdfPCell(new Phrase(String.valueOf(gestion), negrita));
                    cell.setRotation(90); // Rotar los años para ahorrar espacio
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    table.addCell(cell);
                }

                // Agregar tabla al documento
                document.add(table);

                PdfPTable tableBody = new PdfPTable((23)); // Número de columnas
                float[] columnWidthsBody = { 0.3f, 0.5f, 0.3f, 0.5f, 2f, 0.3f, 0.5f, 2f, 0.3f, 0.5f, 2f, 0.3f, 0.3f,
                        0.3f, 2f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 1.5f };
                tableBody.setWidths(columnWidthsBody);
                tableBody.setWidthPercentage(100);
                //tableBody.setSpacingBefore(20f);
                // Obtener las políticas asociadas al área
                List<Politica> listaPoliticas = politicaService.listarPoliticaPorArea(area.getIdArea());

                // Inicializamos las filas del área: 1 fila para el área + las filas de todas
                // las políticas
                int filasArea = 0;

                // Contamos las filas por cada política y por sus objetivos
                for (Politica politica : listaPoliticas) {
                    // Obtener los objetivos asociados a la política
                    List<Objetivo> listaObjetivos = objetivoService.listarObjetivoPorPolitica(politica.getIdPolitica());

                    // Filas para la política (al menos 1 fila para cada política)
                    int filasPolitica = 1;

                    // Para cada objetivo dentro de la política
                    for (Objetivo objetivo : listaObjetivos) {
                        // Obtener los indicadores asociados a este objetivo
                        List<Indicador> listaIndicadores = indicadorService
                                .listarIndicadorPorObjetivo(objetivo.getIdObjetivo());

                        // Filas para el objetivo (1 fila por cada objetivo)
                        int filasObjetivo = 1;

                        // Sumar las filas de los indicadores de este objetivo
                        filasObjetivo += listaIndicadores.size();

                        // Sumar las filas del objetivo a las filas de la política
                        filasPolitica += filasObjetivo;
                    }

                    // Sumar las filas de la política al total de filas del área
                    filasArea += filasPolitica;
                }

                // Celdas comunes para cada área (rowspan en las celdas de Área y Ponderación)
                PdfPCell nro = new PdfPCell(new Phrase(String.valueOf(area.getCodigo()), normalPequeño));
                nro.setHorizontalAlignment(Element.ALIGN_CENTER);
                nro.setVerticalAlignment(Element.ALIGN_MIDDLE);
                nro.setRowspan(filasArea); // rowspan según el número total de filas que ocupa el área
                tableBody.addCell(nro);

                PdfPCell cellArea = new PdfPCell(new Phrase(area.getPonderacion() + "%", normalPequeño));
                cellArea.setRowspan(filasArea); // rowspan para el área
                cellArea.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellArea.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tableBody.addCell(cellArea);

                // Ahora agregamos las celdas de cada política
                for (Politica politica : listaPoliticas) {
                    List<Objetivo> listaObjetivos = objetivoService.listarObjetivoPorPolitica(politica.getIdPolitica());

                    // Calcular las filas para la política: 1 fila para la política
                    int filasPolitica = 0;

                    // Para cada objetivo dentro de la política
                    for (Objetivo objetivo : listaObjetivos) {
                        // Obtener los indicadores asociados a este objetivo
                        List<Indicador> listaIndicadores = indicadorService
                                .listarIndicadorPorObjetivo(objetivo.getIdObjetivo());

                        // Filas para el objetivo (1 fila por cada objetivo)
                        int filasObjetivo = listaIndicadores.size();

                        // Sumar las filas del objetivo a las filas de la política
                        filasPolitica += filasObjetivo;
                    }

                    // Primero agregamos las celdas de la política, con rowspan según el número de
                    // objetivos
                    PdfPCell cellPoliticaCod = new PdfPCell(new Phrase(politica.getCodigo() + "", normalPequeño));
                    cellPoliticaCod.setRowspan(filasPolitica); // rowspan para la política + los objetivos
                    cellPoliticaCod.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cellPoliticaCod.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tableBody.addCell(cellPoliticaCod);

                    PdfPCell cellPoliticaPond = new PdfPCell(
                            new Phrase(politica.getPonderacion() + "%", normalPequeño));
                    cellPoliticaPond.setRowspan(filasPolitica);
                    cellPoliticaPond.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cellPoliticaPond.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tableBody.addCell(cellPoliticaPond);

                    PdfPCell cellPoliticaDesc = new PdfPCell(new Phrase(politica.getDescripcion() + "", normal));
                    cellPoliticaDesc.setRowspan(filasPolitica);
                    cellPoliticaDesc.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tableBody.addCell(cellPoliticaDesc);
                    int cont = 1;
                    // Luego, por cada objetivo, agregamos las celdas correspondientes
                    for (Objetivo objetivo : listaObjetivos) {

                       

                        List<Indicador> listaIndicadores = indicadorService.listarIndicadorPorObjetivo(objetivo.getIdObjetivo());

                        if (listaIndicadores.isEmpty()) {
                            PdfPCell nro2 = new PdfPCell(new Phrase(String.valueOf(cont++), negrita));
                        nro2.setHorizontalAlignment(Element.ALIGN_CENTER);
                        nro2.setFixedHeight(24f);
                        table.addCell(nro2);
                        PdfPCell cellObjetivo = new PdfPCell(
                                new Phrase("CodOE " + objetivo.getCodigo() + ": " + objetivo.getDescripcion(), normal));
                        table.addCell(cellObjetivo);
                        // Celdas vacías para las columnas restantes
                        table.addCell(new PdfPCell(new Phrase("-", normal)));
                        table.addCell(new PdfPCell(new Phrase("-", normal)));
                        table.addCell(new PdfPCell(new Phrase("-", normal)));
                        table.addCell(new PdfPCell(new Phrase("-", normal)));
                        table.addCell(new PdfPCell(new Phrase("-", normal)));
                        table.addCell(new PdfPCell(new Phrase("-", normal)));
                        table.addCell(new PdfPCell(new Phrase("-", normal)));
                        table.addCell(new PdfPCell(new Phrase("-", normal)));
                        table.addCell(new PdfPCell(new Phrase("-", normal)));
                        table.addCell(new PdfPCell(new Phrase("-", normal)));
                        table.addCell(new PdfPCell(new Phrase("-", normal)));
                        table.addCell(new PdfPCell(new Phrase("-", normal)));
                        table.addCell(new PdfPCell(new Phrase("-", normal)));
                        table.addCell(new PdfPCell(new Phrase("-", normal)));
                        table.addCell(new PdfPCell(new Phrase("-", normal)));
                            } else { 
                                PdfPCell nro2 = new PdfPCell(new Phrase(String.valueOf(cont++), negrita));
                        nro2.setHorizontalAlignment(Element.ALIGN_CENTER);
                        nro2.setFixedHeight(24f);
                        nro2.setRowspan(listaIndicadores.size());
                        table.addCell(nro2);   
                        

                        int filasObjetivo = listaIndicadores.size();

                        // Agregar celdas del objetivo con rowspan según el número de indicadores
                        PdfPCell cellObjetivoCod = new PdfPCell(new Phrase(objetivo.getCodigo() + "", normalPequeño));
                        cellObjetivoCod.setRowspan(filasObjetivo);
                        cellObjetivoCod.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cellObjetivoCod.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        tableBody.addCell(cellObjetivoCod);

                        PdfPCell cellObjetivoPond = new PdfPCell(
                                new Phrase(objetivo.getPonderacion() + "%", normalPequeño));
                        cellObjetivoPond.setRowspan(filasObjetivo);
                        cellObjetivoPond.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cellObjetivoPond.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        tableBody.addCell(cellObjetivoPond);

                        PdfPCell cellObjetivoDesc = new PdfPCell(new Phrase(objetivo.getDescripcion() + "", normal));
                        cellObjetivoDesc.setRowspan(filasObjetivo);
                        cellObjetivoDesc.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        tableBody.addCell(cellObjetivoDesc);

                        // Por cada indicador, añadir una fila correspondiente
                        for (Indicador indicador : listaIndicadores) {
                            PdfPCell cellIndicadorCod = new PdfPCell(
                                    new Phrase(indicador.getCodigo() + "", normalPequeño));
                            cellIndicadorCod.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cellIndicadorCod.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            tableBody.addCell(cellIndicadorCod);

                            PdfPCell cellIndicadorPond = new PdfPCell(
                                    new Phrase(indicador.getPonderacion() + "%", normalPequeño));
                            cellIndicadorPond.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cellIndicadorPond.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            tableBody.addCell(cellIndicadorPond);

                            PdfPCell cellIndicadorDesc = new PdfPCell(
                                    new Phrase(indicador.getDenominacion(), normal));
                            cellIndicadorDesc.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            tableBody.addCell(cellIndicadorDesc);

                            PdfPCell cellIndicadorTipo = new PdfPCell(
                                    new Phrase(indicador.getTipoIndicador().getNombre(), normalPequeño));
                            cellIndicadorTipo.setRotation(+90);
                            cellIndicadorTipo.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cellIndicadorTipo.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            tableBody.addCell(cellIndicadorTipo);

                            PdfPCell cellIndicadorCat = new PdfPCell(
                                    new Phrase(indicador.getCategoriaIndicador().getNombre(), normalPequeño));
                            cellIndicadorCat.setRotation(+90);
                            cellIndicadorCat.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cellIndicadorCat.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            tableBody.addCell(cellIndicadorCat);

                            // Resultado de cada indicador
                            Resultado resultado = resultadoService.resultadoPorIdIndicador(indicador.getIdIndicador());

                            PdfPCell cellCodRe = new PdfPCell(
                                    new Phrase(String.valueOf(resultado.getCodigo()), normalPequeño));
                            cellCodRe.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cellCodRe.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            tableBody.addCell(cellCodRe);

                            PdfPCell cellResult = new PdfPCell(
                                    new Phrase(resultado.getDenominacion(), normal));
                            cellResult.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            tableBody.addCell(cellResult);

                            /*PdfPCell cellLineaBase = new PdfPCell(
                                    new Phrase(String.valueOf(indicador.getLineaBase()), normalPequeño));
                            cellLineaBase.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cellLineaBase.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            tableBody.addCell(cellLineaBase);*/
                            switch (indicador.getTipoCalculo().getNombre()) {
                                case "Entero":
                                    PdfPCell cellLineaBase = new PdfPCell(
                                        new Phrase(String.valueOf(indicador.getLineaBase()), normalPequeño));
                                cellLineaBase.setHorizontalAlignment(Element.ALIGN_CENTER);
                                cellLineaBase.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                tableBody.addCell(cellLineaBase);
                                    break;

                                case "Porcentaje":
                                    PdfPCell cellLineaBase1 = new PdfPCell(
                                        new Phrase(String.valueOf(indicador.getLineaBase())+ "%", normalPequeño));
                                cellLineaBase1.setHorizontalAlignment(Element.ALIGN_CENTER);
                                cellLineaBase1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                tableBody.addCell(cellLineaBase1);
                                    break;

                                case "Promedio":
                                    PdfPCell cellLineaBase2 = new PdfPCell(
                                        new Phrase(String.valueOf(indicador.getLineaBase()), normalPequeño));
                                cellLineaBase2.setHorizontalAlignment(Element.ALIGN_CENTER);
                                cellLineaBase2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                tableBody.addCell(cellLineaBase2);
                                    break;
                            }
                            // Metas de cada indicador que se listan por columnas en la misma fila
                            for (Meta meta : indicador.getMetasOrdenadas()) {

                                switch (indicador.getTipoCalculo().getNombre()) {
                                    case "Entero":
                                        PdfPCell cellMeta = new PdfPCell(
                                                new Phrase(String.valueOf(meta.getResultado()), normalPequeño));
                                        cellMeta.setHorizontalAlignment(Element.ALIGN_CENTER);
                                        cellMeta.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                        tableBody.addCell(cellMeta);
                                        break;

                                    case "Porcentaje":
                                        PdfPCell cellMeta1 = new PdfPCell(
                                                new Phrase(String.valueOf(meta.getResultadoDecimal()) + "%",
                                                        normalPequeño));
                                        cellMeta1.setHorizontalAlignment(Element.ALIGN_CENTER);
                                        cellMeta1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                        tableBody.addCell(cellMeta1);
                                        break;

                                    case "Promedio":
                                        PdfPCell cellMeta2 = new PdfPCell(
                                                new Phrase(String.valueOf(meta.getResultado()), normalPequeño));
                                        cellMeta2.setHorizontalAlignment(Element.ALIGN_CENTER);
                                        cellMeta2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                        tableBody.addCell(cellMeta2);
                                        break;
                                }
                            }

                            switch (indicador.getTipoCalculo().getNombre()) {
                                case "Entero":
                                    PdfPCell cellTotal = new PdfPCell(
                                            new Phrase(String.valueOf(indicador.getTotal()), normalPequeño));
                                    cellTotal.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    cellTotal.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                    tableBody.addCell(cellTotal);
                                    break;

                                case "Porcentaje":
                                    PdfPCell cellTotal2 = new PdfPCell(
                                            new Phrase(String.valueOf(indicador.getTotalDecimal()) + "%",
                                                    normalPequeño));
                                    cellTotal2.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    cellTotal2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                    tableBody.addCell(cellTotal2);
                                    break;

                                case "Promedio":
                                    PdfPCell cellTotal3 = new PdfPCell(
                                            new Phrase(String.valueOf(indicador.getTotal()), normalPequeño));
                                    cellTotal3.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    cellTotal3.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                    tableBody.addCell(cellTotal3);
                            }
                            List<UnidadFuncional> responsables = unidadFuncionalService
                                    .listarUnidadPorIdIndicador(indicador.getIdIndicador());
                            StringBuilder nombreRespon = new StringBuilder(); // Usamos StringBuilder para una mejor
                                                                              // eficiencia al concatenar cadenas.

                            int i = 0;
                            for (UnidadFuncional responsable : responsables) {
                                if (i > 0) {
                                    nombreRespon.append(", "); // Agrega una coma y un espacio entre los nombres.
                                }
                                nombreRespon.append(responsable.getNombre()); // Asume que 'getNombre()' obtiene el
                                                                              // nombre del responsable.
                                i++;
                            }

                            // Si deseas convertirlo en un String para usarlo después
                            String resultadoText = nombreRespon.toString();
                            PdfPCell cellResponsable = new PdfPCell(
                                    new Phrase(resultadoText, normal));
                            cellResponsable.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cellResponsable.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            tableBody.addCell(cellResponsable);

                        }
                        }
                    }
                
                }

                // Añadir la tabla al documento
                document.add(tableBody);
            }

            document.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Devolver un estado de error
        }

        // Convertir OutputStream a byte array
        byte[] bytes = ((ByteArrayOutputStream) outputStream).toByteArray();
        ByteArrayResource resource = new ByteArrayResource(bytes);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=PEI.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(bytes.length)
                .body(resource);
    }

}
