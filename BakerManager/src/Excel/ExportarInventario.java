/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Excel;

import DB.DB_Inventario;
import DB.DB_NotaCredito;
import Entities.E_NotaCreditoCabecera;
import Entities.E_NotaCreditoDetalle;
import Entities.E_ajusteStockCabecera;
import Entities.E_impuesto;
import Entities.SeleccionAjusteStockDetalle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JFileChooser;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFRegionUtil;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 *
 * @author Ramiro Ferreira
 */
public class ExportarInventario {

    String nombreHoja;
    E_ajusteStockCabecera notaCreditoCabecera;
    Date fechaInic, fechaFinal;
    HSSFWorkbook workbook;
    HSSFSheet sheet;
    CellStyle styleSubTitle, styleSubTitle2, styleNumberFloatPoint, styleNumberInteger;
    HSSFCellStyle styleDate;

    public ExportarInventario(String nombreHoja, E_ajusteStockCabecera cabecera) {
        this.nombreHoja = nombreHoja;
        this.notaCreditoCabecera = cabecera;
        this.fechaInic = Calendar.getInstance().getTime();
        this.fechaFinal = cabecera.getTiempoFin();
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

    public void exportacionCompleta() {
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
        Row fechaInicio = sheet.createRow(filaActual++);
        //INICIO CAMPO DE FECHAS
        fechaInicio.createCell(col++).setCellValue(new HSSFRichTextString("Fecha"));
        fechaInicio.createCell(col).setCellValue(fechaInic);
        fechaInicio.getCell(col).setCellStyle(styleDate);
        //FIN CAMPO DE FECHAS

        //INICIO CAMPO DE TOTAL INGRESOS
        Row rowTotalProductos = sheet.createRow(filaActual++);
        double totalProductos = 0;
        rowTotalProductos.createCell(0).setCellValue(new HSSFRichTextString("Total productos"));
        rowTotalProductos.getCell(0).setCellStyle(styleSubTitle2);

        filaActual++;
        //FIN CAMPO DE TOTAL INGRESOS
        //INICIO CUERPO DE DATOS
        //INICIO CABECERA DE DATOS
        Row rowCabeceraIDFechaInicio = sheet.createRow(filaActual++);
        col = 0;
        rowCabeceraIDFechaInicio.createCell(col).setCellValue(new HSSFRichTextString("ID"));
        rowCabeceraIDFechaInicio.getCell(col++).setCellStyle(styleSubTitle2);
        rowCabeceraIDFechaInicio.createCell(col).setCellValue(notaCreditoCabecera.getId());
        rowCabeceraIDFechaInicio.getCell(col++).setCellStyle(styleNumberInteger);
        rowCabeceraIDFechaInicio.createCell(col).setCellValue(new HSSFRichTextString("Fecha inicio"));
        rowCabeceraIDFechaInicio.getCell(col++).setCellStyle(styleSubTitle2);
        rowCabeceraIDFechaInicio.createCell(col).setCellValue(notaCreditoCabecera.getTiempoInicio());
        rowCabeceraIDFechaInicio.getCell(col++).setCellStyle(styleDate);

        Row rowCabeceraResponsableFechaFin = sheet.createRow(filaActual++);
        col = 0;
        rowCabeceraResponsableFechaFin.createCell(col).setCellValue(new HSSFRichTextString("Responsable"));
        rowCabeceraResponsableFechaFin.getCell(col++).setCellStyle(styleSubTitle2);
        rowCabeceraResponsableFechaFin.createCell(col++).setCellValue(notaCreditoCabecera.getResponsable().getNombreCompleto());
        rowCabeceraResponsableFechaFin.createCell(col).setCellValue(new HSSFRichTextString("Fecha fin"));
        rowCabeceraResponsableFechaFin.getCell(col++).setCellStyle(styleSubTitle2);
        rowCabeceraResponsableFechaFin.createCell(col).setCellValue(notaCreditoCabecera.getTiempoFin());
        rowCabeceraResponsableFechaFin.getCell(col++).setCellStyle(styleDate);

        Row rowCabeceraFuncionarioObs = sheet.createRow(filaActual++);
        col = 0;
        rowCabeceraFuncionarioObs.createCell(col).setCellValue(new HSSFRichTextString("Registrado por"));
        rowCabeceraFuncionarioObs.getCell(col++).setCellStyle(styleSubTitle2);
        rowCabeceraFuncionarioObs.createCell(col++).setCellValue(notaCreditoCabecera.getRegistradoPor().getNombreCompleto());
        rowCabeceraFuncionarioObs.createCell(col).setCellValue(new HSSFRichTextString("Obs"));
        rowCabeceraFuncionarioObs.getCell(col++).setCellStyle(styleSubTitle2);
        rowCabeceraFuncionarioObs.createCell(col++).setCellValue(notaCreditoCabecera.getObservacion());
        //FIN CABECERA DE DATOS
        List<SeleccionAjusteStockDetalle> detalles = new ArrayList<>(DB_Inventario.consultarAjusteStockDetalle(notaCreditoCabecera.getId()));

        //INICIO CABECERA DETALLE
        Row rowCabeceraDetalle = sheet.createRow(filaActual++);
        col = 0;
        rowCabeceraDetalle.createCell(col).setCellValue(new HSSFRichTextString("ID"));
        rowCabeceraDetalle.getCell(col++).setCellStyle(styleSubTitle);
        rowCabeceraDetalle.createCell(col).setCellValue(new HSSFRichTextString("Código"));
        rowCabeceraDetalle.getCell(col++).setCellStyle(styleSubTitle);
        rowCabeceraDetalle.createCell(col).setCellValue(new HSSFRichTextString("Descripción"));
        rowCabeceraDetalle.getCell(col++).setCellStyle(styleSubTitle);
        rowCabeceraDetalle.createCell(col).setCellValue(new HSSFRichTextString("Cant. anterior"));
        rowCabeceraDetalle.getCell(col++).setCellStyle(styleSubTitle);
        rowCabeceraDetalle.createCell(col).setCellValue(new HSSFRichTextString("Cant. nueva(N)"));
        rowCabeceraDetalle.getCell(col++).setCellStyle(styleSubTitle);
        rowCabeceraDetalle.createCell(col).setCellValue(new HSSFRichTextString("Tiempo"));
        rowCabeceraDetalle.getCell(col++).setCellStyle(styleSubTitle);
        rowCabeceraDetalle.createCell(col).setCellValue(new HSSFRichTextString("Motivo"));
        rowCabeceraDetalle.getCell(col++).setCellStyle(styleSubTitle);
        rowCabeceraDetalle.createCell(col).setCellValue(new HSSFRichTextString("Incluir mov."));
        rowCabeceraDetalle.getCell(col++).setCellStyle(styleSubTitle);
        rowCabeceraDetalle.createCell(col).setCellValue(new HSSFRichTextString("Cant. mov.(M)"));
        rowCabeceraDetalle.getCell(col++).setCellStyle(styleSubTitle);
        rowCabeceraDetalle.createCell(col).setCellValue(new HSSFRichTextString("(N)+(M)"));
        rowCabeceraDetalle.getCell(col++).setCellStyle(styleSubTitle);
        rowCabeceraDetalle.createCell(col).setCellValue(new HSSFRichTextString("Observación"));
        rowCabeceraDetalle.getCell(col++).setCellStyle(styleSubTitle);
        //FIN CABECERA DETALLE
        for (SeleccionAjusteStockDetalle unDetalle : detalles) {
            totalProductos++;
            Row rowDetalle = sheet.createRow(filaActual);
            filaActual++;
            int colIndex = 0;
            rowDetalle.createCell(colIndex).setCellValue(unDetalle.getProducto().getId());
            rowDetalle.getCell(colIndex++).setCellStyle(styleNumberInteger);
            rowDetalle.createCell(colIndex++).setCellValue(unDetalle.getProducto().getCodigo());
            rowDetalle.createCell(colIndex++).setCellValue(unDetalle.getProducto().getDescripcion());
            rowDetalle.createCell(colIndex++).setCellValue(unDetalle.getCantidadVieja());
            rowDetalle.createCell(colIndex++).setCellValue(unDetalle.getCantidadNueva());
            rowDetalle.createCell(colIndex).setCellValue(unDetalle.getTiempoRegistro());
            rowDetalle.getCell(colIndex++).setCellStyle(styleDate);
            rowDetalle.createCell(colIndex++).setCellValue(unDetalle.getMotivo().getDescripcion());
            rowDetalle.createCell(colIndex++).setCellValue(unDetalle.isEstaSeleccionado());
            rowDetalle.createCell(colIndex++).setCellValue(unDetalle.getCantidadMovimiento());
            rowDetalle.createCell(colIndex++).setCellValue(unDetalle.getCantidadMovimiento() + unDetalle.getCantidadNueva());
            if (unDetalle.getObservacion() != null && unDetalle.getObservacion().isEmpty()) {
                String obs = unDetalle.getObservacion();
                rowDetalle.createCell(colIndex).setCellValue(new HSSFRichTextString(obs));
            }
            //FIN
            rowTotalProductos.createCell(1).setCellValue(totalProductos);
            rowTotalProductos.getCell(1).setCellStyle(styleNumberInteger);
            //FIN CUERPO DE DATOS

            //INICIO AJUSTAR COLUMNAS
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
            sheet.autoSizeColumn(5);
            sheet.autoSizeColumn(6);
            sheet.autoSizeColumn(7);
            sheet.autoSizeColumn(8);
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
}
