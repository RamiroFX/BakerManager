/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Excel;

import DB.DB_Cliente;
import Entities.E_movimientoContable;
import Entities.M_cliente;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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
        cabeceraProduccionDetalle.createCell(6).setCellValue(new HSSFRichTextString("Balance"));
        cabeceraProduccionDetalle.getCell(6).setCellStyle(style1);
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
                    total = deuda - credito;
                    rowDetalle.createCell(0).setCellValue(unDetalle.getFechaSaldoInicial());
                    rowDetalle.getCell(0).setCellStyle(dateCellStyle);
                    rowDetalle.createCell(1).setCellValue(unDetalle.getTipoDescripcion());
                    //rowDetalle.createCell(2).setCellValue();
                    rowDetalle.createCell(3).setCellValue(unDetalle.getClienteSaldoInicial().getSaldoInicial());
                    rowDetalle.getCell(3).setCellStyle(style4);
                    rowDetalle.createCell(4).setCellValue(deuda);
                    rowDetalle.getCell(4).setCellStyle(style4);
                    rowDetalle.createCell(5).setCellValue(0);
                    rowDetalle.getCell(5).setCellStyle(style4);
                    rowDetalle.createCell(6).setCellValue(total);
                    rowDetalle.getCell(6).setCellStyle(style4);
                    break;
                }
                case E_movimientoContable.TIPO_COBRO: {
                    credito = credito + (int) unDetalle.getCobro().getDebito();
                    total = deuda - credito;
                    rowDetalle.createCell(0).setCellValue(unDetalle.getCobro().getFechaPago());
                    rowDetalle.getCell(0).setCellStyle(dateCellStyle);
                    rowDetalle.createCell(1).setCellValue(unDetalle.getTipoDescripcion());
                    rowDetalle.createCell(2).setCellValue(unDetalle.getCobro().getNroRecibo());
                    rowDetalle.getCell(2).setCellStyle(style4);
                    rowDetalle.createCell(3).setCellValue(unDetalle.getCobro().getDebito());
                    rowDetalle.getCell(3).setCellStyle(style4);
                    rowDetalle.createCell(4).setCellValue(0);
                    rowDetalle.getCell(4).setCellStyle(style4);
                    rowDetalle.createCell(5).setCellValue(credito);
                    rowDetalle.getCell(5).setCellStyle(style4);
                    rowDetalle.createCell(6).setCellValue(total);
                    rowDetalle.getCell(6).setCellStyle(style4);
                    break;
                }
                case E_movimientoContable.TIPO_COMPRA: {
                    deuda = deuda + (int) unDetalle.getCompra().getTotal();
                    total = deuda - credito;
                    rowDetalle.createCell(0).setCellValue(unDetalle.getCompra().getTiempo());
                    rowDetalle.getCell(0).setCellStyle(dateCellStyle);
                    rowDetalle.createCell(1).setCellValue(unDetalle.getTipoDescripcion());
                    rowDetalle.createCell(2).setCellValue(unDetalle.getCompra().getNro_factura());
                    rowDetalle.getCell(2).setCellStyle(style4);
                    rowDetalle.createCell(3).setCellValue(unDetalle.getCompra().getTotal());
                    rowDetalle.getCell(3).setCellStyle(style4);
                    rowDetalle.createCell(4).setCellValue(deuda);
                    rowDetalle.getCell(4).setCellStyle(style4);
                    rowDetalle.createCell(5).setCellValue(0);
                    rowDetalle.getCell(5).setCellStyle(style4);
                    rowDetalle.createCell(6).setCellValue(total);
                    rowDetalle.getCell(6).setCellStyle(style4);
                    break;
                }
                case E_movimientoContable.TIPO_PAGO: {
                    credito = credito + (int) unDetalle.getPago().getMonto();
                    total = deuda - credito;
                    rowDetalle.createCell(0).setCellValue(unDetalle.getPago().getFechaPago());
                    rowDetalle.getCell(0).setCellStyle(dateCellStyle);
                    rowDetalle.createCell(1).setCellValue(unDetalle.getTipoDescripcion());
                    rowDetalle.createCell(2).setCellValue(unDetalle.getPago().getNroRecibo());
                    rowDetalle.getCell(2).setCellStyle(style4);
                    rowDetalle.createCell(3).setCellValue(unDetalle.getPago().getMonto());
                    rowDetalle.getCell(3).setCellStyle(style4);
                    rowDetalle.createCell(4).setCellValue(0);
                    rowDetalle.getCell(4).setCellStyle(style4);
                    rowDetalle.createCell(5).setCellValue(credito);
                    rowDetalle.getCell(5).setCellStyle(style4);
                    rowDetalle.createCell(6).setCellValue(total);
                    rowDetalle.getCell(6).setCellStyle(style4);
                    break;
                }
                case E_movimientoContable.TIPO_VENTA: {
                    deuda = deuda + (int) unDetalle.getVenta().getMonto();
                    total = deuda - credito;
                    rowDetalle.createCell(0).setCellValue(unDetalle.getVenta().getFecha());
                    rowDetalle.getCell(0).setCellStyle(dateCellStyle);
                    rowDetalle.createCell(1).setCellValue(unDetalle.getTipoDescripcion());
                    rowDetalle.createCell(2).setCellValue(unDetalle.getVenta().getNroFactura());
                    rowDetalle.getCell(2).setCellStyle(style4);
                    rowDetalle.createCell(3).setCellValue(unDetalle.getVenta().getMonto());
                    rowDetalle.getCell(3).setCellStyle(style4);
                    rowDetalle.createCell(4).setCellValue(deuda);
                    rowDetalle.getCell(4).setCellStyle(style4);
                    rowDetalle.createCell(5).setCellValue(0);
                    rowDetalle.getCell(5).setCellStyle(style4);
                    rowDetalle.createCell(6).setCellValue(total);
                    rowDetalle.getCell(6).setCellStyle(style4);
                    break;
                }
                case E_movimientoContable.TIPO_NOTA_CREDITO: {
                    credito = credito + (int) unDetalle.getNotaCredito().getTotal();
                    total = deuda - credito;
                    rowDetalle.createCell(0).setCellValue(unDetalle.getNotaCredito().getTiempo());
                    rowDetalle.getCell(0).setCellStyle(dateCellStyle);
                    rowDetalle.createCell(1).setCellValue(unDetalle.getTipoDescripcion());
                    rowDetalle.createCell(2).setCellValue(unDetalle.getNotaCredito().getNroNotaCredito());
                    rowDetalle.getCell(2).setCellStyle(style4);
                    rowDetalle.createCell(3).setCellValue(unDetalle.getNotaCredito().getTotal());
                    rowDetalle.getCell(3).setCellStyle(style4);
                    rowDetalle.createCell(4).setCellValue(0);
                    rowDetalle.getCell(4).setCellStyle(style4);
                    rowDetalle.createCell(5).setCellValue(credito);
                    rowDetalle.getCell(5).setCellStyle(style4);
                    rowDetalle.createCell(6).setCellValue(total);
                    rowDetalle.getCell(6).setCellStyle(style4);
                    break;
                }
                case E_movimientoContable.TIPO_RETENCION_VENTA: {
                    credito = credito + (int) unDetalle.getRetencionVenta().getMonto();
                    total = deuda - credito;
                    rowDetalle.createCell(0).setCellValue(unDetalle.getRetencionVenta().getTiempo());
                    rowDetalle.getCell(0).setCellStyle(dateCellStyle);
                    rowDetalle.createCell(1).setCellValue(unDetalle.getTipoDescripcion());
                    rowDetalle.createCell(2).setCellValue(unDetalle.getRetencionVenta().getNroRetencion());
                    rowDetalle.getCell(2).setCellStyle(style4);
                    rowDetalle.createCell(3).setCellValue(unDetalle.getRetencionVenta().getMonto());
                    rowDetalle.getCell(3).setCellStyle(style4);
                    rowDetalle.createCell(4).setCellValue(0);
                    rowDetalle.getCell(4).setCellStyle(style4);
                    rowDetalle.createCell(5).setCellValue(credito);
                    rowDetalle.getCell(5).setCellStyle(style4);
                    rowDetalle.createCell(6).setCellValue(total);
                    rowDetalle.getCell(6).setCellStyle(style4);
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
        //FECHA
        Row rowFecha = sheet.createRow(filaActual);
        rowFecha.createCell(0).setCellValue(new HSSFRichTextString("Fecha"));
        rowFecha.getCell(0).setCellStyle(style1);
        rowFecha.createCell(1).setCellValue(Calendar.getInstance().getTime());
        rowFecha.getCell(1).setCellStyle(dateCellStyle);
        filaActual++;
        //CLIENTE
        Row rowCliente = sheet.createRow(filaActual);
        rowCliente.createCell(0).setCellValue(new HSSFRichTextString("Cliente"));
        rowCliente.getCell(0).setCellStyle(style1);
        rowCliente.createCell(1).setCellValue(cliente.getEntidad());
        filaActual++;
        //TOTAL COBRADO
        Row rowTotalCobrado = sheet.createRow(filaActual);
        rowTotalCobrado.createCell(0).setCellValue(new HSSFRichTextString("Total cobrado:"));
        rowTotalCobrado.getCell(0).setCellStyle(style1);
        filaActual++;
        //TOTAL FACTURADO
        Row rowTotalFacturado = sheet.createRow(filaActual);
        rowTotalFacturado.createCell(0).setCellValue(new HSSFRichTextString("Total facturado:"));
        rowTotalFacturado.getCell(0).setCellStyle(style1);
        filaActual++;
        //BALANCE
        Row rowBalance = sheet.createRow(filaActual);
        rowBalance.createCell(0).setCellValue(new HSSFRichTextString("Balance:"));
        rowBalance.getCell(0).setCellStyle(style1);
        filaActual++;
        //INICIO DETALLE PRODUCCION DETALLE
        int cobrado = 0;
        int facturado = 0;
        int balance = 0;
        for (E_movimientoContable unDetalle : cabeceraList) {
            switch (unDetalle.getTipo()) {
                case E_movimientoContable.TIPO_SALDO_INICIAL: {
                    facturado = facturado + (int) unDetalle.getClienteSaldoInicial().getSaldoInicial();
                    balance = cobrado - facturado;
                    break;
                }
                case E_movimientoContable.TIPO_COBRO: {
                    cobrado = cobrado + (int) unDetalle.getCobro().getDebito();
                    balance = cobrado - facturado;
                    break;
                }
                case E_movimientoContable.TIPO_VENTA: {
                    facturado = facturado + (int) unDetalle.getVenta().getMonto();
                    balance = cobrado - facturado;
                    break;
                }
                case E_movimientoContable.TIPO_NOTA_CREDITO: {
                    cobrado = cobrado + (int) unDetalle.getNotaCredito().getTotal();
                    balance = cobrado - facturado;
                    break;
                }
                case E_movimientoContable.TIPO_RETENCION_VENTA: {
                    cobrado = cobrado + (int) unDetalle.getRetencionVenta().getMonto();
                    balance = cobrado - facturado;
                    break;
                }
            }
        }
        rowTotalCobrado.createCell(1).setCellValue(cobrado);
        rowTotalCobrado.getCell(1).setCellStyle(style4);
        rowTotalFacturado.createCell(1).setCellValue(facturado);
        rowTotalFacturado.getCell(1).setCellStyle(style4);
        rowBalance.createCell(1).setCellValue(balance);
        rowBalance.getCell(1).setCellStyle(style4);
        filaActual++;

        //INICIO AJUSTAR COLUMNAS
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
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
