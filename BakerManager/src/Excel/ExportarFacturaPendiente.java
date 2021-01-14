/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Excel;

import DB.DB_Cobro;
import DB.DB_Ingreso;
import Entities.E_cuentaCorrienteCabecera;
import Entities.E_cuentaCorrienteDetalle;
import Entities.E_facturaSinPago;
import Entities.M_facturaDetalle;
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
public class ExportarFacturaPendiente {

    String nombreHoja;
    Date fechaInic, fechaFinal;
    ArrayList<E_facturaSinPago> cabeceraList;
    HSSFWorkbook workbook;
    HSSFSheet sheet;
    CellStyle style1, style2, style3, style4, style5;
    HSSFCellStyle dateCellStyle;

    public ExportarFacturaPendiente(String nombreHoja, ArrayList<E_facturaSinPago> cabeceraList) {
        this.nombreHoja = nombreHoja;
        this.fechaInic = cabeceraList.get(0).getFecha();
        this.fechaFinal = cabeceraList.get(cabeceraList.size() - 1).getFecha();
        this.cabeceraList = cabeceraList;
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
        for (int q = 0; q < cabeceraList.size(); q++) {
            E_facturaSinPago cabecera = cabeceraList.get(q);

            int primeraFilaInterna = filaActual;
            //INICIO FACTURA PENDIENTE CABECERA
            Row rowHeaderDate = sheet.createRow(filaActual);
            filaActual++;
            rowHeaderDate.createCell(0).setCellValue(new HSSFRichTextString("Fecha"));
            rowHeaderDate.createCell(1).setCellValue(cabecera.getFecha());
            rowHeaderDate.getCell(0).setCellStyle(style5);
            rowHeaderDate.getCell(1).setCellStyle(dateCellStyle);

            Row rowHeaderCustomer = sheet.createRow(filaActual);
            filaActual++;
            rowHeaderCustomer.createCell(0).setCellValue(new HSSFRichTextString("Cliente"));
            rowHeaderCustomer.createCell(1).setCellValue(cabecera.getClienteEntidad());
            rowHeaderCustomer.getCell(0).setCellStyle(style5);

            Row rowHeaderIDventa = sheet.createRow(filaActual);
            filaActual++;
            rowHeaderIDventa.createCell(0).setCellValue(new HSSFRichTextString("ID venta"));
            rowHeaderIDventa.createCell(1).setCellValue(cabecera.getIdCabecera());
            rowHeaderIDventa.getCell(0).setCellStyle(style5);

            Row rowHeaderNroFactura = sheet.createRow(filaActual);
            filaActual++;
            rowHeaderNroFactura.createCell(0).setCellValue(new HSSFRichTextString("Nro factura"));
            rowHeaderNroFactura.createCell(1).setCellValue(cabecera.getNroFactura());
            rowHeaderNroFactura.getCell(0).setCellStyle(style5);

            Row rowHeaderTotal = sheet.createRow(filaActual);
            filaActual++;
            rowHeaderTotal.createCell(0).setCellValue(new HSSFRichTextString("Total"));
            rowHeaderTotal.getCell(0).setCellStyle(style5);
            rowHeaderTotal.createCell(1).setCellValue(cabecera.getMonto());
            rowHeaderTotal.getCell(1).setCellStyle(style4);

            Row rowHeaderPagado = sheet.createRow(filaActual);
            filaActual++;
            rowHeaderPagado.createCell(0).setCellValue(new HSSFRichTextString("Pagado"));
            rowHeaderPagado.getCell(0).setCellStyle(style5);
            rowHeaderPagado.createCell(1).setCellValue(cabecera.getPago());
            rowHeaderPagado.getCell(1).setCellStyle(style4);

            Row rowHeaderSaldo = sheet.createRow(filaActual);
            filaActual++;
            rowHeaderSaldo.createCell(0).setCellValue(new HSSFRichTextString("Saldo"));
            rowHeaderSaldo.getCell(0).setCellStyle(style5);
            rowHeaderSaldo.createCell(1).setCellValue(cabecera.getSaldo());
            rowHeaderSaldo.getCell(1).setCellStyle(style4);
            //FIN FACTURA PENDIENTE CABECERA

            //INICIO FACTURA PENDIENTE DETALLE
            Row rowDetailHeader = sheet.createRow(filaActual);
            filaActual++;
            rowDetailHeader.createCell(0).setCellValue(new HSSFRichTextString("Cod."));
            rowDetailHeader.getCell(0).setCellStyle(style1);
            rowDetailHeader.createCell(1).setCellValue(new HSSFRichTextString("Producto"));
            rowDetailHeader.getCell(1).setCellStyle(style1);
            rowDetailHeader.createCell(2).setCellValue(new HSSFRichTextString("Cantidad"));
            rowDetailHeader.getCell(2).setCellStyle(style1);
            rowDetailHeader.createCell(3).setCellValue(new HSSFRichTextString("Precio"));
            rowDetailHeader.getCell(3).setCellStyle(style1);
            rowDetailHeader.createCell(4).setCellValue(new HSSFRichTextString("Descuento"));
            rowDetailHeader.getCell(4).setCellStyle(style1);
            rowDetailHeader.createCell(5).setCellValue(new HSSFRichTextString("Exenta"));
            rowDetailHeader.getCell(5).setCellStyle(style1);
            rowDetailHeader.createCell(6).setCellValue(new HSSFRichTextString("IVA 5%"));
            rowDetailHeader.getCell(6).setCellStyle(style1);
            rowDetailHeader.createCell(7).setCellValue(new HSSFRichTextString("IVA 10%"));
            rowDetailHeader.getCell(7).setCellStyle(style1);
            rowDetailHeader.createCell(8).setCellValue(new HSSFRichTextString("Obs"));
            rowDetailHeader.getCell(8).setCellStyle(style1);
            //FIN CABECERA FACTURA PENDIENTE DETALLE

            //INICIO FACTURA PENDIENTE DETALLE
            ArrayList<M_facturaDetalle> prodDetalleList;
            prodDetalleList = new ArrayList<>(DB_Ingreso.obtenerVentaDetalles(cabecera.getIdCabecera()));
            for (int i = 0; i < prodDetalleList.size(); i++) {
                Row rowDetail = sheet.createRow(filaActual);
                filaActual++;
                M_facturaDetalle unDetalle = prodDetalleList.get(i);
                rowDetail.createCell(0).setCellValue(unDetalle.getProducto().getCodigo());
                //rowDetail.getCell(0).setCellStyle(style4);
                rowDetail.createCell(1).setCellValue(unDetalle.getProducto().getDescripcion());
                rowDetail.createCell(2).setCellValue(unDetalle.getCantidad());
                rowDetail.createCell(3).setCellValue(unDetalle.getPrecio());
                rowDetail.createCell(4).setCellValue(unDetalle.getDescuento());
                switch (unDetalle.getProducto().getIdImpuesto()) {
                    case 1: {
                        rowDetail.createCell(5).setCellValue(unDetalle.calcularTotal());
                        rowDetail.getCell(5).setCellStyle(style4);
                        rowDetail.createCell(6).setCellValue(0);
                        rowDetail.getCell(6).setCellStyle(style4);
                        rowDetail.createCell(7).setCellValue(0);
                        rowDetail.getCell(7).setCellStyle(style4);
                        break;
                    }
                    case 2: {
                        rowDetail.createCell(5).setCellValue(0);
                        rowDetail.getCell(5).setCellStyle(style4);
                        rowDetail.createCell(6).setCellValue(unDetalle.calcularTotal());
                        rowDetail.getCell(6).setCellStyle(style4);
                        rowDetail.createCell(7).setCellValue(0);
                        rowDetail.getCell(7).setCellStyle(style4);
                        break;
                    }
                    case 3: {
                        rowDetail.createCell(5).setCellValue(0);
                        rowDetail.getCell(5).setCellStyle(style4);
                        rowDetail.createCell(6).setCellValue(0);
                        rowDetail.getCell(6).setCellStyle(style4);
                        rowDetail.createCell(7).setCellValue(unDetalle.calcularTotal());
                        rowDetail.getCell(7).setCellStyle(style4);
                        break;
                    }
                }
                rowDetail.createCell(8).setCellValue(unDetalle.getObservacion());
            }
            CellRangeAddress cuerpoCRA = new CellRangeAddress(primeraFilaInterna, filaActual - 1, 0, 8);
            HSSFRegionUtil.setBorderTop(CellStyle.BORDER_THIN, cuerpoCRA, sheet, workbook);
            HSSFRegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cuerpoCRA, sheet, workbook);
            HSSFRegionUtil.setBorderRight(CellStyle.BORDER_THIN, cuerpoCRA, sheet, workbook);
            HSSFRegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cuerpoCRA, sheet, workbook);
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
        Row rowTotalPendiente = sheet.createRow(filaActual);
        rowTotalPendiente.createCell(0).setCellValue(new HSSFRichTextString("Total pendiente:"));
        filaActual++;
        //INICIO CABECERA 
        Row rowHeader = sheet.createRow(filaActual);
        filaActual++;
        rowHeader.createCell(0).setCellValue(new HSSFRichTextString("ID venta"));
        rowHeader.getCell(0).setCellStyle(style1);
        rowHeader.createCell(1).setCellValue(new HSSFRichTextString("Nro Factura"));
        rowHeader.getCell(1).setCellStyle(style1);
        rowHeader.createCell(2).setCellValue(new HSSFRichTextString("Cliente"));
        rowHeader.getCell(2).setCellStyle(style1);
        rowHeader.createCell(3).setCellValue(new HSSFRichTextString("Fecha"));
        rowHeader.getCell(3).setCellStyle(style1);
        rowHeader.createCell(4).setCellValue(new HSSFRichTextString("Total"));
        rowHeader.getCell(4).setCellStyle(style1);
        rowHeader.createCell(5).setCellValue(new HSSFRichTextString("Pagado"));
        rowHeader.getCell(5).setCellStyle(style1);
        rowHeader.createCell(6).setCellValue(new HSSFRichTextString("Saldo"));
        rowHeader.getCell(6).setCellStyle(style1);
        //FIN CABECERA 

        //INICIO DETALLE PRODUCCION DETALLE
        int total = 0;
        for (E_facturaSinPago cabecera : cabeceraList) {
            Row rowDetalleCobro = sheet.createRow(filaActual);
            filaActual++;
            rowDetalleCobro.createCell(0).setCellValue(cabecera.getIdCabecera());
            rowDetalleCobro.createCell(1).setCellValue(cabecera.getNroFactura());
            rowDetalleCobro.createCell(2).setCellValue(cabecera.getClienteEntidad());
            rowDetalleCobro.createCell(3).setCellValue(cabecera.getFecha());
            rowDetalleCobro.getCell(3).setCellStyle(dateCellStyle);
            rowDetalleCobro.createCell(4).setCellValue(cabecera.getMonto());
            rowDetalleCobro.getCell(4).setCellStyle(style4);
            rowDetalleCobro.createCell(5).setCellValue(cabecera.getPago());
            rowDetalleCobro.getCell(5).setCellStyle(style4);
            rowDetalleCobro.createCell(6).setCellValue(cabecera.getSaldo());
            rowDetalleCobro.getCell(6).setCellStyle(style4);
            total = total + cabecera.getSaldo();
        }
        rowTotalPendiente.createCell(1).setCellValue(total);
        rowTotalPendiente.getCell(1).setCellStyle(style4);
        filaActual++;

        //INICIO AJUSTAR COLUMNAS
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
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

}
