/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Excel;

import DB.DB_Ingreso;
import Entities.E_impuesto;
import Entities.M_facturaCabecera;
import Entities.M_facturaDetalle;
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
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author Ramiro Ferreira
 */
public class ExportarVentas {

    String nombreHoja;
    ArrayList<M_facturaCabecera> facturaCabeceraFX;
    Date fechaInic, fechaFinal;
    HSSFWorkbook workbook;
    HSSFSheet sheet;
    CellStyle style1, style2, style3, styleNumber;
    HSSFCellStyle dateCellStyle;

    public ExportarVentas(String nombreHoja, ArrayList<M_facturaCabecera> ed) {
        this.nombreHoja = nombreHoja;
        this.facturaCabeceraFX = ed;
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

        styleNumber = workbook.createCellStyle();
        styleNumber.setDataFormat(format.getFormat("#,##0"));

        dateCellStyle = workbook.createCellStyle();
        short df = workbook.createDataFormat().getFormat("dd-MM-yyyy");
        dateCellStyle.setDataFormat(df);
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
                fechaInicio.getCell(col).setCellStyle(dateCellStyle);
            } else {
                fechaFin = sheet.createRow(filaActual);
                col = 0;
                filaActual++;
                fechaInicio.createCell(col).setCellValue(new HSSFRichTextString("Fecha inicio:"));
                fechaFin.createCell(col).setCellValue(new HSSFRichTextString("Fecha fin:"));
                col++;
                fechaInicio.createCell(col).setCellValue(fechaInic);
                fechaFin.createCell(col).setCellValue(fechaFinal);
                fechaFin.getCell(col).setCellStyle(dateCellStyle);
                fechaInicio.getCell(col).setCellStyle(dateCellStyle);
            }
        }
        //FIN CAMPO DE FECHAS

        //INICIO CAMPO DE TOTAL INGRESOS
        Row rowTotalIngreso = sheet.createRow(filaActual);
        filaActual++;
        Integer total = 0;
        Integer totalImpuesto = 0;
        rowTotalIngreso.createCell(0).setCellValue(new HSSFRichTextString("Total ingresos"));
        rowTotalIngreso.getCell(0).setCellStyle(style2);

        Row rowTotalImpuesto = sheet.createRow(filaActual);
        rowTotalImpuesto.createCell(0).setCellValue(new HSSFRichTextString("Total impuesto"));
        rowTotalImpuesto.getCell(0).setCellStyle(style2);
        filaActual++;
        filaActual++;
        //FIN CAMPO DE TOTAL INGRESOS
        //INICIO CUERPO DE DATOS
        for (M_facturaCabecera facturaCabecera : facturaCabeceraFX) {
            //INICIO CABECERA DE DATOS
            Row rowCabeceraFechaNroFactura = sheet.createRow(filaActual);
            filaActual++;
            col = 0;
            rowCabeceraFechaNroFactura.createCell(col).setCellValue(new HSSFRichTextString("Fecha"));
            rowCabeceraFechaNroFactura.getCell(col).setCellStyle(style2);
            col++;
            rowCabeceraFechaNroFactura.createCell(col).setCellValue(facturaCabecera.getTiempo());
            rowCabeceraFechaNroFactura.getCell(col).setCellStyle(dateCellStyle);
            col++;
            rowCabeceraFechaNroFactura.createCell(col).setCellValue(new HSSFRichTextString("Nro. Factura"));
            rowCabeceraFechaNroFactura.getCell(col).setCellStyle(style2);
            col++;
            rowCabeceraFechaNroFactura.createCell(col).setCellValue(facturaCabecera.getNroFactura());
            rowCabeceraFechaNroFactura.getCell(col).setCellStyle(styleNumber);

            Row rowCabeceraClienteCondVenta = sheet.createRow(filaActual);
            filaActual++;
            col = 0;
            rowCabeceraClienteCondVenta.createCell(col).setCellValue(new HSSFRichTextString("Cliente"));
            rowCabeceraClienteCondVenta.getCell(col).setCellStyle(style2);
            col++;
            rowCabeceraClienteCondVenta.createCell(col).setCellValue(facturaCabecera.getCliente().getEntidad());
            col++;
            rowCabeceraClienteCondVenta.createCell(col).setCellValue(new HSSFRichTextString("Cond. venta"));
            rowCabeceraClienteCondVenta.getCell(col).setCellStyle(style2);
            col++;
            rowCabeceraClienteCondVenta.createCell(col).setCellValue(facturaCabecera.getCondVenta().getDescripcion());

            Row rowCabeceraFuncionarioTotalVenta = sheet.createRow(filaActual);
            filaActual++;
            col = 0;
            rowCabeceraFuncionarioTotalVenta.createCell(col).setCellValue(new HSSFRichTextString("Funcionario"));
            rowCabeceraFuncionarioTotalVenta.getCell(col).setCellStyle(style2);
            col++;
            rowCabeceraFuncionarioTotalVenta.createCell(col).setCellValue(facturaCabecera.getFuncionario().getAlias());
            col++;
            rowCabeceraFuncionarioTotalVenta.createCell(col).setCellValue(new HSSFRichTextString("Total venta"));
            rowCabeceraFuncionarioTotalVenta.getCell(col).setCellStyle(style2);
            col++;
            rowCabeceraFuncionarioTotalVenta.createCell(col).setCellValue(facturaCabecera.getTotal());
            rowCabeceraFuncionarioTotalVenta.getCell(col).setCellStyle(styleNumber);
            //FIN CABECERA DE DATOS
            ArrayList<M_facturaDetalle> detalles = DB_Ingreso.obtenerVentaDetalles(facturaCabecera.getIdFacturaCabecera());

            //INICIO CABECERA DETALLE
            Row rowCabeceraDetalle = sheet.createRow(filaActual);
            filaActual++;
            col = 0;
            rowCabeceraDetalle.createCell(col).setCellValue(new HSSFRichTextString("Descripción"));
            rowCabeceraDetalle.getCell(col).setCellStyle(style1);
            col++;
            rowCabeceraDetalle.createCell(col).setCellValue(new HSSFRichTextString("Cantidad"));
            rowCabeceraDetalle.getCell(col).setCellStyle(style1);
            col++;
            rowCabeceraDetalle.createCell(col).setCellValue(new HSSFRichTextString("Descuento"));
            rowCabeceraDetalle.getCell(col).setCellStyle(style1);
            col++;
            rowCabeceraDetalle.createCell(col).setCellValue(new HSSFRichTextString("Precio"));
            rowCabeceraDetalle.getCell(col).setCellStyle(style1);
            col++;
            rowCabeceraDetalle.createCell(col).setCellValue(new HSSFRichTextString("Sub-total"));
            rowCabeceraDetalle.getCell(col).setCellStyle(style1);
            col++;
            //FIN CABECERA DETALLE
            for (M_facturaDetalle facturaDetalle : detalles) {
                Row rowDetalle = sheet.createRow(filaActual);
                filaActual++;
                int colIndex = 0;
                String prodDescripcion = facturaDetalle.getProducto().getDescripcion();
                if (facturaDetalle.getObservacion() != null) {
                    String obs = facturaDetalle.getObservacion();
                    rowDetalle.createCell(colIndex).setCellValue(new HSSFRichTextString(prodDescripcion + "-(" + obs + ")"));
                } else {
                    rowDetalle.createCell(colIndex).setCellValue(new HSSFRichTextString(prodDescripcion));
                }
                colIndex++;
                rowDetalle.createCell(colIndex).setCellValue(facturaDetalle.getCantidad());
                colIndex++;
                rowDetalle.createCell(colIndex).setCellValue(facturaDetalle.getDescuento());
                colIndex++;
                rowDetalle.createCell(colIndex).setCellValue(facturaDetalle.getPrecio());
                colIndex++;
                int subTotal = facturaDetalle.calcularSubTotal();
                rowDetalle.createCell(colIndex).setCellValue(subTotal);
                colIndex++;
                total = total + subTotal;
                int exenta = 0;
                int iva5 = 0;
                int iva10 = 0;
                switch (facturaDetalle.getProducto().getIdImpuesto()) {
                    case E_impuesto.EXENTA: {
                        exenta = exenta + facturaDetalle.calcularSubTotal();
                        break;
                    }
                    case E_impuesto.IVA5: {
                        iva5 = iva5 + (facturaDetalle.calcularSubTotal() / 21);
                        break;
                    }
                    case E_impuesto.IVA10: {
                        iva10 = iva10 + (facturaDetalle.calcularSubTotal() / 11);
                        break;
                    }
                }
                totalImpuesto = totalImpuesto + exenta + iva5 + iva10;
            }
            filaActual++;
        }
        rowTotalIngreso.createCell(1).setCellValue(total);
        rowTotalIngreso.getCell(1).setCellStyle(styleNumber);
        rowTotalImpuesto.createCell(1).setCellValue(totalImpuesto);
        rowTotalImpuesto.getCell(1).setCellStyle(styleNumber);
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
                fechaInicio.createCell(col).setCellValue(fechaInic);
                fechaInicio.getCell(col).setCellStyle(dateCellStyle);
                col++;
            } else {
                fechaFin = sheet.createRow(filaActual);
                filaActual++;
                fechaFin.createCell(col).setCellValue(new HSSFRichTextString("Fecha fin:"));
                fechaFin.createCell(col).setCellValue(fechaFinal);
                fechaFin.getCell(col).setCellStyle(dateCellStyle);
                fechaInicio.createCell(col).setCellValue(new HSSFRichTextString("Fecha inicio:"));
                fechaInicio.createCell(col).setCellValue(fechaInic);
                fechaInicio.getCell(col).setCellStyle(dateCellStyle);
            }
        }
        Row rowTotalIngreso = sheet.createRow(filaActual);
        rowTotalIngreso.createCell(0).setCellValue(new HSSFRichTextString("Total ingresos"));
        rowTotalIngreso.getCell(0).setCellStyle(style2);
        filaActual++;
        Row rowTotalImpuesto = sheet.createRow(filaActual);
        rowTotalImpuesto.createCell(0).setCellValue(new HSSFRichTextString("Total impuesto"));
        rowTotalImpuesto.getCell(0).setCellStyle(style2);
        filaActual++;

        Row rowCabecera = sheet.createRow(filaActual);
        filaActual++;
        rowCabecera.createCell(col).setCellValue(new HSSFRichTextString("Tiempo"));
        rowCabecera.getCell(col).setCellStyle(style1);
        col++;
        rowCabecera.createCell(col).setCellValue(new HSSFRichTextString("Nro. Factura"));
        rowCabecera.getCell(col).setCellStyle(style1);
        col++;
        rowCabecera.createCell(col).setCellValue(new HSSFRichTextString("Cliente"));
        rowCabecera.getCell(col).setCellStyle(style1);
        col++;
        rowCabecera.createCell(col).setCellValue(new HSSFRichTextString("Total"));
        rowCabecera.getCell(col).setCellStyle(style1);
        col++;
        rowCabecera.createCell(col).setCellValue(new HSSFRichTextString("Impuesto"));
        rowCabecera.getCell(col).setCellStyle(style1);
        col++;
        //FIN CUERPO
        Integer total = 0;
        Integer totalImpuesto = 0;
        //TOTAL INGRESOS

        for (M_facturaCabecera facturaCabecera : facturaCabeceraFX) {
            Row row = sheet.createRow(filaActual);
            col = 0;
            row.createCell(col).setCellValue(facturaCabecera.getTiempo());
            row.getCell(col).setCellStyle(dateCellStyle);
            col++;
            row.createCell(col).setCellValue(facturaCabecera.getNroFactura());
            row.getCell(col).setCellStyle(styleNumber);
            col++;
            row.createCell(col).setCellValue(facturaCabecera.getCliente().getEntidad());
            row.getCell(col).setCellStyle(styleNumber);
            col++;
            row.createCell(col).setCellValue(facturaCabecera.getTotal());
            row.getCell(col).setCellStyle(styleNumber);
            col++;
            ArrayList<M_facturaDetalle> detalles = DB_Ingreso.obtenerVentaDetalles(facturaCabecera.getIdFacturaCabecera());
            int subTotalImpuesto = 0;
            for (M_facturaDetalle facturaDetalle : detalles) {
                int exenta = 0;
                int iva5 = 0;
                int iva10 = 0;
                switch (facturaDetalle.getProducto().getIdImpuesto()) {
                    case E_impuesto.EXENTA: {
                        exenta = exenta + facturaDetalle.calcularSubTotal();
                        break;
                    }
                    case E_impuesto.IVA5: {
                        iva5 = iva5 + (facturaDetalle.calcularSubTotal() / 21);
                        break;
                    }
                    case E_impuesto.IVA10: {
                        iva10 = iva10 + (facturaDetalle.calcularSubTotal() / 11);
                        break;
                    }
                }
                subTotalImpuesto = subTotalImpuesto + exenta + iva5 + iva10;
                int subTotal = facturaDetalle.calcularSubTotal();
                total = total + subTotal;
            }
            totalImpuesto = totalImpuesto + subTotalImpuesto;
            row.createCell(col).setCellValue(subTotalImpuesto);
            row.getCell(col).setCellStyle(styleNumber);
            filaActual++;
        }
        rowTotalIngreso.createCell(1).setCellValue(total);
        rowTotalIngreso.getCell(1).setCellStyle(styleNumber);
        rowTotalImpuesto.createCell(1).setCellValue(totalImpuesto);
        rowTotalImpuesto.getCell(1).setCellStyle(styleNumber);
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(4);
        sheet.autoSizeColumn(5);
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
