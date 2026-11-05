package com.mycompany.jadk_care.controladores;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.Table;
import com.lowagie.text.Cell;
import com.lowagie.text.Image;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import java.io.ByteArrayOutputStream;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Color;

import com.mycompany.jadk_care.dao.EquipoDAO;
import com.mycompany.jadk_care.modelo.Equipo;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reportes")
public class ReportesController {

    private final EquipoDAO equipoDAO = new EquipoDAO();

    @GetMapping("/excel")
    public ResponseEntity<byte[]> exportarExcel(
            @RequestParam(defaultValue = "reporte") String filename,
            @RequestParam(defaultValue = "true") boolean opt1,
            @RequestParam(defaultValue = "true") boolean opt2,
            @RequestParam(defaultValue = "false") boolean opt3,
            @RequestParam(required = false) String lab,
            @RequestParam(required = false) String status) {
            
        List<Equipo> equipos = equipoDAO.obtenerTodosEquipos();
        
        // Aplicar filtros personalizados
        if (lab != null && !lab.isEmpty() && !lab.equals("todos")) {
            equipos = equipos.stream().filter(e -> lab.equalsIgnoreCase(e.getLaboratorio())).collect(Collectors.toList());
        }
        if (status != null && !status.isEmpty() && !status.equals("all")) {
            equipos = equipos.stream().filter(e -> status.equalsIgnoreCase(e.getEstadoActual())).collect(Collectors.toList());
        }

        StringBuilder csv = new StringBuilder();
        // UTF-8 BOM to make Excel display special characters properly
        csv.append('\ufeff');
        
        if (opt1) { // Incluir Resumen
            csv.append("RESUMEN GENERAL DEL INVENTARIO\n");
            long ok = equipos.stream().filter(e -> "green".equals(e.getEstadoActual())).count();
            long warn = equipos.stream().filter(e -> "yellow".equals(e.getEstadoActual())).count();
            long crit = equipos.stream().filter(e -> "red".equals(e.getEstadoActual())).count();
            csv.append("Total Equipos,").append(equipos.size()).append("\n");
            csv.append("Operativos,").append(ok).append("\n");
            csv.append("En Observacion,").append(warn).append("\n");
            csv.append("Criticos,").append(crit).append("\n\n");
        }

        csv.append("ID Equipo,Laboratorio,Puesto,Estado,Proximo Mantenimiento");
        if (opt3) {
            csv.append(",Historial de Mantenimiento");
        }
        csv.append("\n");
        
        for (Equipo eq : equipos) {
            csv.append(eq.getIdEquipo() != null ? eq.getIdEquipo() : "").append(",")
               .append(eq.getLaboratorio() != null ? eq.getLaboratorio() : "").append(",")
               .append(eq.getNumPuesto()).append(",")
               .append(eq.getEstadoActual() != null ? eq.getEstadoActual() : "").append(",")
               .append(eq.getFechaProxMan() != null ? eq.getFechaProxMan().toString() : "");
               
            if (opt3) {
                List<java.util.Map<String, Object>> historial = equipoDAO.obtenerHistorialMantenimiento(eq.getIdEquipo());
                if (historial != null && !historial.isEmpty()) {
                    csv.append(",\"").append(historial.get(0).get("fecha")).append(" - ").append(historial.get(0).get("tipo"))
                       .append(" (").append(historial.get(0).get("tecnico")).append(")\"");
                } else {
                    csv.append(",Sin mantenimientos");
                }
            }
            csv.append("\n");
        }
        
        byte[] csvBytes = csv.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", filename.endsWith(".csv") ? filename : filename + ".csv");
        
        return ResponseEntity.ok().headers(headers).body(csvBytes);
    }

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> exportarPdf(
            @RequestParam(defaultValue = "reporte") String filename,
            @RequestParam(defaultValue = "true") boolean opt1,
            @RequestParam(defaultValue = "true") boolean opt2,
            @RequestParam(defaultValue = "true") boolean opt3,
            @RequestParam(required = false) String lab,
            @RequestParam(required = false) String status) {
            
        List<Equipo> equipos = equipoDAO.obtenerTodosEquipos();
        
        // Aplicar filtros personalizados
        if (lab != null && !lab.isEmpty() && !lab.equals("todos")) {
            equipos = equipos.stream().filter(e -> lab.equalsIgnoreCase(e.getLaboratorio())).collect(Collectors.toList());
        }
        if (status != null && !status.isEmpty() && !status.equals("all")) {
            equipos = equipos.stream().filter(e -> status.equalsIgnoreCase(e.getEstadoActual())).collect(Collectors.toList());
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();
            
            document.add(new Paragraph("Reporte de Equipos - JADK Care"));
            if (lab != null && !lab.equals("todos")) {
                document.add(new Paragraph("Filtro: " + lab.toUpperCase()));
            }
            document.add(new Paragraph(" "));
            
            if (opt2) {
                document.add(new Paragraph("RESUMEN GENERAL DEL INVENTARIO"));
                long ok = equipos.stream().filter(e -> "green".equals(e.getEstadoActual())).count();
                long warn = equipos.stream().filter(e -> "yellow".equals(e.getEstadoActual())).count();
                long crit = equipos.stream().filter(e -> "red".equals(e.getEstadoActual())).count();
                document.add(new Paragraph("Total de equipos: " + equipos.size()));
                document.add(new Paragraph("Operativos: " + ok));
                document.add(new Paragraph("En Observacion: " + warn));
                document.add(new Paragraph("Criticos: " + crit));
                document.add(new Paragraph(" "));
            }
            
            if (opt1) {
                try {
                    // Crear dataset
                    DefaultPieDataset dataset = new DefaultPieDataset();
                    dataset.setValue("Operativos", equipos.stream().filter(e -> "green".equals(e.getEstadoActual())).count());
                    dataset.setValue("En Observacion", equipos.stream().filter(e -> "yellow".equals(e.getEstadoActual())).count());
                    dataset.setValue("Criticos", equipos.stream().filter(e -> "red".equals(e.getEstadoActual())).count());
                    
                    // Crear grafica
                    JFreeChart chart = ChartFactory.createPieChart(
                        "Estado Actual de los Equipos", 
                        dataset, 
                        true, true, false
                    );
                    
                    // Personalizar colores
                    org.jfree.chart.plot.PiePlot plot = (org.jfree.chart.plot.PiePlot) chart.getPlot();
                    plot.setSectionPaint("Operativos", new Color(46, 204, 113));
                    plot.setSectionPaint("En Observacion", new Color(241, 196, 15));
                    plot.setSectionPaint("Criticos", new Color(231, 76, 60));
                    plot.setBackgroundPaint(Color.WHITE);
                    
                    // Convertir a imagen PDF
                    BufferedImage chartImage = chart.createBufferedImage(400, 300);
                    Image pdfImage = Image.getInstance(chartImage, null);
                    pdfImage.setAlignment(Image.ALIGN_CENTER);
                    
                    document.add(pdfImage);
                    document.add(new Paragraph(" "));
                } catch (Throwable t) {
                    // En servidores de Linux como Railway, si faltan las librerías de fuentes (fontconfig) 
                    // Java arroja un "Error" (UnsatisfiedLinkError, NoClassDefFoundError) que se salta el Exception.
                    // Al atrapar "Throwable", salvamos el PDF para que no se rompa por completo.
                    System.err.println("Advertencia: No se pudo dibujar la grafica por falta de interfaz grafica/fuentes en el servidor: " + t.getMessage());
                    document.add(new Paragraph("[La grafica no pudo generarse en este servidor]"));
                    document.add(new Paragraph(" "));
                }
            }
            
            Table table = new Table(5);
            table.addCell(new Cell("ID Equipo"));
            table.addCell(new Cell("Laboratorio"));
            table.addCell(new Cell("Puesto"));
            table.addCell(new Cell("Estado"));
            table.addCell(new Cell("Prox. Mant."));
            
            for (Equipo eq : equipos) {
                table.addCell(new Cell(eq.getIdEquipo()));
                table.addCell(new Cell(eq.getLaboratorio()));
                table.addCell(new Cell(String.valueOf(eq.getNumPuesto())));
                table.addCell(new Cell(eq.getEstadoActual()));
                table.addCell(new Cell(eq.getFechaProxMan() != null ? eq.getFechaProxMan().toString() : ""));
            }
            
            document.add(table);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", filename.endsWith(".pdf") ? filename : filename + ".pdf");
        
        return ResponseEntity.ok().headers(headers).body(out.toByteArray());
    }
}
