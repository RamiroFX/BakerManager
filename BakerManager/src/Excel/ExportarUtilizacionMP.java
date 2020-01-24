/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Excel;

import DB.DB_UtilizacionMateriaPrima;
import Entities.E_utilizacionMateriaPrimaCabecera;
import Entities.E_utilizacionMateriaPrimaDetalle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JFileChooser;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
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
public class ExportarUtilizacionMP {

    String nombreHoja;
    Date fechaInic, fechaFinal;
    ArrayList<E_utilizacionMateriaPrimaCabecera> prodCabeceraList;
    HSSFWorkbook workbook;
    HSSFSheet sheet;
    CellStyle style1, style2, style3, style4, style5;
    HSSFCellStyle dateCellStyle;

    public ExportarUtilizacionMP(String nombreHoja, Date inicio, Date fin, ArrayList<E_utilizacionMateriaPrimaCabecera> prodCabeceraList) {
        this.nombreHoja = nombreHoja;
        this.fechaInic = inicio;
        this.prodCabeceraList = prodCabeceraList;
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

        HSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setItalic(false);
        style5 = workbook.createCellStyle();
        style5.setFont(font);
        //END FORMAT STYLE
    }

    public void exportacionIndividual() {
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
        for (int q = 0; q < prodCabeceraList.size(); q++) {
            E_utilizacionMateriaPrimaCabecera prodCabecera = prodCabeceraList.get(q);

            //INICIO PRODUCCION CABECERA
            Row produccionCabeceraRowFecha = sheet.createRow(filaActual);
            filaActual++;
            produccionCabeceraRowFecha.createCell(0).setCellValue(new HSSFRichTextString("Fecha"));
            produccionCabeceraRowFecha.createCell(1).setCellValue(prodCabecera.getFechaUtilizacion());
            produccionCabeceraRowFecha.getCell(0).setCellStyle(style5);
            produccionCabeceraRowFecha.getCell(1).setCellStyle(dateCellStyle);

            Row produccionCabeceraRowResp = sheet.createRow(filaActual);
            filaActual++;
            produccionCabeceraRowResp.createCell(0).setCellValue(new HSSFRichTextString("Responsable"));
            produccionCabeceraRowResp.createCell(1).setCellValue(prodCabecera.getFuncionarioProduccion().getNombre());
            produccionCabeceraRowResp.getCell(0).setCellStyle(style5);

            Row produccionCabeceraRowReg = sheet.createRow(filaActual);
            filaActual++;
            produccionCabeceraRowReg.createCell(0).setCellValue(new HSSFRichTextString("Registrado por"));
            produccionCabeceraRowReg.createCell(1).setCellValue(prodCabecera.getFuncionarioSistema().getNombre());
            produccionCabeceraRowReg.getCell(0).setCellStyle(style5);

            Row produccionCabeceraRowOT = sheet.createRow(filaActual);
            filaActual++;
            produccionCabeceraRowOT.createCell(0).setCellValue(new HSSFRichTextString("Nro Orden trabajo"));
            produccionCabeceraRowOT.createCell(1).setCellValue(prodCabecera.getNroOrdenTrabajo());
            produccionCabeceraRowOT.getCell(0).setCellStyle(style5);
            //FIN PRODUCCION CABECERA

            //INICIO CABECERA PRODUCCION DETALLE
            Row cabeceraProduccionDetalle = sheet.createRow(filaActual);
            filaActual++;
            cabeceraProduccionDetalle.createCell(0).setCellValue(new HSSFRichTextString("Cod."));
            cabeceraProduccionDetalle.getCell(0).setCellStyle(style1);
            cabeceraProduccionDetalle.createCell(1).setCellValue(new HSSFRichTextString("Producto"));
            cabeceraProduccionDetalle.getCell(1).setCellStyle(style1);
            cabeceraProduccionDetalle.createCell(2).setCellValue(new HSSFRichTextString("Cantidad"));
            cabeceraProduccionDetalle.getCell(2).setCellStyle(style1);
            //FIN CABECERA PRODUCCION DETALLE

            //INICIO DETALLE PRODUCCION DETALLE
            ArrayList<E_utilizacionMateriaPrimaDetalle> prodDetalleList;
            prodDetalleList = new ArrayList<>(DB_UtilizacionMateriaPrima.consultarUtilizacionMateriaPrimaDetalle(prodCabecera.getId()));
            for (int i = 0; i < prodDetalleList.size(); i++) {
                Row filaProdDetalle = sheet.createRow(filaActual);
                filaActual++;
                E_utilizacionMateriaPrimaDetalle get = prodDetalleList.get(i);
                filaProdDetalle.createCell(0).setCellValue(get.getProducto().getCodigo());
                filaProdDetalle.createCell(1).setCellValue(get.getProducto().getDescripcion());
                filaProdDetalle.createCell(2).setCellValue(get.getCantidad());
            }
            filaActual++;
        }

        //INICIO AJUSTAR COLUMNAS
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
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

    public void exportacionAgrupada() {
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
        fechaInicio.createCell(0).setCellValue(new HSSFRichTextString("Fecha inicio:"));
        fechaInicio.createCell(1).setCellValue(fechaInic);
        fechaInicio.getCell(1).setCellStyle(dateCellStyle);
        filaActual++;
        Row fechaFin = sheet.createRow(filaActual);
        fechaFin.createCell(0).setCellValue(new HSSFRichTextString("Fecha fin:"));
        fechaFin.createCell(1).setCellValue(fechaFinal);
        fechaFin.getCell(1).setCellStyle(dateCellStyle);
        filaActual++;
        //INICIO CABECERA PRODUCCION DETALLE
        Row cabeceraProduccionDetalle = sheet.createRow(filaActual);
        filaActual++;
        cabeceraProduccionDetalle.createCell(0).setCellValue(new HSSFRichTextString("Cod."));
        cabeceraProduccionDetalle.getCell(0).setCellStyle(style1);
        cabeceraProduccionDetalle.createCell(1).setCellValue(new HSSFRichTextString("Producto"));
        cabeceraProduccionDetalle.getCell(1).setCellStyle(style1);
        cabeceraProduccionDetalle.createCell(2).setCellValue(new HSSFRichTextString("Cantidad"));
        cabeceraProduccionDetalle.getCell(2).setCellStyle(style1);
        //FIN CABECERA PRODUCCION DETALLE

        //INICIO DETALLE PRODUCCION DETALLE
        ArrayList<E_utilizacionMateriaPrimaDetalle> prodDetalleList;
        prodDetalleList = new ArrayList<>(DB_UtilizacionMateriaPrima.consultarUtilizacionMateriaPrimaDetalleAgrupado(prodCabeceraList));
        for (int i = 0; i < prodDetalleList.size(); i++) {
            Row filaProdDetalle = sheet.createRow(filaActual);
            filaActual++;
            E_utilizacionMateriaPrimaDetalle get = prodDetalleList.get(i);
            filaProdDetalle.createCell(0).setCellValue(get.getProducto().getCodigo());
            filaProdDetalle.createCell(1).setCellValue(get.getProducto().getDescripcion());
            filaProdDetalle.createCell(2).setCellValue(get.getCantidad());
        }
        filaActual++;

        //INICIO AJUSTAR COLUMNAS
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
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

}
