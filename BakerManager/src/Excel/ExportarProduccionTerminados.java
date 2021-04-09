/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Excel;

import Entities.E_produccionDetallePlus;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
public class ExportarProduccionTerminados {

    String nombreHoja;
    Date fechaFin, fechaInicio;
    List<E_produccionDetallePlus> prodDetallePlusList;
    HSSFWorkbook workbook;
    HSSFSheet sheet;
    private ArrayList<HSSFSheet> sheets;
    CellStyle style1, style2, styleNumber1, styleNumber2, style5;
    HSSFCellStyle dateCellStyle;
    boolean conFecha;

    public ExportarProduccionTerminados(String nombreHoja, boolean conFecha, Date fechaInicio, Date fechaFin, List<E_produccionDetallePlus> prodDetallePlusList) {
        this.nombreHoja = nombreHoja;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.conFecha = conFecha;
        this.prodDetallePlusList = prodDetallePlusList;
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
        styleNumber1 = workbook.createCellStyle();
        styleNumber1.setDataFormat(format.getFormat("#,##0.00"));

        styleNumber2 = workbook.createCellStyle();
        styleNumber2.setDataFormat(format.getFormat("#,##0"));

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
        int col = 0;
        if (conFecha) {
            Row rowfechaInicio = sheet.createRow(filaActual);
            rowfechaInicio.createCell(0).setCellValue(new HSSFRichTextString("Fecha inicio:"));
            rowfechaInicio.createCell(1).setCellValue(this.fechaInicio);
            rowfechaInicio.getCell(1).setCellStyle(dateCellStyle);
            filaActual++;
            Row rowfechaFin = sheet.createRow(filaActual);
            rowfechaFin.createCell(0).setCellValue(new HSSFRichTextString("Fecha fin:"));
            rowfechaFin.createCell(1).setCellValue(this.fechaFin);
            rowfechaFin.getCell(1).setCellStyle(dateCellStyle);
            filaActual++;
        }
        //INICIO CABECERA PRODUCCION DETALLE
        Row cabeceraProduccionDetalle = sheet.createRow(filaActual);
        filaActual++;

        cabeceraProduccionDetalle.createCell(col).setCellValue(new HSSFRichTextString("Cod."));
        cabeceraProduccionDetalle.getCell(col++).setCellStyle(style1);
        cabeceraProduccionDetalle.createCell(col).setCellValue(new HSSFRichTextString("Producto"));
        cabeceraProduccionDetalle.getCell(col++).setCellStyle(style1);
        cabeceraProduccionDetalle.createCell(col).setCellValue(new HSSFRichTextString("Cantidad producida"));
        cabeceraProduccionDetalle.getCell(col++).setCellStyle(style1);
        cabeceraProduccionDetalle.createCell(col).setCellValue(new HSSFRichTextString("Cantidad vendida"));
        cabeceraProduccionDetalle.getCell(col++).setCellStyle(style1);
        cabeceraProduccionDetalle.createCell(col).setCellValue(new HSSFRichTextString("Cantidad actual"));
        cabeceraProduccionDetalle.getCell(col).setCellStyle(style1);
        //FIN CABECERA PRODUCCION DETALLE

        //INICIO DETALLE PRODUCCION DETALLE
        for (int i = 0; i < this.prodDetallePlusList.size(); i++) {
            Row filaProdDetalle = sheet.createRow(filaActual);
            filaActual++;
            E_produccionDetallePlus get = prodDetallePlusList.get(i);
            filaProdDetalle.createCell(0).setCellValue(get.getProducto().getCodigo());
            filaProdDetalle.getCell(0).setCellStyle(styleNumber2);
            filaProdDetalle.createCell(1).setCellValue(get.getProducto().getDescripcion());
            filaProdDetalle.createCell(2).setCellValue(get.getCantidad());
            filaProdDetalle.getCell(2).setCellStyle(styleNumber1);
            filaProdDetalle.createCell(3).setCellValue(get.getCantidadVendida());
            filaProdDetalle.getCell(3).setCellStyle(styleNumber1);
            filaProdDetalle.createCell(4).setCellValue(get.getProducto().getCantActual());
            filaProdDetalle.getCell(4).setCellStyle(styleNumber1);
        }
        filaActual++;

        //INICIO AJUSTAR COLUMNAS
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(4);
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
