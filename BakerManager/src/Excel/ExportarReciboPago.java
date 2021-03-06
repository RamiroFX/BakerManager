/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Excel;

import DB.DB_Pago;
import Entities.E_cuentaCorrienteDetalle;
import Entities.E_reciboPagoCabecera;
import Entities.E_reciboPagoDetalle;
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
public class ExportarReciboPago {

    String nombreHoja;
    Date fechaInic, fechaFinal;
    ArrayList<E_reciboPagoCabecera> cabeceraList;
    HSSFWorkbook workbook;
    HSSFSheet sheet;
    CellStyle titleCellStyle, subTitleCellStyle, style3, decimalCellStyle, style5;
    HSSFCellStyle dateCellStyle;

    public ExportarReciboPago(String nombreHoja, ArrayList<E_reciboPagoCabecera> cabeceraList) {
        this.nombreHoja = nombreHoja;
        //this.fechaInic = inicio;
        this.fechaInic = cabeceraList.get(0).getFechaPago();
        //this.fechaFinal = fin;
        this.fechaFinal = cabeceraList.get(cabeceraList.size() - 1).getFechaPago();
        this.cabeceraList = cabeceraList;
        createWorkBook();
        createCellStyles();
    }

    public ExportarReciboPago(String nombreHoja) {
        this.nombreHoja = nombreHoja;
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
        titleCellStyle = workbook.createCellStyle();
        titleCellStyle.setFillForegroundColor(IndexedColors.GOLD.getIndex());
        titleCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);

        // Orange "foreground", foreground being the fill foreground not the font color.
        subTitleCellStyle = workbook.createCellStyle();
        subTitleCellStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        subTitleCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        //END STYLE
        // FORMAT STYLE
        DataFormat format = workbook.createDataFormat();
        style3 = workbook.createCellStyle();
        style3.setDataFormat(format.getFormat("0.0"));

        decimalCellStyle = workbook.createCellStyle();
        decimalCellStyle.setDataFormat(format.getFormat("#,##0.00"));

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
            E_reciboPagoCabecera cabecera = cabeceraList.get(q);

            //INICIO RECIBO COBRO CABECERA
            Row produccionCabeceraRowFecha = sheet.createRow(filaActual);
            filaActual++;
            produccionCabeceraRowFecha.createCell(0).setCellValue(new HSSFRichTextString("Fecha"));
            produccionCabeceraRowFecha.createCell(1).setCellValue(cabecera.getFechaPago());
            produccionCabeceraRowFecha.getCell(0).setCellStyle(style5);
            produccionCabeceraRowFecha.getCell(1).setCellStyle(dateCellStyle);

            Row produccionCabeceraRowResp = sheet.createRow(filaActual);
            filaActual++;
            produccionCabeceraRowResp.createCell(0).setCellValue(new HSSFRichTextString("Proveedor"));
            produccionCabeceraRowResp.createCell(1).setCellValue(cabecera.getProveedor().getEntidad());
            produccionCabeceraRowResp.getCell(0).setCellStyle(style5);

            Row produccionCabeceraRowOT = sheet.createRow(filaActual);
            filaActual++;
            produccionCabeceraRowOT.createCell(0).setCellValue(new HSSFRichTextString("Nro recibo"));
            produccionCabeceraRowOT.createCell(1).setCellValue(cabecera.getNroRecibo());
            produccionCabeceraRowOT.getCell(0).setCellStyle(style5);

            Row rowTotal = sheet.createRow(filaActual);
            filaActual++;
            rowTotal.createCell(0).setCellValue(new HSSFRichTextString("Total pagado"));
            rowTotal.getCell(0).setCellStyle(style5);
            //FIN RECIBO COBRO CABECERA

            //INICIO RECIBO COBRO CABECERA DETALLE
            Row cabeceraProduccionDetalle = sheet.createRow(filaActual);
            filaActual++;
            cabeceraProduccionDetalle.createCell(0).setCellValue(new HSSFRichTextString("Monto"));
            cabeceraProduccionDetalle.getCell(0).setCellStyle(titleCellStyle);
            cabeceraProduccionDetalle.createCell(1).setCellValue(new HSSFRichTextString("ID compra"));
            cabeceraProduccionDetalle.getCell(1).setCellStyle(titleCellStyle);
            cabeceraProduccionDetalle.createCell(2).setCellValue(new HSSFRichTextString("Nro Factura"));
            cabeceraProduccionDetalle.getCell(2).setCellStyle(titleCellStyle);
            cabeceraProduccionDetalle.createCell(3).setCellValue(new HSSFRichTextString("Nro Cheque"));
            cabeceraProduccionDetalle.getCell(3).setCellStyle(titleCellStyle);
            cabeceraProduccionDetalle.createCell(4).setCellValue(new HSSFRichTextString("Banco"));
            cabeceraProduccionDetalle.getCell(4).setCellStyle(titleCellStyle);
            cabeceraProduccionDetalle.createCell(5).setCellValue(new HSSFRichTextString("Fecha cheque"));
            cabeceraProduccionDetalle.getCell(5).setCellStyle(titleCellStyle);
            cabeceraProduccionDetalle.createCell(6).setCellValue(new HSSFRichTextString("Fecha Cheque diferido"));
            cabeceraProduccionDetalle.getCell(6).setCellStyle(titleCellStyle);
            //FIN CABECERA PRODUCCION DETALLE

            //INICIO DETALLE PRODUCCION DETALLE
            ArrayList<E_reciboPagoDetalle> prodDetalleList;
            prodDetalleList = new ArrayList<>(DB_Pago.obtenerPagoDetalle(cabecera.getId()));
            int total = 0;
            for (int i = 0; i < prodDetalleList.size(); i++) {
                Row filaProdDetalle = sheet.createRow(filaActual);
                filaActual++;
                E_reciboPagoDetalle unDetalle = prodDetalleList.get(i);
                filaProdDetalle.createCell(0).setCellValue(unDetalle.getMonto());
                total = total + (int) unDetalle.getMonto();
                filaProdDetalle.getCell(0).setCellStyle(decimalCellStyle);
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
            rowTotal.getCell(1).setCellStyle(decimalCellStyle);
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
        totalCobrado.createCell(0).setCellValue(new HSSFRichTextString("Total pagado:"));
        filaActual++;
        //INICIO CABECERA PRODUCCION DETALLE
        Row cabeceraCobroDetalle = sheet.createRow(filaActual);
        filaActual++;
        cabeceraCobroDetalle.createCell(0).setCellValue(new HSSFRichTextString("Proveedor"));
        cabeceraCobroDetalle.getCell(0).setCellStyle(titleCellStyle);
        cabeceraCobroDetalle.createCell(1).setCellValue(new HSSFRichTextString("Fecha"));
        cabeceraCobroDetalle.getCell(1).setCellStyle(titleCellStyle);
        cabeceraCobroDetalle.createCell(2).setCellValue(new HSSFRichTextString("Nro Recibo"));
        cabeceraCobroDetalle.getCell(2).setCellStyle(titleCellStyle);
        cabeceraCobroDetalle.createCell(3).setCellValue(new HSSFRichTextString("Monto"));
        cabeceraCobroDetalle.getCell(3).setCellStyle(titleCellStyle);
        //FIN CABECERA PRODUCCION DETALLE

        //INICIO DETALLE PRODUCCION DETALLE
        int total = 0;
        for (E_reciboPagoCabecera cabecera : cabeceraList) {
            Row rowDetalleCobro = sheet.createRow(filaActual);
            filaActual++;
            rowDetalleCobro.createCell(0).setCellValue(cabecera.getProveedor().getEntidad());
            rowDetalleCobro.createCell(1).setCellValue(cabecera.getFechaPago());
            rowDetalleCobro.getCell(1).setCellStyle(dateCellStyle);
            rowDetalleCobro.createCell(2).setCellValue(cabecera.getNroRecibo());
            rowDetalleCobro.createCell(3).setCellValue(cabecera.getMonto());
            rowDetalleCobro.getCell(3).setCellStyle(decimalCellStyle);
            total = total + cabecera.getMonto();
        }
        totalCobrado.createCell(1).setCellValue(total);
        totalCobrado.getCell(1).setCellStyle(decimalCellStyle);
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

    public void exportarChequesEmitidosDetallado(List<E_cuentaCorrienteDetalle> chequesEmitidosList) {
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
        //INICIO RECIBO COBRO CABECERA DETALLE
        Row cabeceraRow = sheet.createRow(filaActual);
        filaActual++;
        cabeceraRow.createCell(0).setCellValue(new HSSFRichTextString("Monto"));
        cabeceraRow.getCell(0).setCellStyle(titleCellStyle);
        cabeceraRow.createCell(1).setCellValue(new HSSFRichTextString("ID compra"));
        cabeceraRow.getCell(1).setCellStyle(titleCellStyle);
        cabeceraRow.createCell(2).setCellValue(new HSSFRichTextString("Nro Factura"));
        cabeceraRow.getCell(2).setCellStyle(titleCellStyle);
        cabeceraRow.createCell(3).setCellValue(new HSSFRichTextString("Nro Cheque"));
        cabeceraRow.getCell(3).setCellStyle(titleCellStyle);
        cabeceraRow.createCell(4).setCellValue(new HSSFRichTextString("Banco"));
        cabeceraRow.getCell(4).setCellStyle(titleCellStyle);
        cabeceraRow.createCell(5).setCellValue(new HSSFRichTextString("Fecha cheque"));
        cabeceraRow.getCell(5).setCellStyle(titleCellStyle);
        cabeceraRow.createCell(6).setCellValue(new HSSFRichTextString("Fecha Cheque diferido"));
        cabeceraRow.getCell(6).setCellStyle(titleCellStyle);
        double total = 0;
        for (E_cuentaCorrienteDetalle unDetalle : chequesEmitidosList) {
            Row chequeDetalleRow = sheet.createRow(filaActual);
            filaActual++;
            chequeDetalleRow.createCell(0).setCellValue(unDetalle.getMonto());
            total = total + unDetalle.getMonto();
            chequeDetalleRow.getCell(0).setCellStyle(decimalCellStyle);
            chequeDetalleRow.createCell(1).setCellValue(unDetalle.getIdFacturaCabecera());
            chequeDetalleRow.createCell(2).setCellValue(unDetalle.getNroFactura());
            chequeDetalleRow.createCell(3).setCellValue(unDetalle.getNroCheque());
            chequeDetalleRow.createCell(4).setCellValue(unDetalle.getBanco().getDescripcion());
            if (unDetalle.getFechaCheque() != null) {
                chequeDetalleRow.createCell(5).setCellValue(unDetalle.getFechaCheque());
                chequeDetalleRow.getCell(5).setCellStyle(dateCellStyle);
            } else {
                chequeDetalleRow.createCell(5).setCellValue("");
            }
            if (unDetalle.getFechaDiferidaCheque() != null) {
                chequeDetalleRow.createCell(6).setCellValue(unDetalle.getFechaDiferidaCheque());
                chequeDetalleRow.getCell(6).setCellStyle(dateCellStyle);
            } else {
                chequeDetalleRow.createCell(6).setCellValue("");
            }
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

    public void exportarChequesEmitidosAgrupado(List<E_cuentaCorrienteDetalle> chequesEmitidosList, Date fechaInicio, Date fechaFinal) {
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
        Row fechaInicioRow = sheet.createRow(filaActual);
        fechaInicioRow.createCell(0).setCellValue(new HSSFRichTextString("Fecha inicio:"));
        fechaInicioRow.createCell(1).setCellValue(fechaInicio);
        fechaInicioRow.getCell(0).setCellStyle(titleCellStyle);
        fechaInicioRow.getCell(1).setCellStyle(dateCellStyle);
        filaActual++;
        Row fechaFinRow = sheet.createRow(filaActual);
        fechaFinRow.createCell(0).setCellValue(new HSSFRichTextString("Fecha fin:"));
        fechaFinRow.createCell(1).setCellValue(fechaFinal);
        fechaFinRow.getCell(0).setCellStyle(titleCellStyle);
        fechaFinRow.getCell(1).setCellStyle(dateCellStyle);
        filaActual++;
        Row totalCobradoRow = sheet.createRow(filaActual);
        totalCobradoRow.createCell(0).setCellValue(new HSSFRichTextString("Total:"));
        totalCobradoRow.getCell(0).setCellStyle(titleCellStyle);
        filaActual++;
        //INICIO RECIBO COBRO CABECERA DETALLE
        Row cabeceraRow = sheet.createRow(filaActual);
        filaActual++;
        cabeceraRow.createCell(0).setCellValue(new HSSFRichTextString("Monto"));
        cabeceraRow.getCell(0).setCellStyle(subTitleCellStyle);
        cabeceraRow.createCell(1).setCellValue(new HSSFRichTextString("Banco"));
        cabeceraRow.getCell(1).setCellStyle(subTitleCellStyle);
        double total = 0;
        for (E_cuentaCorrienteDetalle unDetalle : chequesEmitidosList) {
            Row chequeDetalleRow = sheet.createRow(filaActual);
            filaActual++;
            chequeDetalleRow.createCell(0).setCellValue(unDetalle.getMonto());
            total = total + unDetalle.getMonto();
            chequeDetalleRow.getCell(0).setCellStyle(decimalCellStyle);
            chequeDetalleRow.createCell(1).setCellValue(unDetalle.getBanco().getDescripcion());
        }
        totalCobradoRow.createCell(1).setCellValue(total);
        totalCobradoRow.getCell(1).setCellStyle(decimalCellStyle);
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
