/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Excel;

import DB.DB_Egreso;
import DB.DB_Ingreso;
import Entities.E_impuesto;
import Entities.M_egresoCabecera;
import Entities.M_egreso_detalle;
import Entities.M_egreso_detalleFX;
import Entities.M_facturaCabecera;
import Entities.M_facturaDetalle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
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
 * @author Ramiro
 */
public class C_create_excel {

    public static int MULTIPLES_FECHAS = 1;
    public static int UNA_FECHA = 2;
    String nombreHoja;
    ArrayList<M_egreso_detalleFX> egresoDetalle;
    ArrayList<M_egresoCabecera> egresoCabecera;
    List<M_egreso_detalle> egresoDetalle2;
    Date fechaInic, fechaFinal;
    HSSFWorkbook workbook;
    HSSFSheet sheet;
    CellStyle style1, style2, style3, style4, styleNumber;
    HSSFCellStyle dateCellStyle;
    int tipo_fecha;

    public C_create_excel(String nombreHoja, ArrayList<M_egreso_detalleFX> egresoDetalle, Date fechaInic, Date fechaFinal) {
        this.nombreHoja = nombreHoja;
        this.egresoDetalle = egresoDetalle;
        this.fechaInic = fechaInic;
        this.fechaFinal = fechaFinal;
        if (fechaInic != null && fechaFinal != null) {
            int dateType = fechaInic.compareTo(fechaFinal);
            if (dateType == 0) {
                tipo_fecha = UNA_FECHA;
            } else {
                tipo_fecha = MULTIPLES_FECHAS;
            }
        } else {
            tipo_fecha = MULTIPLES_FECHAS;
        }
        createWorkBook();
        createCellStyles();
    }

    public C_create_excel(String nombreHoja, Date fechaInic, Date fechaFinal, ArrayList<M_egresoCabecera> egresoCabecera) {
        this.nombreHoja = nombreHoja;
        this.fechaInic = fechaInic;
        this.fechaFinal = fechaFinal;
        this.egresoCabecera = egresoCabecera;
        if (fechaInic != null && fechaFinal != null) {
            int dateType = fechaInic.compareTo(fechaFinal);
            if (dateType == 0) {
                tipo_fecha = UNA_FECHA;
            } else {
                tipo_fecha = MULTIPLES_FECHAS;
            }
        } else {
            tipo_fecha = MULTIPLES_FECHAS;
        }
        createWorkBook();
        createCellStyles();
    }


    public C_create_excel(String nombreHoja, Date fechaInic, Date fechaFinal, List<M_egreso_detalle> egresoDetalle2) {
        this.nombreHoja = nombreHoja;
        this.fechaInic = fechaInic;
        this.fechaFinal = fechaFinal;
        this.egresoDetalle2 = egresoDetalle2;
        if (fechaInic != null && fechaFinal != null) {
            int dateType = fechaInic.compareTo(fechaFinal);
            if (dateType == 0) {
                tipo_fecha = UNA_FECHA;
            } else {
                tipo_fecha = MULTIPLES_FECHAS;
            }
        } else {
            tipo_fecha = MULTIPLES_FECHAS;
        }
        createWorkBook();
        createCellStyles();
    }

    public void establecerListaCabecera() {

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
        style4.setDataFormat(format.getFormat("#,##0.0000"));

        dateCellStyle = workbook.createCellStyle();
        short df = workbook.createDataFormat().getFormat("dd-MM-yyyy");
        dateCellStyle.setDataFormat(df);

        styleNumber = workbook.createCellStyle();
        styleNumber.setDataFormat(format.getFormat("#,##0"));
        //END FORMAT STYLE
    }

    public void initComp() {
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
        if (tipo_fecha == MULTIPLES_FECHAS) {
            fechaFin = sheet.createRow(filaActual);
            filaActual++;
            fechaFin.createCell(0).setCellValue(new HSSFRichTextString("Fecha fin:"));
            if (fechaFinal != null) {
                fechaFin.createCell(1).setCellValue(fechaFinal);
                fechaFin.getCell(1).setCellStyle(dateCellStyle);
            }
            fechaInicio.createCell(0).setCellValue(new HSSFRichTextString("Fecha inicio:"));
            fechaInicio.createCell(1).setCellValue(fechaInic);
            fechaInicio.getCell(1).setCellStyle(dateCellStyle);
        } else {
            fechaInicio.createCell(0).setCellValue(new HSSFRichTextString("Fecha :"));
            fechaInicio.createCell(1).setCellValue(fechaInic);
            fechaInicio.getCell(1).setCellStyle(dateCellStyle);
        }
        Row totalEgreso2 = sheet.createRow(filaActual);
        filaActual++;
        Row cabecera = sheet.createRow(filaActual);
        filaActual++;
        if (tipo_fecha == MULTIPLES_FECHAS) {
        } else {
        }
        totalEgreso2.createCell(0).setCellValue(new HSSFRichTextString("Total egresos"));
        totalEgreso2.getCell(0).setCellStyle(style2);
        //------------
        int col = 0;
        cabecera.createCell(col).setCellValue(new HSSFRichTextString("Proveedor"));
        cabecera.getCell(col).setCellStyle(style1);
        col++;
        if (tipo_fecha == MULTIPLES_FECHAS) {
            cabecera.createCell(col).setCellValue(new HSSFRichTextString("Fecha"));
            cabecera.getCell(col).setCellStyle(style1);
            col++;
        }
        cabecera.createCell(col).setCellValue(new HSSFRichTextString("Descripción"));
        cabecera.getCell(col).setCellStyle(style1);
        col++;
        cabecera.createCell(col).setCellValue(new HSSFRichTextString("Cantidad"));
        cabecera.getCell(col).setCellStyle(style1);
        col++;
        cabecera.createCell(col).setCellValue(new HSSFRichTextString("Descuento"));
        cabecera.getCell(col).setCellStyle(style1);
        col++;
        cabecera.createCell(col).setCellValue(new HSSFRichTextString("Precio"));
        cabecera.getCell(col).setCellStyle(style1);
        col++;
        cabecera.createCell(col).setCellValue(new HSSFRichTextString("Sub-total"));
        cabecera.getCell(col).setCellStyle(style1);
        col++;
        cabecera.createCell(col).setCellValue(new HSSFRichTextString("Total"));
        cabecera.getCell(col).setCellStyle(style1);

        Integer total = 0;
        Integer SubTotal = 0;
        int idEgresoCabecera = egresoDetalle.get(0).getId_cabecera();
        for (int i = 0; i < egresoDetalle.size(); i++) {
            SubTotal = SubTotal + (egresoDetalle.get(i).getTotal());
        }
        total = total + SubTotal;
        //TOTAL EGRESOS
        totalEgreso2.createCell(1).setCellValue(total);
        SubTotal = 0;
        if (tipo_fecha == MULTIPLES_FECHAS) {
            escribirCeldasConFecha(idEgresoCabecera);
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
            sheet.autoSizeColumn(5);
            sheet.autoSizeColumn(6);
        } else {
            escribirCeldasSinFecha(idEgresoCabecera);
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
            sheet.autoSizeColumn(5);
        }
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

    private void escribirCeldasConFecha(int egresocabecera) {
        int filaActual = 4;
        int idEgCab = egresocabecera;
        boolean b = true;
        Integer SubTotal = 0;
        for (int i = 0; i < egresoDetalle.size(); i++) {
            if (idEgCab == egresoDetalle.get(i).getId_cabecera()) {
                if (b) {
                    Row asd = sheet.createRow(filaActual);
                    asd.createCell(0).setCellValue(new HSSFRichTextString(egresoDetalle.get(i).getProveedor()));
                    asd.getCell(0).setCellStyle(style2);
                    asd.createCell(1).setCellValue(egresoDetalle.get(i).getTiempo());
                    asd.getCell(1).setCellStyle(dateCellStyle);
                    if (egresoDetalle.get(i).getObservacion() != null) {
                        asd.createCell(2).setCellValue(new HSSFRichTextString(egresoDetalle.get(i).getProducto() + "-(" + egresoDetalle.get(i).getObservacion() + ")"));
                    } else {
                        asd.createCell(2).setCellValue(new HSSFRichTextString(egresoDetalle.get(i).getProducto()));
                    }
                    asd.createCell(3).setCellValue(egresoDetalle.get(i).getCantidad());
                    asd.createCell(4).setCellValue(egresoDetalle.get(i).getDescuento());
                    asd.createCell(5).setCellValue(egresoDetalle.get(i).getPrecio());
                    asd.createCell(6).setCellValue(egresoDetalle.get(i).getTotal());
                    for (int X = i; X < egresoDetalle.size(); X++) {
                        if (idEgCab == egresoDetalle.get(X).getId_cabecera()) {
                            SubTotal = SubTotal + egresoDetalle.get(X).getTotal();
                        } else {
                            X = egresoDetalle.size();
                        }
                    }
                    asd.createCell(7).setCellValue(SubTotal);
                    filaActual++;
                }
                if (!b) {
                    Row qwerty = sheet.createRow(filaActual);
                    filaActual++;
                    qwerty.createCell(2).setCellValue(new HSSFRichTextString(egresoDetalle.get(i).getProducto()));
                    qwerty.createCell(3).setCellValue(egresoDetalle.get(i).getCantidad());
                    qwerty.createCell(4).setCellValue(egresoDetalle.get(i).getDescuento());
                    qwerty.createCell(5).setCellValue(egresoDetalle.get(i).getPrecio());
                    qwerty.createCell(6).setCellValue(egresoDetalle.get(i).getTotal());
                }
                b = false;
            } else {
                SubTotal = 0;
                b = true;
                idEgCab = egresoDetalle.get(i).getId_cabecera();
                i--;
                filaActual++;
            }
        }
    }

    private void escribirCeldasSinFecha(int egresocabecera) {
        int filaActual = 3;
        int idEgCab = egresocabecera;
        boolean b = true;
        Integer SubTotal = 0;
        for (int i = 0; i < egresoDetalle.size(); i++) {
            if (idEgCab == egresoDetalle.get(i).getId_cabecera()) {
                if (b) {
                    Row asd = sheet.createRow(filaActual);
                    asd.createCell(0).setCellValue(new HSSFRichTextString(egresoDetalle.get(i).getProveedor()));
                    asd.getCell(0).setCellStyle(style2);
                    if (egresoDetalle.get(i).getObservacion() != null) {
                        asd.createCell(1).setCellValue(new HSSFRichTextString(egresoDetalle.get(i).getProducto() + "-(" + egresoDetalle.get(i).getObservacion() + ")"));
                    } else {
                        asd.createCell(1).setCellValue(new HSSFRichTextString(egresoDetalle.get(i).getProducto()));
                    }
                    asd.createCell(2).setCellValue(egresoDetalle.get(i).getCantidad());
                    asd.createCell(3).setCellValue(egresoDetalle.get(i).getDescuento());
                    asd.createCell(4).setCellValue(egresoDetalle.get(i).getPrecio());
                    asd.createCell(5).setCellValue(egresoDetalle.get(i).getTotal());
                    for (int X = i; X < egresoDetalle.size(); X++) {
                        if (idEgCab == egresoDetalle.get(X).getId_cabecera()) {
                            SubTotal = SubTotal + egresoDetalle.get(X).getTotal();
                        } else {
                            X = egresoDetalle.size();
                        }
                    }
                    asd.createCell(6).setCellValue(SubTotal);
                    filaActual++;
                }
                if (!b) {
                    Row qwerty = sheet.createRow(filaActual);
                    filaActual++;
                    qwerty.createCell(1).setCellValue(new HSSFRichTextString(egresoDetalle.get(i).getProducto()));
                    qwerty.createCell(2).setCellValue(egresoDetalle.get(i).getCantidad());
                    qwerty.createCell(3).setCellValue(egresoDetalle.get(i).getDescuento());
                    qwerty.createCell(4).setCellValue(egresoDetalle.get(i).getPrecio());
                    qwerty.createCell(5).setCellValue(egresoDetalle.get(i).getTotal());
                }
                b = false;
            } else {
                SubTotal = 0;
                b = true;
                idEgCab = egresoDetalle.get(i).getId_cabecera();
                i--;
                filaActual++;
            }
        }
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
        double total = 0;
        double totalImpuesto = 0;
        rowTotalIngreso.createCell(0).setCellValue(new HSSFRichTextString("Total compras"));
        rowTotalIngreso.getCell(0).setCellStyle(style2);

        Row rowTotalImpuesto = sheet.createRow(filaActual);
        rowTotalImpuesto.createCell(0).setCellValue(new HSSFRichTextString("Total impuesto"));
        rowTotalImpuesto.getCell(0).setCellStyle(style2);
        filaActual++;
        filaActual++;
        //FIN CAMPO DE TOTAL INGRESOS
        //INICIO CUERPO DE DATOS
        for (M_egresoCabecera egresoCabecera : egresoCabecera) {
            int ventaPrimeraFila = filaActual;
            //INICIO CABECERA DE DATOS
            Row rowCabeceraFechaNroFactura = sheet.createRow(filaActual);
            filaActual++;
            col = 0;
            rowCabeceraFechaNroFactura.createCell(col).setCellValue(new HSSFRichTextString("Fecha"));
            rowCabeceraFechaNroFactura.getCell(col).setCellStyle(style2);
            col++;
            rowCabeceraFechaNroFactura.createCell(col).setCellValue(egresoCabecera.getTiempo());
            rowCabeceraFechaNroFactura.getCell(col).setCellStyle(dateCellStyle);
            col++;
            rowCabeceraFechaNroFactura.createCell(col).setCellValue(new HSSFRichTextString("Nro. Factura"));
            rowCabeceraFechaNroFactura.getCell(col).setCellStyle(style2);
            col++;
            rowCabeceraFechaNroFactura.createCell(col).setCellValue(egresoCabecera.getNro_factura());
            rowCabeceraFechaNroFactura.getCell(col).setCellStyle(styleNumber);

            Row rowCabeceraClienteCondVenta = sheet.createRow(filaActual);
            filaActual++;
            col = 0;
            rowCabeceraClienteCondVenta.createCell(col).setCellValue(new HSSFRichTextString("Cliente"));
            rowCabeceraClienteCondVenta.getCell(col).setCellStyle(style2);
            col++;
            rowCabeceraClienteCondVenta.createCell(col).setCellValue(egresoCabecera.getProveedor().getEntidad());
            col++;
            rowCabeceraClienteCondVenta.createCell(col).setCellValue(new HSSFRichTextString("Cond. compra"));
            rowCabeceraClienteCondVenta.getCell(col).setCellStyle(style2);
            col++;
            rowCabeceraClienteCondVenta.createCell(col).setCellValue(egresoCabecera.getCondCompra().getDescripcion());

            Row rowCabeceraFuncionarioTotalVenta = sheet.createRow(filaActual);
            filaActual++;
            col = 0;
            rowCabeceraFuncionarioTotalVenta.createCell(col).setCellValue(new HSSFRichTextString("Funcionario"));
            rowCabeceraFuncionarioTotalVenta.getCell(col).setCellStyle(style2);
            col++;
            rowCabeceraFuncionarioTotalVenta.createCell(col).setCellValue(egresoCabecera.getFuncionario().getAlias());
            col++;
            rowCabeceraFuncionarioTotalVenta.createCell(col).setCellValue(new HSSFRichTextString("Total compra"));
            rowCabeceraFuncionarioTotalVenta.getCell(col).setCellStyle(style2);
            col++;
            rowCabeceraFuncionarioTotalVenta.createCell(col).setCellValue(egresoCabecera.getTotal());
            rowCabeceraFuncionarioTotalVenta.getCell(col).setCellStyle(styleNumber);
            //FIN CABECERA DE DATOS
            ArrayList<M_egreso_detalle> detalles = DB_Egreso.obtenerEgresoDetalles(egresoCabecera.getId_cabecera());

            //INICIO CABECERA DETALLE
            Row rowCabeceraDetalle = sheet.createRow(filaActual);
            filaActual++;
            col = 0;
            rowCabeceraDetalle.createCell(col).setCellValue(new HSSFRichTextString("Cantidad"));
            rowCabeceraDetalle.getCell(col).setCellStyle(style1);
            col++;
            rowCabeceraDetalle.createCell(col).setCellValue(new HSSFRichTextString("Descripción"));
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
            for (M_egreso_detalle facturaDetalle : detalles) {
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
                double subTotal = facturaDetalle.calcularSubTotal();
                rowDetalle.createCell(colIndex).setCellValue(subTotal);
                colIndex++;
                total = total + subTotal;
                double exenta = 0;
                double iva5 = 0;
                double iva10 = 0;
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
            CellRangeAddress cuerpoCRA = new CellRangeAddress(ventaPrimeraFila, filaActual - 1, 0, 4);
            HSSFRegionUtil.setBorderTop(CellStyle.BORDER_THIN, cuerpoCRA, sheet, workbook);
            HSSFRegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cuerpoCRA, sheet, workbook);
            HSSFRegionUtil.setBorderRight(CellStyle.BORDER_THIN, cuerpoCRA, sheet, workbook);
            HSSFRegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cuerpoCRA, sheet, workbook);
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
                fechaInicio.getCell(col).setCellStyle(style2);
                col++;
                fechaInicio.createCell(col).setCellValue(fechaInic);
                fechaInicio.getCell(col).setCellStyle(dateCellStyle);
                col++;
            } else {
                fechaFin = sheet.createRow(filaActual);
                filaActual++;
                fechaFin.createCell(col).setCellValue(new HSSFRichTextString("Fecha fin:"));
                fechaFin.getCell(col).setCellStyle(style2);
                fechaInicio.createCell(col).setCellValue(new HSSFRichTextString("Fecha inicio:"));
                fechaInicio.getCell(col).setCellStyle(style2);
                col++;
                fechaFin.createCell(col).setCellValue(fechaFinal);
                fechaFin.getCell(col).setCellStyle(dateCellStyle);
                fechaInicio.createCell(col).setCellValue(fechaInic);
                fechaInicio.getCell(col).setCellStyle(dateCellStyle);
            }
        }
        col = 0;
        Row rowTotalIngreso = sheet.createRow(filaActual);
        rowTotalIngreso.createCell(0).setCellValue(new HSSFRichTextString("Total compras"));
        rowTotalIngreso.getCell(0).setCellStyle(style2);
        filaActual++;
        Row rowTotalImpuesto = sheet.createRow(filaActual);
        rowTotalImpuesto.createCell(0).setCellValue(new HSSFRichTextString("Total impuesto"));
        rowTotalImpuesto.getCell(0).setCellStyle(style2);
        filaActual++;
        Row rowTotalImpuestoIVA5 = sheet.createRow(filaActual);
        rowTotalImpuestoIVA5.createCell(0).setCellValue(new HSSFRichTextString("Impuesto 5%"));
        rowTotalImpuestoIVA5.getCell(0).setCellStyle(style2);
        filaActual++;
        Row rowTotalImpuestoIVA10 = sheet.createRow(filaActual);
        rowTotalImpuestoIVA10.createCell(0).setCellValue(new HSSFRichTextString("Impuesto 10%"));
        rowTotalImpuestoIVA10.getCell(0).setCellStyle(style2);
        filaActual++;

        Row rowCabecera = sheet.createRow(filaActual);
        filaActual++;
        rowCabecera.createCell(col).setCellValue(new HSSFRichTextString("Tiempo"));
        rowCabecera.getCell(col).setCellStyle(style1);
        col++;
        rowCabecera.createCell(col).setCellValue(new HSSFRichTextString("Nro. Factura"));
        rowCabecera.getCell(col).setCellStyle(style1);
        col++;
        rowCabecera.createCell(col).setCellValue(new HSSFRichTextString("Proveedor"));
        rowCabecera.getCell(col).setCellStyle(style1);
        col++;
        rowCabecera.createCell(col).setCellValue(new HSSFRichTextString("Total"));
        rowCabecera.getCell(col).setCellStyle(style1);
        col++;
        rowCabecera.createCell(col).setCellValue(new HSSFRichTextString("Impuesto"));
        rowCabecera.getCell(col).setCellStyle(style1);
        col++;
        rowCabecera.createCell(col).setCellValue(new HSSFRichTextString("Impuesto IVA 5%"));
        rowCabecera.getCell(col).setCellStyle(style1);
        col++;
        rowCabecera.createCell(col).setCellValue(new HSSFRichTextString("Impuesto IVA 10%"));
        rowCabecera.getCell(col).setCellStyle(style1);
        col++;
        //FIN CUERPO
        double total = 0;
        double totalImpuesto = 0;
        double totalImpuestoIVA5 = 0;
        double totalImpuestoIVA10 = 0;
        //TOTAL EGRESOS
        for (M_egresoCabecera facturaCabecera : egresoCabecera) {
            double impuestoIVA5 = 0;
            double impuestoIVA10 = 0;
            Row row = sheet.createRow(filaActual);
            col = 0;
            row.createCell(col).setCellValue(facturaCabecera.getTiempo());
            row.getCell(col).setCellStyle(dateCellStyle);
            col++;
            row.createCell(col).setCellValue(facturaCabecera.getNro_factura());
            row.getCell(col).setCellStyle(styleNumber);
            col++;
            row.createCell(col).setCellValue(facturaCabecera.getProveedor().getEntidad());
            row.getCell(col).setCellStyle(styleNumber);
            col++;
            row.createCell(col).setCellValue(facturaCabecera.getTotal());
            row.getCell(col).setCellStyle(styleNumber);
            col++;
            ArrayList<M_egreso_detalle> detalles = DB_Egreso.obtenerEgresoDetalles(facturaCabecera.getId_cabecera());
            double subTotalImpuesto = 0;
            for (M_egreso_detalle compraDetalle : detalles) {
                double exenta = 0;
                double iva5 = 0;
                double iva10 = 0;
                switch (compraDetalle.getProducto().getIdImpuesto()) {
                    case E_impuesto.EXENTA: {
                        //exenta = exenta + facturaDetalle.calcularSubTotal();
                        break;
                    }
                    case E_impuesto.IVA5: {
                        iva5 = iva5 + (compraDetalle.calcularSubTotal() / 21);
                        break;
                    }
                    case E_impuesto.IVA10: {
                        iva10 = iva10 + (compraDetalle.calcularSubTotal() / 11);
                        break;
                    }
                }
                impuestoIVA5 = impuestoIVA5 + iva5;
                impuestoIVA10 = impuestoIVA10 + iva10;
                subTotalImpuesto = subTotalImpuesto + exenta + iva5 + iva10;
                double subTotal = compraDetalle.calcularSubTotal();
                total = total + subTotal;
            }
            totalImpuesto = totalImpuesto + subTotalImpuesto;
            totalImpuestoIVA10 = totalImpuestoIVA10 + impuestoIVA10;
            totalImpuestoIVA5 = totalImpuestoIVA5 + impuestoIVA5;
            row.createCell(col).setCellValue(subTotalImpuesto);
            row.getCell(col).setCellStyle(styleNumber);
            col++;
            row.createCell(col).setCellValue(impuestoIVA5);
            row.getCell(col).setCellStyle(styleNumber);
            col++;
            row.createCell(col).setCellValue(impuestoIVA10);
            row.getCell(col).setCellStyle(styleNumber);
            col++;
            filaActual++;
        }
        rowTotalIngreso.createCell(1).setCellValue(total);
        rowTotalIngreso.getCell(1).setCellStyle(styleNumber);
        rowTotalImpuesto.createCell(1).setCellValue(totalImpuesto);
        rowTotalImpuesto.getCell(1).setCellStyle(styleNumber);
        rowTotalImpuestoIVA5.createCell(1).setCellValue(totalImpuestoIVA5);
        rowTotalImpuestoIVA5.getCell(1).setCellStyle(styleNumber);
        rowTotalImpuestoIVA10.createCell(1).setCellValue(totalImpuestoIVA10);
        rowTotalImpuestoIVA10.getCell(1).setCellStyle(styleNumber);
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
