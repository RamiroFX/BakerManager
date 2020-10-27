/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Excel;

import DB.DB_NotaCredito;
import Entities.E_NotaCreditoCabecera;
import Entities.E_NotaCreditoDetalle;
import Entities.E_impuesto;
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
public class ExportarNotaCredito {

    String nombreHoja;
    ArrayList<E_NotaCreditoCabecera> cabecera;
    Date fechaInic, fechaFinal;
    HSSFWorkbook workbook;
    HSSFSheet sheet;
    CellStyle styleSubTitle, styleSubTitle2, styleNumberFloatPoint, styleNumberInteger;
    HSSFCellStyle styleDate;

    public ExportarNotaCredito(String nombreHoja, ArrayList<E_NotaCreditoCabecera> ed) {
        this.nombreHoja = nombreHoja;
        this.cabecera = ed;
        this.fechaInic = ed.get(0).getTiempo();
        this.fechaFinal = ed.get(ed.size() - 1).getTiempo();
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
                fechaInicio.getCell(col).setCellStyle(styleDate);
            } else {
                fechaFin = sheet.createRow(filaActual);
                col = 0;
                filaActual++;
                fechaInicio.createCell(col).setCellValue(new HSSFRichTextString("Fecha inicio:"));
                fechaFin.createCell(col).setCellValue(new HSSFRichTextString("Fecha fin:"));
                col++;
                fechaInicio.createCell(col).setCellValue(fechaInic);
                fechaFin.createCell(col).setCellValue(fechaFinal);
                fechaFin.getCell(col).setCellStyle(styleDate);
                fechaInicio.getCell(col).setCellStyle(styleDate);
            }
        }
        //FIN CAMPO DE FECHAS

        //INICIO CAMPO DE TOTAL INGRESOS
        Row rowTotalIngreso = sheet.createRow(filaActual);
        filaActual++;
        double total = 0;
        double totalImpuesto = 0;
        rowTotalIngreso.createCell(0).setCellValue(new HSSFRichTextString("Total"));
        rowTotalIngreso.getCell(0).setCellStyle(styleSubTitle2);

        Row rowTotalImpuesto = sheet.createRow(filaActual);
        rowTotalImpuesto.createCell(0).setCellValue(new HSSFRichTextString("Total impuesto"));
        rowTotalImpuesto.getCell(0).setCellStyle(styleSubTitle2);
        filaActual++;
        filaActual++;
        //FIN CAMPO DE TOTAL INGRESOS
        //INICIO CUERPO DE DATOS
        for (E_NotaCreditoCabecera notaCreditoCabecera : cabecera) {
            int notaCreditoPrimeraFila = filaActual;
            //INICIO CABECERA DE DATOS
            Row rowCabeceraFechaNroFactura = sheet.createRow(filaActual);
            filaActual++;
            col = 0;
            rowCabeceraFechaNroFactura.createCell(col).setCellValue(new HSSFRichTextString("Fecha"));
            rowCabeceraFechaNroFactura.getCell(col).setCellStyle(styleSubTitle2);
            col++;
            rowCabeceraFechaNroFactura.createCell(col).setCellValue(notaCreditoCabecera.getTiempo());
            rowCabeceraFechaNroFactura.getCell(col).setCellStyle(styleDate);
            col++;
            rowCabeceraFechaNroFactura.createCell(col).setCellValue(new HSSFRichTextString("Nro. Nota de crédito"));
            rowCabeceraFechaNroFactura.getCell(col).setCellStyle(styleSubTitle2);
            col++;
            rowCabeceraFechaNroFactura.createCell(col).setCellValue(notaCreditoCabecera.getNroNotaCredito());
            rowCabeceraFechaNroFactura.getCell(col).setCellStyle(styleNumberInteger);

            Row rowCabeceraClienteCondVenta = sheet.createRow(filaActual);
            filaActual++;
            col = 0;
            rowCabeceraClienteCondVenta.createCell(col).setCellValue(new HSSFRichTextString("Cliente"));
            rowCabeceraClienteCondVenta.getCell(col).setCellStyle(styleSubTitle2);
            col++;
            rowCabeceraClienteCondVenta.createCell(col).setCellValue(notaCreditoCabecera.getCliente().getEntidad());
            col++;
            rowCabeceraClienteCondVenta.createCell(col).setCellValue(new HSSFRichTextString("Cond. venta"));
            rowCabeceraClienteCondVenta.getCell(col).setCellStyle(styleSubTitle2);
            col++;
            rowCabeceraClienteCondVenta.createCell(col).setCellValue(notaCreditoCabecera.getFacturaCabecera().getTipoOperacion().getDescripcion());

            Row rowCabeceraFuncionarioTotalVenta = sheet.createRow(filaActual);
            filaActual++;
            col = 0;
            rowCabeceraFuncionarioTotalVenta.createCell(col).setCellValue(new HSSFRichTextString("Funcionario"));
            rowCabeceraFuncionarioTotalVenta.getCell(col).setCellStyle(styleSubTitle2);
            col++;
            rowCabeceraFuncionarioTotalVenta.createCell(col).setCellValue(notaCreditoCabecera.getFuncionario().getAlias());
            col++;
            rowCabeceraFuncionarioTotalVenta.createCell(col).setCellValue(new HSSFRichTextString("Total venta"));
            rowCabeceraFuncionarioTotalVenta.getCell(col).setCellStyle(styleSubTitle2);
            col++;
            rowCabeceraFuncionarioTotalVenta.createCell(col).setCellValue(notaCreditoCabecera.getTotal());
            rowCabeceraFuncionarioTotalVenta.getCell(col).setCellStyle(styleNumberInteger);
            //FIN CABECERA DE DATOS
            ArrayList<E_NotaCreditoDetalle> detalles = new ArrayList<>(DB_NotaCredito.obtenerNotasCreditoDetalle(notaCreditoCabecera.getId()));

            //INICIO CABECERA DETALLE
            Row rowCabeceraDetalle = sheet.createRow(filaActual);
            filaActual++;
            col = 0;
            rowCabeceraDetalle.createCell(col).setCellValue(new HSSFRichTextString("Cantidad"));
            rowCabeceraDetalle.getCell(col).setCellStyle(styleSubTitle);
            col++;
            rowCabeceraDetalle.createCell(col).setCellValue(new HSSFRichTextString("Descripción"));
            rowCabeceraDetalle.getCell(col).setCellStyle(styleSubTitle);
            col++;
            rowCabeceraDetalle.createCell(col).setCellValue(new HSSFRichTextString("Descuento"));
            rowCabeceraDetalle.getCell(col).setCellStyle(styleSubTitle);
            col++;
            rowCabeceraDetalle.createCell(col).setCellValue(new HSSFRichTextString("Precio"));
            rowCabeceraDetalle.getCell(col).setCellStyle(styleSubTitle);
            col++;
            rowCabeceraDetalle.createCell(col).setCellValue(new HSSFRichTextString("Sub-total"));
            rowCabeceraDetalle.getCell(col).setCellStyle(styleSubTitle);
            col++;
            //FIN CABECERA DETALLE
            for (E_NotaCreditoDetalle facturaDetalle : detalles) {
                Row rowDetalle = sheet.createRow(filaActual);
                filaActual++;
                int colIndex = 0;
                rowDetalle.createCell(colIndex).setCellValue(facturaDetalle.getCantidad());
                colIndex++;
                String prodDescripcion = facturaDetalle.getProducto().getDescripcion();
                if (facturaDetalle.getObservacion() != null) {
                    String obs = facturaDetalle.getObservacion();
                    rowDetalle.createCell(colIndex).setCellValue(new HSSFRichTextString(prodDescripcion + "-(" + obs + ")"));
                } else {
                    rowDetalle.createCell(colIndex).setCellValue(new HSSFRichTextString(prodDescripcion));
                }
                colIndex++;
                rowDetalle.createCell(colIndex).setCellValue(facturaDetalle.getDescuento());
                colIndex++;
                rowDetalle.createCell(colIndex).setCellValue(facturaDetalle.getPrecio());
                colIndex++;
                double subTotal = facturaDetalle.getSubTotal();
                rowDetalle.createCell(colIndex).setCellValue(subTotal);
                colIndex++;
                total = total + subTotal;
                double exenta = 0;
                double iva5 = 0;
                double iva10 = 0;
                switch (facturaDetalle.getProducto().getIdImpuesto()) {
                    case E_impuesto.EXENTA: {
                        exenta = exenta + facturaDetalle.getSubTotal();
                        break;
                    }
                    case E_impuesto.IVA5: {
                        iva5 = iva5 + (facturaDetalle.getSubTotal() / 21);
                        break;
                    }
                    case E_impuesto.IVA10: {
                        iva10 = iva10 + (facturaDetalle.getSubTotal() / 11);
                        break;
                    }
                }
                totalImpuesto = totalImpuesto + exenta + iva5 + iva10;
            }
            CellRangeAddress cuerpoCRA = new CellRangeAddress(notaCreditoPrimeraFila, filaActual - 1, 0, 4);
            HSSFRegionUtil.setBorderTop(CellStyle.BORDER_THIN, cuerpoCRA, sheet, workbook);
            HSSFRegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cuerpoCRA, sheet, workbook);
            HSSFRegionUtil.setBorderRight(CellStyle.BORDER_THIN, cuerpoCRA, sheet, workbook);
            HSSFRegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cuerpoCRA, sheet, workbook);
            filaActual++;
        }
        rowTotalIngreso.createCell(1).setCellValue(total);
        rowTotalIngreso.getCell(1).setCellStyle(styleNumberInteger);
        rowTotalImpuesto.createCell(1).setCellValue(totalImpuesto);
        rowTotalImpuesto.getCell(1).setCellStyle(styleNumberInteger);
        //FIN CUERPO DE DATOS

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
        int col = 0;
        Row fechaInicio = sheet.createRow(filaActual);
        filaActual++;
        Row fechaFin = null;
        if (fechaInic != null && fechaFinal != null) {
            int dateType = fechaInic.compareTo(fechaFinal);
            if (dateType == 0) {
                fechaInicio.createCell(col).setCellValue(new HSSFRichTextString("Fecha :"));
                fechaInicio.getCell(col).setCellStyle(styleSubTitle2);
                col++;
                fechaInicio.createCell(col).setCellValue(fechaInic);
                fechaInicio.getCell(col).setCellStyle(styleDate);
                col++;
            } else {
                fechaFin = sheet.createRow(filaActual);
                filaActual++;
                fechaFin.createCell(col).setCellValue(new HSSFRichTextString("Fecha fin:"));
                fechaFin.getCell(col).setCellStyle(styleSubTitle2);
                fechaInicio.createCell(col).setCellValue(new HSSFRichTextString("Fecha inicio:"));
                fechaInicio.getCell(col).setCellStyle(styleSubTitle2);
                col++;
                fechaFin.createCell(col).setCellValue(fechaFinal);
                fechaFin.getCell(col).setCellStyle(styleDate);
                fechaInicio.createCell(col).setCellValue(fechaInic);
                fechaInicio.getCell(col).setCellStyle(styleDate);
            }
        }
        col = 0;
        Row rowTotalIngreso = sheet.createRow(filaActual);
        rowTotalIngreso.createCell(0).setCellValue(new HSSFRichTextString("Total"));
        rowTotalIngreso.getCell(0).setCellStyle(styleSubTitle2);
        filaActual++;
        Row rowTotalImpuesto = sheet.createRow(filaActual);
        rowTotalImpuesto.createCell(0).setCellValue(new HSSFRichTextString("Total impuesto"));
        rowTotalImpuesto.getCell(0).setCellStyle(styleSubTitle2);
        filaActual++;
        Row rowTotalImpuestoIVA5 = sheet.createRow(filaActual);
        rowTotalImpuestoIVA5.createCell(0).setCellValue(new HSSFRichTextString("Impuesto 5%"));
        rowTotalImpuestoIVA5.getCell(0).setCellStyle(styleSubTitle2);
        filaActual++;
        Row rowTotalImpuestoIVA10 = sheet.createRow(filaActual);
        rowTotalImpuestoIVA10.createCell(0).setCellValue(new HSSFRichTextString("Impuesto 10%"));
        rowTotalImpuestoIVA10.getCell(0).setCellStyle(styleSubTitle2);
        filaActual++;

        Row rowCabecera = sheet.createRow(filaActual);
        filaActual++;
        rowCabecera.createCell(col).setCellValue(new HSSFRichTextString("Tiempo"));
        rowCabecera.getCell(col).setCellStyle(styleSubTitle);
        col++;
        rowCabecera.createCell(col).setCellValue(new HSSFRichTextString("Nro. Nota de crédito"));
        rowCabecera.getCell(col).setCellStyle(styleSubTitle);
        col++;
        rowCabecera.createCell(col).setCellValue(new HSSFRichTextString("Cliente"));
        rowCabecera.getCell(col).setCellStyle(styleSubTitle);
        col++;
        rowCabecera.createCell(col).setCellValue(new HSSFRichTextString("Total"));
        rowCabecera.getCell(col).setCellStyle(styleSubTitle);
        col++;
        rowCabecera.createCell(col).setCellValue(new HSSFRichTextString("Impuesto"));
        rowCabecera.getCell(col).setCellStyle(styleSubTitle);
        col++;
        rowCabecera.createCell(col).setCellValue(new HSSFRichTextString("Impuesto IVA 5%"));
        rowCabecera.getCell(col).setCellStyle(styleSubTitle);
        col++;
        rowCabecera.createCell(col).setCellValue(new HSSFRichTextString("Impuesto IVA 10%"));
        rowCabecera.getCell(col).setCellStyle(styleSubTitle);
        col++;
        //FIN CUERPO
        double total = 0;
        double totalImpuesto = 0;
        double totalImpuestoIVA5 = 0;
        double totalImpuestoIVA10 = 0;
        //TOTAL INGRESOS

        for (E_NotaCreditoCabecera notaCreditoCabecera : cabecera) {
            double impuestoIVA5 = 0;
            double impuestoIVA10 = 0;
            Row row = sheet.createRow(filaActual);
            col = 0;
            row.createCell(col).setCellValue(notaCreditoCabecera.getTiempo());
            row.getCell(col).setCellStyle(styleDate);
            col++;
            row.createCell(col).setCellValue(notaCreditoCabecera.getNroNotaCredito());
            row.getCell(col).setCellStyle(styleNumberInteger);
            col++;
            row.createCell(col).setCellValue(notaCreditoCabecera.getCliente().getEntidad());
            row.getCell(col).setCellStyle(styleNumberInteger);
            col++;
            row.createCell(col).setCellValue(notaCreditoCabecera.getTotal());
            row.getCell(col).setCellStyle(styleNumberInteger);
            col++;
            ArrayList<E_NotaCreditoDetalle> detalles = new ArrayList<>(DB_NotaCredito.obtenerNotasCreditoDetalle(notaCreditoCabecera.getId()));
            double subTotalImpuesto = 0;
            for (E_NotaCreditoDetalle facturaDetalle : detalles) {
                double exenta = 0;
                double iva5 = 0;
                double iva10 = 0;
                switch (facturaDetalle.getProducto().getIdImpuesto()) {
                    case E_impuesto.EXENTA: {
                        //exenta = exenta + facturaDetalle.calcularSubTotal();
                        break;
                    }
                    case E_impuesto.IVA5: {
                        iva5 = iva5 + (facturaDetalle.getSubTotal() / 21);
                        break;
                    }
                    case E_impuesto.IVA10: {
                        iva10 = iva10 + (facturaDetalle.getSubTotal() / 11);
                        break;
                    }
                }
                impuestoIVA5 = impuestoIVA5 + iva5;
                impuestoIVA10 = impuestoIVA10 + iva10;
                subTotalImpuesto = subTotalImpuesto + exenta + iva5 + iva10;
                double subTotal = facturaDetalle.getSubTotal();
                total = total + subTotal;
            }
            totalImpuesto = totalImpuesto + subTotalImpuesto;
            totalImpuestoIVA10 = totalImpuestoIVA10 + impuestoIVA10;
            totalImpuestoIVA5 = totalImpuestoIVA5 + impuestoIVA5;
            row.createCell(col).setCellValue(subTotalImpuesto);
            row.getCell(col).setCellStyle(styleNumberInteger);
            col++;
            row.createCell(col).setCellValue(impuestoIVA5);
            row.getCell(col).setCellStyle(styleNumberInteger);
            col++;
            row.createCell(col).setCellValue(impuestoIVA10);
            row.getCell(col).setCellStyle(styleNumberInteger);
            col++;
            filaActual++;
        }
        rowTotalIngreso.createCell(1).setCellValue(total);
        rowTotalIngreso.getCell(1).setCellStyle(styleNumberInteger);
        rowTotalImpuesto.createCell(1).setCellValue(totalImpuesto);
        rowTotalImpuesto.getCell(1).setCellStyle(styleNumberInteger);
        rowTotalImpuestoIVA5.createCell(1).setCellValue(totalImpuestoIVA5);
        rowTotalImpuestoIVA5.getCell(1).setCellStyle(styleNumberInteger);
        rowTotalImpuestoIVA10.createCell(1).setCellValue(totalImpuestoIVA10);
        rowTotalImpuestoIVA10.getCell(1).setCellStyle(styleNumberInteger);
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(4);
        sheet.autoSizeColumn(5);
        sheet.autoSizeColumn(6);
        sheet.autoSizeColumn(7);
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
