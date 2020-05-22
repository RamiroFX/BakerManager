/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Excel;

import DB.DB_Cobro;
import Entities.E_cuentaCorrienteCabecera;
import Entities.E_cuentaCorrienteDetalle;
import Entities.M_cliente;
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
public class ExportarEstadoCuentaCliente {

    String nombreHoja;
    Date fechaInic, fechaFinal;
    M_cliente cliente;
    ArrayList<E_cuentaCorrienteCabecera> cabeceraList;
    HSSFWorkbook workbook;
    HSSFSheet sheet;
    CellStyle style1, style2, style3, style4, style5;
    HSSFCellStyle dateCellStyle;

    public ExportarEstadoCuentaCliente(String nombreHoja, M_cliente cliente) {
        this.nombreHoja = nombreHoja;
        this.cliente = cliente;
        createWorkBook();
        createCellStyles();
    }

    private void inicializarVariables() {
        this.fechaInic = cabeceraList.get(0).getFechaPago();
        this.fechaFinal = cabeceraList.get(cabeceraList.size() - 1).getFechaPago();

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
        for (int q = 0; q < cabeceraList.size(); q++) {
            E_cuentaCorrienteCabecera cabecera = cabeceraList.get(q);

            //INICIO RECIBO COBRO CABECERA
            Row produccionCabeceraRowFecha = sheet.createRow(filaActual);
            filaActual++;
            produccionCabeceraRowFecha.createCell(0).setCellValue(new HSSFRichTextString("Fecha"));
            produccionCabeceraRowFecha.createCell(1).setCellValue(cabecera.getFechaPago());
            produccionCabeceraRowFecha.getCell(0).setCellStyle(style5);
            produccionCabeceraRowFecha.getCell(1).setCellStyle(dateCellStyle);

            Row produccionCabeceraRowResp = sheet.createRow(filaActual);
            filaActual++;
            produccionCabeceraRowResp.createCell(0).setCellValue(new HSSFRichTextString("Cliente"));
            produccionCabeceraRowResp.createCell(1).setCellValue(cabecera.getCliente().getEntidad());
            produccionCabeceraRowResp.getCell(0).setCellStyle(style5);

            Row produccionCabeceraRowReg = sheet.createRow(filaActual);
            filaActual++;
            produccionCabeceraRowReg.createCell(0).setCellValue(new HSSFRichTextString("Cobrador"));
            produccionCabeceraRowReg.createCell(1).setCellValue(cabecera.getCobrador().getNombre());
            produccionCabeceraRowReg.getCell(0).setCellStyle(style5);

            Row produccionCabeceraRowOT = sheet.createRow(filaActual);
            filaActual++;
            produccionCabeceraRowOT.createCell(0).setCellValue(new HSSFRichTextString("Nro recibo"));
            produccionCabeceraRowOT.createCell(1).setCellValue(cabecera.getNroRecibo());
            produccionCabeceraRowOT.getCell(0).setCellStyle(style5);

            Row rowTotal = sheet.createRow(filaActual);
            filaActual++;
            rowTotal.createCell(0).setCellValue(new HSSFRichTextString("Total cobrado"));
            rowTotal.getCell(0).setCellStyle(style5);
            //FIN RECIBO COBRO CABECERA

            //INICIO RECIBO COBRO CABECERA DETALLE
            Row cabeceraProduccionDetalle = sheet.createRow(filaActual);
            filaActual++;
            cabeceraProduccionDetalle.createCell(0).setCellValue(new HSSFRichTextString("Monto"));
            cabeceraProduccionDetalle.getCell(0).setCellStyle(style1);
            cabeceraProduccionDetalle.createCell(1).setCellValue(new HSSFRichTextString("ID venta"));
            cabeceraProduccionDetalle.getCell(1).setCellStyle(style1);
            cabeceraProduccionDetalle.createCell(2).setCellValue(new HSSFRichTextString("Nro Factura"));
            cabeceraProduccionDetalle.getCell(2).setCellStyle(style1);
            cabeceraProduccionDetalle.createCell(3).setCellValue(new HSSFRichTextString("Nro Cheque"));
            cabeceraProduccionDetalle.getCell(3).setCellStyle(style1);
            cabeceraProduccionDetalle.createCell(4).setCellValue(new HSSFRichTextString("Banco"));
            cabeceraProduccionDetalle.getCell(4).setCellStyle(style1);
            cabeceraProduccionDetalle.createCell(5).setCellValue(new HSSFRichTextString("Fecha cheque"));
            cabeceraProduccionDetalle.getCell(5).setCellStyle(style1);
            cabeceraProduccionDetalle.createCell(6).setCellValue(new HSSFRichTextString("Fecha Cheque diferido"));
            cabeceraProduccionDetalle.getCell(6).setCellStyle(style1);
            //FIN CABECERA PRODUCCION DETALLE

            //INICIO DETALLE PRODUCCION DETALLE
            ArrayList<E_cuentaCorrienteDetalle> prodDetalleList;
            prodDetalleList = new ArrayList<>(DB_Cobro.obtenerCobroDetalle(cabecera.getId()));
            int total = 0;
            for (int i = 0; i < prodDetalleList.size(); i++) {
                Row filaProdDetalle = sheet.createRow(filaActual);
                filaActual++;
                E_cuentaCorrienteDetalle unDetalle = prodDetalleList.get(i);
                filaProdDetalle.createCell(0).setCellValue(unDetalle.getMonto());
                total = total + (int) unDetalle.getMonto();
                filaProdDetalle.getCell(0).setCellStyle(style4);
                filaProdDetalle.createCell(1).setCellValue(unDetalle.getIdFacturaCabecera());
                filaProdDetalle.createCell(2).setCellValue(unDetalle.getNroFactura());
                filaProdDetalle.createCell(3).setCellValue(unDetalle.getNroCheque());
                filaProdDetalle.createCell(4).setCellValue(unDetalle.getBanco().getDescripcion());
                if (unDetalle.getFechaCheque() != null) {
                    filaProdDetalle.createCell(5).setCellValue(unDetalle.getFechaCheque());
                    filaProdDetalle.getCell(5).setCellStyle(dateCellStyle);
                } else {
                    filaProdDetalle.createCell(5).setCellValue("");
                }
                if (unDetalle.getFechaDiferidaCheque() != null) {
                    filaProdDetalle.createCell(6).setCellValue(unDetalle.getFechaDiferidaCheque());
                    filaProdDetalle.getCell(6).setCellStyle(dateCellStyle);
                } else {
                    filaProdDetalle.createCell(6).setCellValue("");
                }
            }
            rowTotal.createCell(1).setCellValue(total);
            rowTotal.getCell(1).setCellStyle(style4);
            filaActual++;
        }

        //INICIO AJUSTAR COLUMNAS
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(4);
        sheet.autoSizeColumn(5);
        sheet.autoSizeColumn(6);
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
        Row totalCobrado = sheet.createRow(filaActual);
        totalCobrado.createCell(0).setCellValue(new HSSFRichTextString("Total cobrado:"));
        filaActual++;
        //INICIO CABECERA PRODUCCION DETALLE
        Row cabeceraCobroDetalle = sheet.createRow(filaActual);
        filaActual++;
        cabeceraCobroDetalle.createCell(0).setCellValue(new HSSFRichTextString("Cliente"));
        cabeceraCobroDetalle.getCell(0).setCellStyle(style1);
        cabeceraCobroDetalle.createCell(1).setCellValue(new HSSFRichTextString("Fecha"));
        cabeceraCobroDetalle.getCell(1).setCellStyle(style1);
        cabeceraCobroDetalle.createCell(2).setCellValue(new HSSFRichTextString("Nro Recibo"));
        cabeceraCobroDetalle.getCell(2).setCellStyle(style1);
        cabeceraCobroDetalle.createCell(3).setCellValue(new HSSFRichTextString("Monto"));
        cabeceraCobroDetalle.getCell(3).setCellStyle(style1);
        //FIN CABECERA PRODUCCION DETALLE

        //INICIO DETALLE PRODUCCION DETALLE
        int total = 0;
        for (E_cuentaCorrienteCabecera cabecera : cabeceraList) {
            Row rowDetalleCobro = sheet.createRow(filaActual);
            filaActual++;
            rowDetalleCobro.createCell(0).setCellValue(cabecera.getCliente().getEntidad());
            rowDetalleCobro.createCell(1).setCellValue(cabecera.getFechaPago());
            rowDetalleCobro.getCell(1).setCellStyle(dateCellStyle);
            rowDetalleCobro.createCell(2).setCellValue(cabecera.getNroRecibo());
            rowDetalleCobro.createCell(3).setCellValue(cabecera.getDebito());
            rowDetalleCobro.getCell(3).setCellStyle(style4);
            total = total + cabecera.getDebito();
        }
        totalCobrado.createCell(1).setCellValue(total);
        totalCobrado.getCell(1).setCellStyle(style4);
        filaActual++;

        //INICIO AJUSTAR COLUMNAS
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
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
