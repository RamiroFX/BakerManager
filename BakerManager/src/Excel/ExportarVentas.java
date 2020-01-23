/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Excel;

import Entities.E_facturaCabeceraFX;
import Entities.E_facturaDetalleFX;
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
 * @author Ramiro
 */
public class ExportarVentas {

    String nombreHoja;
    ArrayList<E_facturaDetalleFX> facturaDetalle;
    ArrayList<E_facturaCabeceraFX> facturaCabeceraFX;
    Date fechaInic, fechaFinal;
    HSSFWorkbook workbook;
    HSSFSheet sheet;
    CellStyle style1, style2, style3, style4;
    HSSFCellStyle dateCellStyle;
    int tipo_fecha;

    /*
    TIPO
    1=COMPLETO
    2=RESUMIDO
     */
    public ExportarVentas(String nombreHoja, ArrayList<E_facturaDetalleFX> ed, Date inicio, Date fin, int tipo) {
        this.nombreHoja = nombreHoja;
        this.facturaDetalle = ed;
        this.fechaInic = inicio;
        this.fechaFinal = fin;
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
        style1 = workbook.createCellStyle();
        style1.setFillForegroundColor(IndexedColors.GOLD.getIndex());
        style1.setFillPattern(CellStyle.SOLID_FOREGROUND);

        // Orange "foreground", foreground being the fill foreground not the font color.
        style2 = workbook.createCellStyle();
        style2.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        style2.setFillPattern(CellStyle.SOLID_FOREGROUND);
        //END STYLE
        // FORMAT STYLE
        DataFormat format = workbook.createDataFormat();
        style3 = workbook.createCellStyle();
        style3.setDataFormat(format.getFormat("0.0"));

        style4 = workbook.createCellStyle();
        style4.setDataFormat(format.getFormat("#,##0"));

        dateCellStyle = workbook.createCellStyle();
        short df = workbook.createDataFormat().getFormat("dd-MM-yyyy");
        dateCellStyle.setDataFormat(df);
        //END FORMAT STYLE
    }

    public void initComp() {
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
        Row fechaInicio = sheet.createRow(filaActual);
        filaActual++;
        Row fechaFin = null;
        int col = 0;
        //INICIO CAMPO DE FECHAS
        if (fechaInic != null && fechaFinal != null) {
            int dateType = fechaInic.compareTo(fechaFinal);
            if (dateType == 0) {
                fechaInicio.createCell(col).setCellValue(new HSSFRichTextString("Fecha :"));
                col++;
                fechaInicio.createCell(col).setCellValue(fechaInic);
                fechaInicio.getCell(col).setCellStyle(dateCellStyle);
            } else {
                fechaFin = sheet.createRow(filaActual);
                col = 0;
                filaActual++;
                fechaInicio.createCell(col).setCellValue(new HSSFRichTextString("Fecha inicio:"));
                fechaFin.createCell(col).setCellValue(new HSSFRichTextString("Fecha fin:"));
                col++;
                fechaInicio.createCell(col).setCellValue(fechaInic);
                fechaFin.createCell(col).setCellValue(fechaFinal);
                fechaFin.getCell(col).setCellStyle(dateCellStyle);
                fechaInicio.getCell(col).setCellStyle(dateCellStyle);
            }
        }
        //FIN CAMPO DE FECHAS

        //INICIO CAMPO DE TOTAL INGRESOS
        Row totalIngresos2 = sheet.createRow(filaActual);
        filaActual++;
        Integer total = 0;
        Integer SubTotal = 0;
        totalIngresos2.createCell(0).setCellValue(new HSSFRichTextString("Total ingresos"));
        totalIngresos2.getCell(0).setCellStyle(style2);
        for (int i = 0; i < facturaDetalle.size(); i++) {
            SubTotal = SubTotal + (facturaDetalle.get(i).getTotal());
        }
        total = total + SubTotal;
        totalIngresos2.createCell(1).setCellValue(total);
        //FIN CAMPO DE TOTAL INGRESOS

        //INICIO CABECERA DE DATOS
        Row cabecera = sheet.createRow(filaActual);
        col = 0;
        filaActual++;
        cabecera.createCell(col).setCellValue(new HSSFRichTextString("Cliente"));
        cabecera.getCell(col).setCellStyle(style1);
        col++;
        cabecera.createCell(col).setCellValue(new HSSFRichTextString("Descripción"));
        cabecera.getCell(col).setCellStyle(style1);
        col++;
        cabecera.createCell(col).setCellValue(new HSSFRichTextString("Cantidad"));
        cabecera.getCell(col).setCellStyle(style1);
        col++;
        cabecera.createCell(col).setCellValue(new HSSFRichTextString("Descuento"));
        cabecera.getCell(col).setCellStyle(style1);
        col++;
        cabecera.createCell(col).setCellValue(new HSSFRichTextString("Precio"));
        cabecera.getCell(col).setCellStyle(style1);
        col++;
        cabecera.createCell(col).setCellValue(new HSSFRichTextString("Sub-total"));
        cabecera.getCell(col).setCellStyle(style1);
        col++;
        cabecera.createCell(col).setCellValue(new HSSFRichTextString("Total"));
        cabecera.getCell(col).setCellStyle(style1);
        //FIN CABECERA DE DATOS

        //INICIO CUERPO DE DATOS
        SubTotal = 0;
        int idEgCab = facturaDetalle.get(0).getIdFacturaCabecera();
        boolean b = true;
        for (int i = 0; i < facturaDetalle.size(); i++) {
            if (idEgCab == facturaDetalle.get(i).getIdFacturaCabecera()) {
                if (b) {
                    Row asd = sheet.createRow(filaActual);
                    asd.createCell(0).setCellValue(new HSSFRichTextString(facturaDetalle.get(i).getClienteEntidad()));
                    asd.getCell(0).setCellStyle(style2);
                    asd.createCell(1).setCellValue(facturaDetalle.get(i).getTiempo());
                    asd.getCell(1).setCellStyle(dateCellStyle);
                    if (facturaDetalle.get(i).getObservacion() != null) {
                        asd.createCell(2).setCellValue(new HSSFRichTextString(facturaDetalle.get(i).getProductoDescripcion() + "-(" + facturaDetalle.get(i).getObservacion() + ")"));
                    } else {
                        asd.createCell(2).setCellValue(new HSSFRichTextString(facturaDetalle.get(i).getProductoDescripcion()));
                    }
                    asd.createCell(3).setCellValue(facturaDetalle.get(i).getCantidad());
                    asd.createCell(4).setCellValue(facturaDetalle.get(i).getDescuento());
                    asd.createCell(5).setCellValue(facturaDetalle.get(i).getPrecio());
                    asd.createCell(6).setCellValue(facturaDetalle.get(i).getTotal());
                    for (int X = i; X < facturaDetalle.size(); X++) {
                        if (idEgCab == facturaDetalle.get(X).getIdFacturaCabecera()) {
                            SubTotal = SubTotal + facturaDetalle.get(X).getTotal();
                        } else {
                            X = facturaDetalle.size();
                        }
                    }
                    asd.createCell(7).setCellValue(SubTotal);
                    filaActual++;
                }
                if (!b) {
                    Row qwerty = sheet.createRow(filaActual);
                    filaActual++;
                    qwerty.createCell(2).setCellValue(new HSSFRichTextString(facturaDetalle.get(i).getProductoDescripcion()));
                    qwerty.createCell(3).setCellValue(facturaDetalle.get(i).getCantidad());
                    qwerty.createCell(4).setCellValue(facturaDetalle.get(i).getDescuento());
                    qwerty.createCell(5).setCellValue(facturaDetalle.get(i).getPrecio());
                    qwerty.createCell(6).setCellValue(facturaDetalle.get(i).getTotal());
                }
                b = false;
            } else {
                SubTotal = 0;
                b = true;
                idEgCab = facturaDetalle.get(i).getIdFacturaCabecera();
                i--;
                filaActual++;
            }
        }
        //FIN CUERPO DE DATOS

        //INICIO AJUSTAR COLUMNAS
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(4);
        sheet.autoSizeColumn(5);
        sheet.autoSizeColumn(6);
        /*if (tipo_fecha == MULTIPLES_FECHAS) {
            escribirCeldasConFecha(idEgresoCabecera);
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
            sheet.autoSizeColumn(5);
            sheet.autoSizeColumn(6);
        } else {
            escribirCeldasSinFecha(idEgresoCabecera);
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
            sheet.autoSizeColumn(5);
        }*/
        //FIN AJUSTAR COLUMNAS
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

    public void initCompResumidoFX() {
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
                fechaInicio.createCell(col).setCellValue(fechaInic);
                fechaInicio.getCell(col).setCellStyle(dateCellStyle);
                col++;
            } else {
                fechaFin = sheet.createRow(filaActual);
                filaActual++;
                fechaFin.createCell(col).setCellValue(new HSSFRichTextString("Fecha fin:"));
                fechaFin.createCell(col).setCellValue(fechaFinal);
                fechaFin.getCell(col).setCellStyle(dateCellStyle);
                fechaInicio.createCell(col).setCellValue(new HSSFRichTextString("Fecha inicio:"));
                fechaInicio.createCell(col).setCellValue(fechaInic);
                fechaInicio.getCell(col).setCellStyle(dateCellStyle);
            }
        }
        Row totalIngreso2 = sheet.createRow(filaActual);
        filaActual++;
        Row cabecera = sheet.createRow(filaActual);
        filaActual++;

        totalIngreso2.createCell(0).setCellValue(new HSSFRichTextString("Total ingresos"));
        totalIngreso2.getCell(0).setCellStyle(style2);
        cabecera.createCell(col).setCellValue(new HSSFRichTextString("Tiempo"));
        cabecera.getCell(col).setCellStyle(style1);
        col++;
        cabecera.createCell(col).setCellValue(new HSSFRichTextString("Nro. Factura"));
        cabecera.getCell(col).setCellStyle(style1);
        col++;
        cabecera.createCell(col).setCellValue(new HSSFRichTextString("Cliente"));
        cabecera.getCell(col).setCellStyle(style1);
        col++;
        cabecera.createCell(col).setCellValue(new HSSFRichTextString("Total"));
        cabecera.getCell(col).setCellStyle(style1);
        col++;
        cabecera.createCell(col).setCellValue(new HSSFRichTextString("Impuesto"));
        cabecera.getCell(col).setCellStyle(style1);
        col++;
        //FIN CUERPO
        Integer total = 0;
        Integer SubTotal = 0;
        for (int i = 0; i < facturaDetalle.size(); i++) {
            SubTotal = SubTotal + (facturaDetalle.get(i).getTotal());
        }
        total = total + SubTotal;
        //TOTAL INGRESOS

        totalIngreso2.createCell(1).setCellValue(total);
        int idEgresoCabecera = facturaDetalle.get(0).getIdFacturaCabecera();
        SubTotal = 0;
        escribirCeldasConFechaResumido(idEgresoCabecera);
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(4);
        sheet.autoSizeColumn(5);
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

    private void escribirCeldasConFechaResumido(int egresocabecera) {
        System.out.println("Excel.ExportarVentas.escribirCeldasConFecha()");
        int filaActual = 4;
        int idEgCab = egresocabecera;
        System.out.println("facturaDetalle: " + facturaDetalle.size());
        for (int i = 0; i < facturaDetalle.size(); i++) {
            if (idEgCab == facturaDetalle.get(i).getIdFacturaCabecera()) {
                Row asd = sheet.createRow(filaActual);
                int pos = 0;
                asd.createCell(pos).setCellValue(facturaDetalle.get(i).getTiempo());
                asd.getCell(pos).setCellStyle(dateCellStyle);
                pos++;
                asd.createCell(pos).setCellValue(new HSSFRichTextString(facturaDetalle.get(i).getNroFacura() + ""));
                asd.getCell(pos).setCellStyle(style2);
                pos++;
                asd.createCell(pos).setCellValue(new HSSFRichTextString(facturaDetalle.get(i).getClienteEntidad()));
                asd.getCell(pos).setCellStyle(style2);
                pos++;
                //calcular el total e iva total
                int exenta = 0, iva5 = 0, iva10 = 0;
                int SubTotal = 0;
                int TotalImpuesto = 0;
                for (int X = i; X < facturaDetalle.size(); X++) {
                    if (idEgCab == facturaDetalle.get(X).getIdFacturaCabecera()) {
                        exenta = exenta + facturaDetalle.get(X).getExenta();
                        iva5 = iva5 + facturaDetalle.get(X).getIva5();
                        iva10 = iva10 + facturaDetalle.get(X).getIva10();
                        TotalImpuesto = TotalImpuesto + exenta + iva5 + iva10;
                        SubTotal = SubTotal + facturaDetalle.get(X).getTotal();
                        //i++;
                    } else {
                        X = facturaDetalle.size();
                    }
                    //i--;
                }
                asd.createCell(pos).setCellValue(SubTotal);
                pos++;
                asd.createCell(pos).setCellValue(TotalImpuesto);
                filaActual++;
                idEgCab = facturaDetalle.get(i).getIdFacturaCabecera();
            }
        }
    }

}
