/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Excel;

import DB.DB_Cliente;
import DB.DB_Cobro;
import Entities.E_cuentaCorrienteCabecera;
import Entities.E_cuentaCorrienteDetalle;
import Entities.E_movimientoContable;
import Entities.M_cliente;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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
    //Date fechaInic, fechaFinal;
    M_cliente cliente;
    ArrayList<E_movimientoContable> cabeceraList;
    HSSFWorkbook workbook;
    HSSFSheet sheet;
    CellStyle style1, style2, style3, style4, style5;
    HSSFCellStyle dateCellStyle;

    public ExportarEstadoCuentaCliente(String nombreHoja, M_cliente cliente) {
        this.nombreHoja = nombreHoja;
        this.cliente = cliente;
        inicializarVariables();
        createWorkBook();
        createCellStyles();
    }

    private void inicializarVariables() {
        cabeceraList = DB_Cliente.obtenerEstadoCuenta(this.cliente.getIdCliente());
        /*this.fechaInic = cabeceraList.get(0).getFechaPago();
        this.fechaFinal = cabeceraList.get(cabeceraList.size() - 1).getFechaPago();*/

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

    public void exportacionHistorica() {
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
        //INICIO ESTADO DE CUENTA - CABECERA
        Row produccionCabeceraRowFecha = sheet.createRow(filaActual);
        filaActual++;
        produccionCabeceraRowFecha.createCell(0).setCellValue(new HSSFRichTextString("Fecha"));
        produccionCabeceraRowFecha.createCell(1).setCellValue(Calendar.getInstance().getTime());
        produccionCabeceraRowFecha.getCell(0).setCellStyle(style5);
        produccionCabeceraRowFecha.getCell(1).setCellStyle(dateCellStyle);

        Row produccionCabeceraRowResp = sheet.createRow(filaActual);
        filaActual++;
        produccionCabeceraRowResp.createCell(0).setCellValue(new HSSFRichTextString("Cliente"));
        produccionCabeceraRowResp.createCell(1).setCellValue(this.cliente.getEntidad());
        produccionCabeceraRowResp.getCell(0).setCellStyle(style5);

        Row rowTotal = sheet.createRow(filaActual);
        filaActual++;
        rowTotal.createCell(0).setCellValue(new HSSFRichTextString("Total pendiente"));
        rowTotal.getCell(0).setCellStyle(style5);
        //FIN ESTADO DE CUENTA - CABECERA

        //INICIO CABECERA DETALLE 
        Row cabeceraProduccionDetalle = sheet.createRow(filaActual);
        filaActual++;
        cabeceraProduccionDetalle.createCell(0).setCellValue(new HSSFRichTextString("Fecha"));
        cabeceraProduccionDetalle.getCell(0).setCellStyle(style1);
        cabeceraProduccionDetalle.createCell(1).setCellValue(new HSSFRichTextString("Documento"));
        cabeceraProduccionDetalle.getCell(1).setCellStyle(style1);
        cabeceraProduccionDetalle.createCell(2).setCellValue(new HSSFRichTextString("Nro"));
        cabeceraProduccionDetalle.getCell(2).setCellStyle(style1);
        cabeceraProduccionDetalle.createCell(3).setCellValue(new HSSFRichTextString("Monto"));
        cabeceraProduccionDetalle.getCell(3).setCellStyle(style1);
        cabeceraProduccionDetalle.createCell(4).setCellValue(new HSSFRichTextString("Débito"));
        cabeceraProduccionDetalle.getCell(4).setCellStyle(style1);
        cabeceraProduccionDetalle.createCell(5).setCellValue(new HSSFRichTextString("Credito"));
        cabeceraProduccionDetalle.getCell(5).setCellStyle(style1);
        //FIN CABECERA DETALLE

        //INICIO DETALLE
        int deuda = 0;
        int credito = 0;
        int total = 0;
        for (int i = 0; i < cabeceraList.size(); i++) {
            Row rowDetalle = sheet.createRow(filaActual);
            filaActual++;
            E_movimientoContable unDetalle = cabeceraList.get(i);
            switch (unDetalle.getTipo()) {
                case E_movimientoContable.TIPO_SALDO_INICIAL: {
                    deuda = deuda + (int) unDetalle.getClienteSaldoInicial().getSaldoInicial();
                    total = credito - deuda;
                    //rowDetalle.createCell(0).setCellValue();
                    rowDetalle.createCell(1).setCellValue(unDetalle.getTipoDescripcion());
                    //rowDetalle.createCell(2).setCellValue();
                    rowDetalle.createCell(3).setCellValue(unDetalle.getClienteSaldoInicial().getSaldoInicial());
                    rowDetalle.createCell(4).setCellValue(deuda);
                    rowDetalle.createCell(5).setCellValue(credito);
                    break;
                }
                case E_movimientoContable.TIPO_COBRO: {
                    credito = credito + (int) unDetalle.getCobro().getDebito();
                    total = credito - deuda;
                    rowDetalle.createCell(0).setCellValue(unDetalle.getCobro().getFechaPago());
                    rowDetalle.getCell(0).setCellStyle(style4);
                    rowDetalle.createCell(1).setCellValue(unDetalle.getTipoDescripcion());
                    rowDetalle.createCell(2).setCellValue(unDetalle.getCobro().getNroRecibo());
                    rowDetalle.createCell(3).setCellValue(unDetalle.getCobro().getDebito());
                    rowDetalle.createCell(4).setCellValue(deuda);
                    rowDetalle.createCell(5).setCellValue(credito);
                    break;
                }
                case E_movimientoContable.TIPO_COMPRA: {
                    deuda = deuda + (int) unDetalle.getCompra().getTotal();
                    total = credito - deuda;
                    rowDetalle.createCell(0).setCellValue(unDetalle.getCompra().getTiempo());
                    rowDetalle.getCell(0).setCellStyle(style4);
                    rowDetalle.createCell(1).setCellValue(unDetalle.getTipoDescripcion());
                    rowDetalle.createCell(2).setCellValue(unDetalle.getCompra().getNro_factura());
                    rowDetalle.createCell(3).setCellValue(unDetalle.getCompra().getTotal());
                    rowDetalle.createCell(4).setCellValue(deuda);
                    rowDetalle.createCell(5).setCellValue(credito);
                    break;
                }
                case E_movimientoContable.TIPO_PAGO: {
                    credito = credito + (int) unDetalle.getPago().getMonto();
                    total = credito - deuda;
                    rowDetalle.createCell(0).setCellValue(unDetalle.getPago().getFechaPago());
                    rowDetalle.getCell(0).setCellStyle(style4);
                    rowDetalle.createCell(1).setCellValue(unDetalle.getTipoDescripcion());
                    rowDetalle.createCell(2).setCellValue(unDetalle.getPago().getNroRecibo());
                    rowDetalle.createCell(3).setCellValue(unDetalle.getPago().getMonto());
                    rowDetalle.createCell(4).setCellValue(deuda);
                    rowDetalle.createCell(5).setCellValue(credito);
                    break;
                }
                case E_movimientoContable.TIPO_VENTA: {
                    deuda = deuda + (int) unDetalle.getVenta().getMonto();
                    total = credito - deuda;
                    rowDetalle.createCell(0).setCellValue(unDetalle.getVenta().getFecha());
                    rowDetalle.getCell(0).setCellStyle(style4);
                    rowDetalle.createCell(1).setCellValue(unDetalle.getTipoDescripcion());
                    rowDetalle.createCell(2).setCellValue(unDetalle.getVenta().getNroFactura());
                    rowDetalle.createCell(3).setCellValue(unDetalle.getVenta().getMonto());
                    rowDetalle.createCell(4).setCellValue(deuda);
                    rowDetalle.createCell(5).setCellValue(credito);
                    break;
                }
            }
        }
        rowTotal.createCell(1).setCellValue(total);
        rowTotal.getCell(1).setCellStyle(style4);
        filaActual++;

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
//        Row fechaInicio = sheet.createRow(filaActual);
//        fechaInicio.createCell(0).setCellValue(new HSSFRichTextString("Fecha inicio:"));
//        fechaInicio.createCell(1).setCellValue(fechaInic);
//        fechaInicio.getCell(1).setCellStyle(dateCellStyle);
//        filaActual++;
//        Row fechaFin = sheet.createRow(filaActual);
//        fechaFin.createCell(0).setCellValue(new HSSFRichTextString("Fecha fin:"));
//        fechaFin.createCell(1).setCellValue(fechaFinal);
//        fechaFin.getCell(1).setCellStyle(dateCellStyle);
//        filaActual++;
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
        /*for (E_cuentaCorrienteCabecera cabecera : cabeceraList) {
            Row rowDetalleCobro = sheet.createRow(filaActual);
            filaActual++;
            rowDetalleCobro.createCell(0).setCellValue(cabecera.getCliente().getEntidad());
            rowDetalleCobro.createCell(1).setCellValue(cabecera.getFechaPago());
            rowDetalleCobro.getCell(1).setCellStyle(dateCellStyle);
            rowDetalleCobro.createCell(2).setCellValue(cabecera.getNroRecibo());
            rowDetalleCobro.createCell(3).setCellValue(cabecera.getDebito());
            rowDetalleCobro.getCell(3).setCellStyle(style4);
            total = total + cabecera.getDebito();
        }*/
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
