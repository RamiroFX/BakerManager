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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.SwingWorker;
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
    DecimalFormat decimalFormat;
    File directory;

    public ExportarEstadoCuentaCliente(String nombreHoja, M_cliente cliente) {
        this.nombreHoja = nombreHoja;
        this.cliente = cliente;
        inicializarVariables();
        createWorkBook();
        createCellStyles();
    }

    private void inicializarVariables() {
        this.decimalFormat = new DecimalFormat("#,##0.##");
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
        style4.setDataFormat(format.getFormat("#,##0.00"));

        dateCellStyle = workbook.createCellStyle();
        short df = workbook.createDataFormat().getFormat("dd-MM-yyyy");
        dateCellStyle.setDataFormat(df);

        HSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setItalic(false);
        style5 = workbook.createCellStyle();
        style5.setFont(font);

        this.decimalFormat = new DecimalFormat("#,##0.##");
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
        cabeceraProduccionDetalle.createCell(2).setCellValue(new HSSFRichTextString("Debe"));
        cabeceraProduccionDetalle.getCell(2).setCellStyle(style1);
        cabeceraProduccionDetalle.createCell(3).setCellValue(new HSSFRichTextString("Haber"));
        cabeceraProduccionDetalle.getCell(3).setCellStyle(style1);
        cabeceraProduccionDetalle.createCell(4).setCellValue(new HSSFRichTextString("Saldo"));
        cabeceraProduccionDetalle.getCell(4).setCellStyle(style1);
        //FIN CABECERA DETALLE

        //INICIO DETALLE
        double debe = 0;
        double haber = 0;
        double saldo = 0;
        for (int i = 0; i < cabeceraList.size(); i++) {
            Row rowDetalle = sheet.createRow(filaActual);
            filaActual++;
            E_movimientoContable unDetalle = cabeceraList.get(i);
            switch (unDetalle.getTipo()) {
                case E_movimientoContable.TIPO_SALDO_INICIAL: {
                    debe = debe + unDetalle.getClienteSaldoInicial().getSaldoInicial();
                    saldo = debe - haber;
                    rowDetalle.createCell(0).setCellValue(unDetalle.getFechaSaldoInicial());
                    rowDetalle.getCell(0).setCellStyle(dateCellStyle);
                    rowDetalle.createCell(1).setCellValue(unDetalle.getTipoDescripcion());
                    //rowDetalle.createCell(2).setCellValue();
                    rowDetalle.createCell(2).setCellValue(unDetalle.getClienteSaldoInicial().getSaldoInicial());
                    rowDetalle.getCell(2).setCellStyle(style4);
                    rowDetalle.createCell(3).setCellValue(0);
                    rowDetalle.getCell(3).setCellStyle(style4);
                    rowDetalle.createCell(4).setCellValue(saldo);
                    rowDetalle.getCell(4).setCellStyle(style4);
                    break;
                }
                case E_movimientoContable.TIPO_COBRO: {
                    haber = haber + unDetalle.getCobro().getMonto();
                    saldo = debe - haber;
                    int nroFactura = unDetalle.getCobro().getFacturaVenta().getNroFactura();
                    int nroRecibo = unDetalle.getCobro().getCuentaCorrienteCabecera().getNroRecibo();
                    String sNroFactura = decimalFormat.format(nroFactura);
                    String sNroRecibo = decimalFormat.format(nroRecibo);
                    rowDetalle.createCell(0).setCellValue(unDetalle.getCobro().getCuentaCorrienteCabecera().getFechaPago());
                    rowDetalle.getCell(0).setCellStyle(dateCellStyle);
                    rowDetalle.createCell(1).setCellValue(unDetalle.getTipoDescripcion() + " N° " + sNroRecibo + " (Fact. N° " + sNroFactura + ")");
                    rowDetalle.createCell(2).setCellValue(0);
                    rowDetalle.getCell(2).setCellStyle(style4);
                    rowDetalle.createCell(3).setCellValue(unDetalle.getCobro().getMonto());
                    rowDetalle.getCell(3).setCellStyle(style4);
                    rowDetalle.createCell(4).setCellValue(saldo);
                    rowDetalle.getCell(4).setCellStyle(style4);
                    break;
                }
                case E_movimientoContable.TIPO_VENTA: {
                    debe = debe + unDetalle.getVenta().getMonto();
                    saldo = debe - haber;
                    int nroFactura = unDetalle.getVenta().getNroFactura();
                    String sNroFactura = decimalFormat.format(nroFactura);
                    rowDetalle.createCell(0).setCellValue(unDetalle.getVenta().getFecha());
                    rowDetalle.getCell(0).setCellStyle(dateCellStyle);
                    rowDetalle.createCell(1).setCellValue(unDetalle.getTipoDescripcion() + " N° " + sNroFactura);
                    rowDetalle.createCell(2).setCellValue(unDetalle.getVenta().getMonto());
                    rowDetalle.getCell(2).setCellStyle(style4);
                    rowDetalle.createCell(3).setCellValue(0);
                    rowDetalle.getCell(3).setCellStyle(style4);
                    rowDetalle.createCell(4).setCellValue(saldo);
                    rowDetalle.getCell(4).setCellStyle(style4);
                    break;
                }
                case E_movimientoContable.TIPO_NOTA_CREDITO: {
                    haber = haber + unDetalle.getNotaCredito().getTotal();
                    saldo = debe - haber;
                    int nroFactura = unDetalle.getNotaCredito().getFacturaCabecera().getNroFactura();
                    int nroNotaCredito = unDetalle.getNotaCredito().getNroNotaCredito();
                    String sNroFactura = decimalFormat.format(nroFactura);
                    String sNroNotaCredito = decimalFormat.format(nroNotaCredito);
                    rowDetalle.createCell(0).setCellValue(unDetalle.getNotaCredito().getTiempo());
                    rowDetalle.getCell(0).setCellStyle(dateCellStyle);
                    rowDetalle.createCell(1).setCellValue(unDetalle.getTipoDescripcion() + " N° " + sNroNotaCredito + " (Fact. N° " + sNroFactura + ")");
                    rowDetalle.createCell(2).setCellValue(0);
                    rowDetalle.getCell(2).setCellStyle(style4);
                    rowDetalle.createCell(3).setCellValue(unDetalle.getNotaCredito().getTotal());
                    rowDetalle.getCell(3).setCellStyle(style4);
                    rowDetalle.createCell(4).setCellValue(saldo);
                    rowDetalle.getCell(4).setCellStyle(style4);
                    break;
                }
                case E_movimientoContable.TIPO_RETENCION_VENTA: {
                    haber = haber + unDetalle.getRetencionVenta().getMonto();
                    saldo = debe - haber;
                    int nroRetencion = unDetalle.getRetencionVenta().getNroRetencion();
                    String sNroFactura = decimalFormat.format(unDetalle.getRetencionVenta().getVenta().getNroFactura());
                    String sNroRetencion = decimalFormat.format(nroRetencion);
                    rowDetalle.createCell(0).setCellValue(unDetalle.getRetencionVenta().getTiempo());
                    rowDetalle.getCell(0).setCellStyle(dateCellStyle);
                    rowDetalle.createCell(1).setCellValue(unDetalle.getTipoDescripcion() + " N° " + sNroRetencion + " (Fact. N° " + sNroFactura + ")");
                    rowDetalle.createCell(2).setCellValue(0);
                    rowDetalle.getCell(2).setCellStyle(style4);
                    rowDetalle.createCell(3).setCellValue(unDetalle.getRetencionVenta().getMonto());
                    rowDetalle.getCell(3).setCellStyle(style4);
                    rowDetalle.createCell(4).setCellValue(saldo);
                    rowDetalle.getCell(4).setCellStyle(style4);
                    break;
                }
            }
        }
        rowTotal.createCell(1).setCellValue(saldo);
        rowTotal.getCell(1).setCellStyle(style4);
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

    int cur = 0;

    public void exportarEstadoCuenta(Date fechaInicio, Date fechaFin, final List<M_cliente> clientesList,
            boolean inclusivo, JLabel statusLabel, JButton jbAceptar) {
        int totalVal = clientesList.size();
        directory = null;
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

        SwingWorker sw1;
        sw1 = new SwingWorker() {

            @Override
            protected String doInBackground() throws Exception {
                // define what thread will do here

                // Create a row and put some cells in it. Rows are 0 based.
                int filaActual = 0;
                //INICIO ESTADO DE CUENTA - FECHAS
                Row rowFechaInicio = sheet.createRow(filaActual);
                filaActual++;
                rowFechaInicio.createCell(0).setCellValue(new HSSFRichTextString("Fecha inicio"));
                rowFechaInicio.createCell(1).setCellValue(fechaInicio);
                rowFechaInicio.getCell(0).setCellStyle(style5);
                rowFechaInicio.getCell(1).setCellStyle(dateCellStyle);
                Row rowFechaFin = sheet.createRow(filaActual);
                filaActual++;
                rowFechaFin.createCell(0).setCellValue(new HSSFRichTextString("Fecha Fin"));
                rowFechaFin.createCell(1).setCellValue(fechaFin);
                rowFechaFin.getCell(0).setCellStyle(style5);
                rowFechaFin.getCell(1).setCellStyle(dateCellStyle);
                /**
                 * INICIO DE BUCLE DE CLIENTES
                 */
                for (M_cliente unCliente : clientesList) {
                    publish(unCliente);
                    System.out.println("unCliente: " + unCliente);

                    //INICIO DETALLE
                    boolean iniciar = false;
                    boolean saldoAntImprimido = false;
                    boolean cabeceraImprimido = false;
                    int debe = 0;
                    int haber = 0;
                    int balance = 0;
                    cabeceraList = DB_Cliente.obtenerEstadoCuenta(unCliente.getIdCliente());
                    for (int i = 0; i < cabeceraList.size(); i++) {
                        E_movimientoContable unDetalle = cabeceraList.get(i);
                        System.out.println("mov: "+unDetalle.getMovDescripcion()+" - fec: "+unDetalle.getMovFecha());
                        //VERIFICA SI SE EMPIEZA A IMPRIMIR LAS CELDAS
                        if (unDetalle.getMovFecha().after(fechaInicio)) {
                            iniciar = true;
                            /* if(){
                            *
                            }*/
                        }
                        //VERIFICA SI SE TERMINA DE IMPRIMIR LAS CELDAS
                        if (unDetalle.getMovFecha().after(fechaFin)) {
                            break;
                        }
                        Row rowDetalle = null;
                        if (iniciar) {
                            if (!cabeceraImprimido) {
                                filaActual++;
                                Row rowClienteDescripcion = sheet.createRow(filaActual);
                                filaActual++;
                                rowClienteDescripcion.createCell(0).setCellValue(new HSSFRichTextString("Cliente"));
                                rowClienteDescripcion.createCell(1).setCellValue(unCliente.getEntidad());
                                rowClienteDescripcion.getCell(0).setCellStyle(style5);
                                //FIN ESTADO DE CUENTA - CABECERA
                                //INICIO CABECERA DETALLE 
                                Row rowCabecera = sheet.createRow(filaActual);
                                filaActual++;
                                rowCabecera.createCell(0).setCellValue(new HSSFRichTextString("Fecha"));
                                rowCabecera.getCell(0).setCellStyle(style1);
                                rowCabecera.createCell(1).setCellValue(new HSSFRichTextString("Documento"));
                                rowCabecera.getCell(1).setCellStyle(style1);
                                rowCabecera.createCell(2).setCellValue(new HSSFRichTextString("Debe"));
                                rowCabecera.getCell(2).setCellStyle(style1);
                                rowCabecera.createCell(3).setCellValue(new HSSFRichTextString("Haber"));
                                rowCabecera.getCell(3).setCellStyle(style1);
                                rowCabecera.createCell(4).setCellValue(new HSSFRichTextString("Balance"));
                                rowCabecera.getCell(4).setCellStyle(style1);
                                //FIN CABECERA DETALLE    
                                cabeceraImprimido = true;
                            }
                            if (balance != 0 && !saldoAntImprimido) {
                                Row rowSaldoAnt = sheet.createRow(filaActual);
                                filaActual++;
                                rowSaldoAnt.createCell(1).setCellValue("Saldo anterior");
                                rowSaldoAnt.createCell(2).setCellValue(0);
                                rowSaldoAnt.getCell(2).setCellStyle(style4);
                                rowSaldoAnt.createCell(3).setCellValue(0);
                                rowSaldoAnt.getCell(3).setCellStyle(style4);
                                rowSaldoAnt.createCell(4).setCellValue(balance);
                                rowSaldoAnt.getCell(4).setCellStyle(style4);
                            }
                            rowDetalle = sheet.createRow(filaActual);
                            saldoAntImprimido = true;
                            filaActual++;
                        }
                        switch (unDetalle.getTipo()) {
                            case E_movimientoContable.TIPO_SALDO_INICIAL: {
                                debe = debe + (int) unDetalle.getClienteSaldoInicial().getSaldoInicial();
                                balance = debe - haber;
                                if (!iniciar) {
                                    break;
                                }
                                rowDetalle.createCell(0).setCellValue(unDetalle.getFechaSaldoInicial());
                                rowDetalle.getCell(0).setCellStyle(dateCellStyle);
                                rowDetalle.createCell(1).setCellValue(unDetalle.getTipoDescripcion());
                                //rowDetalle.createCell(2).setCellValue();
                                rowDetalle.createCell(2).setCellValue(unDetalle.getClienteSaldoInicial().getSaldoInicial());
                                rowDetalle.getCell(2).setCellStyle(style4);
                                rowDetalle.createCell(3).setCellValue(0);
                                rowDetalle.getCell(3).setCellStyle(style4);
                                rowDetalle.createCell(4).setCellValue(balance);
                                rowDetalle.getCell(4).setCellStyle(style4);
                                break;
                            }
                            case E_movimientoContable.TIPO_COBRO: {
                                haber = haber + (int) unDetalle.getCobro().getMonto();
                                balance = debe - haber;
                                if (!iniciar) {
                                    break;
                                }
                                int nroFactura = unDetalle.getCobro().getFacturaVenta().getNroFactura();
                                int nroRecibo = unDetalle.getCobro().getCuentaCorrienteCabecera().getNroRecibo();
                                String sNroFactura = decimalFormat.format(nroFactura);
                                String sNroRecibo = decimalFormat.format(nroRecibo);
                                rowDetalle.createCell(0).setCellValue(unDetalle.getCobro().getCuentaCorrienteCabecera().getFechaPago());
                                rowDetalle.getCell(0).setCellStyle(dateCellStyle);
                                rowDetalle.createCell(1).setCellValue(unDetalle.getTipoDescripcion() + " N° " + sNroRecibo + " (Fact. N° " + sNroFactura + ")");
                                rowDetalle.createCell(2).setCellValue(0);
                                rowDetalle.getCell(2).setCellStyle(style4);
                                rowDetalle.createCell(3).setCellValue(unDetalle.getCobro().getMonto());
                                rowDetalle.getCell(3).setCellStyle(style4);
                                rowDetalle.createCell(4).setCellValue(balance);
                                rowDetalle.getCell(4).setCellStyle(style4);
                                break;
                            }
                            case E_movimientoContable.TIPO_VENTA: {
                                debe = debe + (int) unDetalle.getVenta().getMonto();
                                balance = debe - haber;
                                if (!iniciar) {
                                    break;
                                }
                                int nroFactura = unDetalle.getVenta().getNroFactura();
                                String sNroFactura = decimalFormat.format(nroFactura);
                                rowDetalle.createCell(0).setCellValue(unDetalle.getVenta().getFecha());
                                rowDetalle.getCell(0).setCellStyle(dateCellStyle);
                                rowDetalle.createCell(1).setCellValue(unDetalle.getTipoDescripcion() + " N° " + sNroFactura);
                                rowDetalle.createCell(2).setCellValue(unDetalle.getVenta().getMonto());
                                rowDetalle.getCell(2).setCellStyle(style4);
                                rowDetalle.createCell(3).setCellValue(0);
                                rowDetalle.getCell(3).setCellStyle(style4);
                                rowDetalle.createCell(4).setCellValue(balance);
                                rowDetalle.getCell(4).setCellStyle(style4);
                                break;
                            }
                            case E_movimientoContable.TIPO_NOTA_CREDITO: {
                                haber = haber + (int) unDetalle.getNotaCredito().getTotal();
                                balance = debe - haber;
                                if (!iniciar) {
                                    break;
                                }
                                int nroFactura = unDetalle.getNotaCredito().getFacturaCabecera().getNroFactura();
                                int nroNotaCredito = unDetalle.getNotaCredito().getNroNotaCredito();
                                String sNroFactura = decimalFormat.format(nroFactura);
                                String sNroNotaCredito = decimalFormat.format(nroNotaCredito);
                                rowDetalle.createCell(0).setCellValue(unDetalle.getNotaCredito().getTiempo());
                                rowDetalle.getCell(0).setCellStyle(dateCellStyle);
                                rowDetalle.createCell(1).setCellValue(unDetalle.getTipoDescripcion() + " N° " + sNroNotaCredito + " (Fact. N° " + sNroFactura + ")");
                                rowDetalle.createCell(2).setCellValue(0);
                                rowDetalle.getCell(2).setCellStyle(style4);
                                rowDetalle.createCell(3).setCellValue(unDetalle.getNotaCredito().getTotal());
                                rowDetalle.getCell(3).setCellStyle(style4);
                                rowDetalle.createCell(4).setCellValue(balance);
                                rowDetalle.getCell(4).setCellStyle(style4);
                                break;
                            }
                            case E_movimientoContable.TIPO_RETENCION_VENTA: {
                                haber = haber + (int) unDetalle.getRetencionVenta().getMonto();
                                balance = debe - haber;
                                if (!iniciar) {
                                    break;
                                }
                                int nroRetencion = unDetalle.getRetencionVenta().getNroRetencion();
                                String sNroFactura = decimalFormat.format(unDetalle.getRetencionVenta().getVenta().getNroFactura());
                                String sNroRetencion = decimalFormat.format(nroRetencion);
                                rowDetalle.createCell(0).setCellValue(unDetalle.getRetencionVenta().getTiempo());
                                rowDetalle.getCell(0).setCellStyle(dateCellStyle);
                                rowDetalle.createCell(1).setCellValue(unDetalle.getTipoDescripcion() + " N° " + sNroRetencion + " (Fact. N° " + sNroFactura + ")");
                                rowDetalle.createCell(2).setCellValue(0);
                                rowDetalle.getCell(2).setCellStyle(style4);
                                rowDetalle.createCell(3).setCellValue(unDetalle.getRetencionVenta().getMonto());
                                rowDetalle.getCell(3).setCellStyle(style4);
                                rowDetalle.createCell(4).setCellValue(balance);
                                rowDetalle.getCell(4).setCellStyle(style4);
                                break;
                            }
                        }
                    }
                    //rowTotal.createCell(1).setCellValue(balance);
                    //rowTotal.getCell(1).setCellStyle(style4);
                }

                //INICIO AJUSTAR COLUMNAS
                sheet.autoSizeColumn(0);
                sheet.autoSizeColumn(1);
                sheet.autoSizeColumn(2);
                sheet.autoSizeColumn(3);
                sheet.autoSizeColumn(4);
                //FIN AJUSTAR COLUMNAS
                try {
                    try (FileOutputStream out = new FileOutputStream(directory.getPath() + ".xls")) {
                        workbook.write(out);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                /*
                for (int i = 10; i >= 0; i--) {
                Thread.sleep(100);
                System.out.println("Value in thread : " + i);
                publish(i);
                }
                 */
                String res = "Reporte finalizado";
                return res;
            }

            @Override
            protected void process(List chunks) {
                cur++;
                // define what the event dispatch thread 
                // will do with the intermediate results received
                // while the thread is executing
                M_cliente val = (M_cliente) chunks.get(chunks.size() - 1);

                statusLabel.setText(String.format("%.2f", calculatePercentage(cur, clientesList.size())) + "% Procesado: " + String.valueOf(val.getEntidad()));
            }

            @Override
            protected void done() {
                // this method is called when the background 
                // thread finishes execution
                try {
                    String statusMsg = (String) get();
                    System.out.println("Reporte finalizado");
                    statusLabel.setText(statusMsg);
                    jbAceptar.setEnabled(true);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };

        // executes the swingworker on worker thread
        sw1.execute();
    }

    public void exportarEstadoCuenta2(Date fechaInicio, Date fechaFin, List<M_cliente> clientesList, boolean inclusivo, JLabel statusLabel) {
        if (clientesList.isEmpty()) {
            clientesList = DB_Cliente.consultarClienteFX("", false, true, false, inclusivo);
        }
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
        //INICIO ESTADO DE CUENTA - FECHAS
        Row rowFechaInicio = sheet.createRow(filaActual);
        filaActual++;
        rowFechaInicio.createCell(0).setCellValue(new HSSFRichTextString("Fecha inicio"));
        rowFechaInicio.createCell(1).setCellValue(fechaInicio);
        rowFechaInicio.getCell(0).setCellStyle(style5);
        rowFechaInicio.getCell(1).setCellStyle(dateCellStyle);
        Row rowFechaFin = sheet.createRow(filaActual);
        filaActual++;
        rowFechaFin.createCell(0).setCellValue(new HSSFRichTextString("Fecha Fin"));
        rowFechaFin.createCell(1).setCellValue(fechaFin);
        rowFechaFin.getCell(0).setCellStyle(style5);
        rowFechaFin.getCell(1).setCellStyle(dateCellStyle);
        /**
         * INICIO DE BUCLE DE CLIENTES
         */
        for (M_cliente unCliente : clientesList) {
            System.out.println("unCliente: " + unCliente);

            //INICIO DETALLE
            boolean iniciar = false;
            boolean saldoAntImprimido = false;
            int debe = 0;
            int haber = 0;
            int balance = 0;
            cabeceraList = DB_Cliente.obtenerEstadoCuenta(unCliente.getIdCliente());
            for (int i = 0; i < cabeceraList.size(); i++) {
                E_movimientoContable unDetalle = cabeceraList.get(i);
                //VERIFICA SI SE EMPIEZA A IMPRIMIR LAS CELDAS
                if (unDetalle.getMovFecha().after(fechaInicio)) {
                    iniciar = true;
                    /* if(){
                        *
                    }*/
                }
                //VERIFICA SI SE TERMINA DE IMPRIMIR LAS CELDAS
                if (unDetalle.getMovFecha().after(fechaFin)) {
                    return;
                }
                Row rowDetalle = null;
                if (iniciar) {
                    if (balance != 0 && !saldoAntImprimido) {
                        filaActual++;
                        Row rowClienteDescripcion = sheet.createRow(filaActual);
                        filaActual++;
                        rowClienteDescripcion.createCell(0).setCellValue(new HSSFRichTextString("Cliente"));
                        rowClienteDescripcion.createCell(1).setCellValue(unCliente.getEntidad());
                        rowClienteDescripcion.getCell(0).setCellStyle(style5);

                        //FIN ESTADO DE CUENTA - CABECERA
                        //INICIO CABECERA DETALLE 
                        Row rowCabecera = sheet.createRow(filaActual);
                        filaActual++;
                        rowCabecera.createCell(0).setCellValue(new HSSFRichTextString("Fecha"));
                        rowCabecera.getCell(0).setCellStyle(style1);
                        rowCabecera.createCell(1).setCellValue(new HSSFRichTextString("Documento"));
                        rowCabecera.getCell(1).setCellStyle(style1);
                        rowCabecera.createCell(2).setCellValue(new HSSFRichTextString("Debe"));
                        rowCabecera.getCell(2).setCellStyle(style1);
                        rowCabecera.createCell(3).setCellValue(new HSSFRichTextString("Haber"));
                        rowCabecera.getCell(3).setCellStyle(style1);
                        rowCabecera.createCell(4).setCellValue(new HSSFRichTextString("Balance"));
                        rowCabecera.getCell(4).setCellStyle(style1);
                        //FIN CABECERA DETALLE
                        Row rowSaldoAnt = sheet.createRow(filaActual);
                        filaActual++;
                        rowSaldoAnt.createCell(1).setCellValue("Saldo anterior");
                        rowSaldoAnt.createCell(2).setCellValue(0);
                        rowSaldoAnt.getCell(2).setCellStyle(style4);
                        rowSaldoAnt.createCell(3).setCellValue(0);
                        rowSaldoAnt.getCell(3).setCellStyle(style4);
                        rowSaldoAnt.createCell(4).setCellValue(balance);
                        rowSaldoAnt.getCell(4).setCellStyle(style4);
                        saldoAntImprimido = true;
                    }
                    rowDetalle = sheet.createRow(filaActual);
                    filaActual++;
                }
                switch (unDetalle.getTipo()) {
                    case E_movimientoContable.TIPO_SALDO_INICIAL: {
                        debe = debe + (int) unDetalle.getClienteSaldoInicial().getSaldoInicial();
                        balance = debe - haber;
                        if (!iniciar) {
                            break;
                        }
                        rowDetalle.createCell(0).setCellValue(unDetalle.getFechaSaldoInicial());
                        rowDetalle.getCell(0).setCellStyle(dateCellStyle);
                        rowDetalle.createCell(1).setCellValue(unDetalle.getTipoDescripcion());
                        //rowDetalle.createCell(2).setCellValue();
                        rowDetalle.createCell(2).setCellValue(unDetalle.getClienteSaldoInicial().getSaldoInicial());
                        rowDetalle.getCell(2).setCellStyle(style4);
                        rowDetalle.createCell(3).setCellValue(0);
                        rowDetalle.getCell(3).setCellStyle(style4);
                        rowDetalle.createCell(4).setCellValue(balance);
                        rowDetalle.getCell(4).setCellStyle(style4);
                        break;
                    }
                    case E_movimientoContable.TIPO_COBRO: {
                        haber = haber + (int) unDetalle.getCobro().getMonto();
                        balance = debe - haber;
                        if (!iniciar) {
                            break;
                        }
                        int nroFactura = unDetalle.getCobro().getFacturaVenta().getNroFactura();
                        int nroRecibo = unDetalle.getCobro().getCuentaCorrienteCabecera().getNroRecibo();
                        String sNroFactura = decimalFormat.format(nroFactura);
                        String sNroRecibo = decimalFormat.format(nroRecibo);
                        rowDetalle.createCell(0).setCellValue(unDetalle.getCobro().getCuentaCorrienteCabecera().getFechaPago());
                        rowDetalle.getCell(0).setCellStyle(dateCellStyle);
                        rowDetalle.createCell(1).setCellValue(unDetalle.getTipoDescripcion() + " N° " + sNroRecibo + " (Fact. N° " + sNroFactura + ")");
                        rowDetalle.createCell(2).setCellValue(0);
                        rowDetalle.getCell(2).setCellStyle(style4);
                        rowDetalle.createCell(3).setCellValue(unDetalle.getCobro().getMonto());
                        rowDetalle.getCell(3).setCellStyle(style4);
                        rowDetalle.createCell(4).setCellValue(balance);
                        rowDetalle.getCell(4).setCellStyle(style4);
                        break;
                    }
                    case E_movimientoContable.TIPO_VENTA: {
                        debe = debe + (int) unDetalle.getVenta().getMonto();
                        balance = debe - haber;
                        if (!iniciar) {
                            break;
                        }
                        int nroFactura = unDetalle.getVenta().getNroFactura();
                        String sNroFactura = decimalFormat.format(nroFactura);
                        rowDetalle.createCell(0).setCellValue(unDetalle.getVenta().getFecha());
                        rowDetalle.getCell(0).setCellStyle(dateCellStyle);
                        rowDetalle.createCell(1).setCellValue(unDetalle.getTipoDescripcion() + " N° " + sNroFactura);
                        rowDetalle.createCell(2).setCellValue(unDetalle.getVenta().getMonto());
                        rowDetalle.getCell(2).setCellStyle(style4);
                        rowDetalle.createCell(3).setCellValue(0);
                        rowDetalle.getCell(3).setCellStyle(style4);
                        rowDetalle.createCell(4).setCellValue(balance);
                        rowDetalle.getCell(4).setCellStyle(style4);
                        break;
                    }
                    case E_movimientoContable.TIPO_NOTA_CREDITO: {
                        haber = haber + (int) unDetalle.getNotaCredito().getTotal();
                        balance = debe - haber;
                        if (!iniciar) {
                            break;
                        }
                        int nroFactura = unDetalle.getNotaCredito().getFacturaCabecera().getNroFactura();
                        int nroNotaCredito = unDetalle.getNotaCredito().getNroNotaCredito();
                        String sNroFactura = decimalFormat.format(nroFactura);
                        String sNroNotaCredito = decimalFormat.format(nroNotaCredito);
                        rowDetalle.createCell(0).setCellValue(unDetalle.getNotaCredito().getTiempo());
                        rowDetalle.getCell(0).setCellStyle(dateCellStyle);
                        rowDetalle.createCell(1).setCellValue(unDetalle.getTipoDescripcion() + " N° " + sNroNotaCredito + " (Fact. N° " + sNroFactura + ")");
                        rowDetalle.createCell(2).setCellValue(0);
                        rowDetalle.getCell(2).setCellStyle(style4);
                        rowDetalle.createCell(3).setCellValue(unDetalle.getNotaCredito().getTotal());
                        rowDetalle.getCell(3).setCellStyle(style4);
                        rowDetalle.createCell(4).setCellValue(balance);
                        rowDetalle.getCell(4).setCellStyle(style4);
                        break;
                    }
                    case E_movimientoContable.TIPO_RETENCION_VENTA: {
                        haber = haber + (int) unDetalle.getRetencionVenta().getMonto();
                        balance = debe - haber;
                        if (!iniciar) {
                            break;
                        }
                        int nroRetencion = unDetalle.getRetencionVenta().getNroRetencion();
                        String sNroFactura = decimalFormat.format(unDetalle.getRetencionVenta().getVenta().getNroFactura());
                        String sNroRetencion = decimalFormat.format(nroRetencion);
                        rowDetalle.createCell(0).setCellValue(unDetalle.getRetencionVenta().getTiempo());
                        rowDetalle.getCell(0).setCellStyle(dateCellStyle);
                        rowDetalle.createCell(1).setCellValue(unDetalle.getTipoDescripcion() + " N° " + sNroRetencion + " (Fact. N° " + sNroFactura + ")");
                        rowDetalle.createCell(2).setCellValue(0);
                        rowDetalle.getCell(2).setCellStyle(style4);
                        rowDetalle.createCell(3).setCellValue(unDetalle.getRetencionVenta().getMonto());
                        rowDetalle.getCell(3).setCellStyle(style4);
                        rowDetalle.createCell(4).setCellValue(balance);
                        rowDetalle.getCell(4).setCellStyle(style4);
                        break;
                    }
                }
            }
            //rowTotal.createCell(1).setCellValue(balance);
            //rowTotal.getCell(1).setCellStyle(style4);
        }

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
                    cobrado = cobrado + (int) unDetalle.getCobro().getMonto();
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

    public double calculatePercentage(double obtained, double total) {
        return obtained * 100 / total;
    }
}
