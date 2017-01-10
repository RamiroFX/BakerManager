/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reportes;

import DB.DB_manager;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class PdfFromXMLFile {

    public static void main(String[] args) throws JRException, IOException, SQLException {
        DB_manager.conectarBD("postgres", "postgres");
        String path = System.getProperty("user.dir") + "\\src\\Assets\\Reportes\\ResumenPedidos.jrxml";
        //String path = System.getProperty("user.dir") + "\\src\\Assets\\Reportes\\PedidoCliente2.jrxml";
        // Compile jrxml file.
        JasperReport jasperReport = JasperCompileManager
                .compileReport(path);


        String fecha_inicio = "01/01/16 00:00:00.00";
        String fecha_fin = "30/12/16 23:59:59.00";
        // Parameters for report
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("fecha inicio", fecha_inicio);
        parameters.put("fecha fin", fecha_fin);

        // DataSource
        // This is simple example, no database.
        // then using empty datasource.

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
                parameters, DB_manager.getConection());


        // Make sure the output directory exists.
        File outDir = new File("C:/jasperoutput");
        outDir.mkdirs();

        // Export to PDF.
        JasperExportManager.exportReportToPdfFile(jasperPrint,
                "C:/jasperoutput/StyledTextReport.pdf");

        System.out.println("Done!");
    }
}