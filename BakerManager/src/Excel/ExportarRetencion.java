/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Excel;

import Entities.E_retencionVenta;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JFileChooser;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author Ramiro Ferreira
 */
public class ExportarRetencion {

    String nombreHoja;
    ArrayList<E_retencionVenta> cabecera;
    Date fechaInic, fechaFinal;
    HSSFWorkbook workbook;
    HSSFSheet sheet;
    CellStyle styleSubTitle, styleSubTitle2, styleNumberFloatPoint, styleNumberInteger;
    HSSFCellStyle styleDate;

    public ExportarRetencion(String nombreHoja, ArrayList<E_retencionVenta> ed) {
        this.nombreHoja = nombreHoja;
        this.cabecera = ed;
        this.fechaInic = ed.get(0).getTiempo();
        this.fechaFinal = ed.get(ed.size() - 1).getTiempo();
        createWorkBook();
        createCellStyles();
    }

    private void createWorkBook() {
        workbook = new HSSFWorkbook();
        sheet = workbook.createSheet(nombreHoja);
    }

    private void createCellStyles() {
        //COLOR STYLE
        // Aqua background
        styleSubTitle = workbook.createCellStyle();
        styleSubTitle.setFillForegroundColor(IndexedColors.GOLD.getIndex());
        styleSubTitle.setFillPattern(CellStyle.SOLID_FOREGROUND);

        // Orange "foreground", foreground being the fill foreground not the font color.
        styleSubTitle2 = workbook.createCellStyle();
        styleSubTitle2.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        styleSubTitle2.setFillPattern(CellStyle.SOLID_FOREGROUND);
        //END STYLE
        // FORMAT STYLE
        DataFormat format = workbook.createDataFormat();
        styleNumberFloatPoint = workbook.createCellStyle();
        styleNumberFloatPoint.setDataFormat(format.getFormat("0.0"));

        styleNumberInteger = workbook.createCellStyle();
        styleNumberInteger.setDataFormat(format.getFormat("#,##0"));

        styleDate = workbook.createCellStyle();
        short df = workbook.createDataFormat().getFormat("dd-MM-yyyy");
        styleDate.setDataFormat(df);
        //END FORMAT STYLE
    }

    public void exportacionResumida() {
        File directory = null;
        String desktop = System.getProperty("user.home") + "\\Desktop";
        JFileChooser chooser = new JFileChooser(desktop);
        if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            directory = chooser.getSelectedFile();
            directory.setWritable(true);
            directory.setExecutable(true);
            directory.setReadable(true);
        } else {
            return;
        }
        // Create a row and put some cells in it. Rows are 0 based.
        int filaActual = 0;
        int col = 0;
        Row fechaInicio = sheet.createRow(filaActual);
        filaActual++;
        Row fechaFin = null;
        if (fechaInic != null && fechaFinal != null) {
            int dateType = fechaInic.compareTo(fechaFinal);
            if (dateType == 0) {
                fechaInicio.createCell(col).setCellValue(new HSSFRichTextString("Fecha :"));
                fechaInicio.getCell(col).setCellStyle(styleSubTitle2);
                col++;
                fechaInicio.createCell(col).setCellValue(fechaInic);
                fechaInicio.getCell(col).setCellStyle(styleDate);
                col++;
            } else {
                fechaFin = sheet.createRow(filaActual);
                filaActual++;
                fechaFin.createCell(col).setCellValue(new HSSFRichTextString("Fecha fin:"));
                fechaFin.getCell(col).setCellStyle(styleSubTitle2);
                fechaInicio.createCell(col).setCellValue(new HSSFRichTextString("Fecha inicio:"));
                fechaInicio.getCell(col).setCellStyle(styleSubTitle2);
                col++;
                fechaFin.createCell(col).setCellValue(fechaFinal);
                fechaFin.getCell(col).setCellStyle(styleDate);
                fechaInicio.createCell(col).setCellValue(fechaInic);
                fechaInicio.getCell(col).setCellStyle(styleDate);
            }
        }
        col = 0;
        Row rowTotalRetenciones = sheet.createRow(filaActual);
        rowTotalRetenciones.createCell(0).setCellValue(new HSSFRichTextString("Total de retenciones"));
        rowTotalRetenciones.getCell(0).setCellStyle(styleSubTitle2);
        filaActual++;

        Row rowCabecera = sheet.createRow(filaActual);
        filaActual++;
        rowCabecera.createCell(col).setCellValue(new HSSFRichTextString("Tiempo"));
        rowCabecera.getCell(col).setCellStyle(styleSubTitle);
        col++;
        rowCabecera.createCell(col).setCellValue(new HSSFRichTextString("Nro. Retención"));
        rowCabecera.getCell(col).setCellStyle(styleSubTitle);
        col++;
        rowCabecera.createCell(col).setCellValue(new HSSFRichTextString("Nro. Factura"));
        rowCabecera.getCell(col).setCellStyle(styleSubTitle);
        col++;
        rowCabecera.createCell(col).setCellValue(new HSSFRichTextString("Cliente"));
        rowCabecera.getCell(col).setCellStyle(styleSubTitle);
        col++;
        rowCabecera.createCell(col).setCellValue(new HSSFRichTextString("Funcionario"));
        rowCabecera.getCell(col).setCellStyle(styleSubTitle);
        col++;
        rowCabecera.createCell(col).setCellValue(new HSSFRichTextString("Retención(%)"));
        rowCabecera.getCell(col).setCellStyle(styleSubTitle);
        col++;
        rowCabecera.createCell(col).setCellValue(new HSSFRichTextString("Monto"));
        rowCabecera.getCell(col).setCellStyle(styleSubTitle);
        col++;
        //FIN CUERPO
        Integer total = 0;
        //TOTAL INGRESOS

        for (E_retencionVenta unaRetencion : cabecera) {
            Row row = sheet.createRow(filaActual);
            col = 0;
            row.createCell(col).setCellValue(unaRetencion.getTiempo());
            row.getCell(col).setCellStyle(styleDate);
            col++;
            row.createCell(col).setCellValue(unaRetencion.getNroRetencion());
            row.getCell(col).setCellStyle(styleNumberInteger);
            col++;
            row.createCell(col).setCellValue(unaRetencion.getVenta().getNroFactura());
            row.getCell(col).setCellStyle(styleNumberInteger);
            col++;
            row.createCell(col).setCellValue(unaRetencion.getVenta().getCliente().getEntidad());
            col++;
            row.createCell(col).setCellValue(unaRetencion.getFuncionario().getNombre());
            col++;
            row.createCell(col).setCellValue(unaRetencion.getPorcentaje());
            row.getCell(col).setCellStyle(styleNumberFloatPoint);
            col++;
            row.createCell(col).setCellValue(unaRetencion.getMonto());
            row.getCell(col).setCellStyle(styleNumberInteger);
            col++;
            total = total + unaRetencion.getMonto();
            filaActual++;
        }
        rowTotalRetenciones.createCell(1).setCellValue(total);
        rowTotalRetenciones.getCell(1).setCellStyle(styleNumberInteger);
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(4);
        sheet.autoSizeColumn(5);
        sheet.autoSizeColumn(6);
        sheet.autoSizeColumn(7);
        try {
            FileOutputStream out = new FileOutputStream(directory.getPath() + ".xls");
            workbook.write(out);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
