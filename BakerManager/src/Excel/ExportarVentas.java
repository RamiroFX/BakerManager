/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Excel;

import Entities.E_facturaCabeceraFX;
import Entities.E_facturaDetalleFX;
import Entities.Estado;
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
 * @author Ramiro
 */
public class ExportarVentas {

    public static int MULTIPLES_FECHAS = 1;
    public static int UNA_FECHA = 2;
    String nombreHoja;
    ArrayList<E_facturaDetalleFX> facturaDetalle;
    ArrayList<E_facturaCabeceraFX> facturaCabeceraFX;
    Date fechaInic, fechaFinal;
    HSSFWorkbook workbook;
    HSSFSheet sheet;
    CellStyle style1, style2, style3, style4;
    HSSFCellStyle dateCellStyle;
    int tipo_fecha;

    public ExportarVentas(String nombreHoja, ArrayList<E_facturaDetalleFX> ed, Date inicio, Date fin) {
        this.nombreHoja = nombreHoja;
        this.facturaDetalle = ed;
        this.fechaInic = inicio;
        this.fechaFinal = fin;
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

    public ExportarVentas(String nombreHoja, ArrayList<E_facturaCabeceraFX> ic, Date inicio, Date fin, Estado estado) {
        this.nombreHoja = nombreHoja;
        this.facturaCabeceraFX = ic;
        this.fechaInic = inicio;
        this.fechaFinal = fin;
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
        totalEgreso2.createCell(0).setCellValue(new HSSFRichTextString("Total ingresos"));
        totalEgreso2.getCell(0).setCellStyle(style2);
        //------------
        int col = 0;
        cabecera.createCell(col).setCellValue(new HSSFRichTextString("Cliente"));
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
        int idEgresoCabecera = facturaDetalle.get(0).getIdFacturaCabecera();
        for (int i = 0; i < facturaDetalle.size(); i++) {
            SubTotal = SubTotal + (facturaDetalle.get(i).getTotal());
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

    public void initCompResumido() {
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
        totalEgreso2.createCell(0).setCellValue(new HSSFRichTextString("Total ingresos"));
        totalEgreso2.getCell(0).setCellStyle(style2);
        //INICIO CABECERA
        int col = 0;
        if (tipo_fecha == MULTIPLES_FECHAS) {
            cabecera.createCell(col).setCellValue(new HSSFRichTextString("Fecha"));
            cabecera.getCell(col).setCellStyle(style1);
            col++;
        }
        cabecera.createCell(col).setCellValue(new HSSFRichTextString("Nro. Factura"));
        cabecera.getCell(col).setCellStyle(style1);
        col++;
        cabecera.createCell(col).setCellValue(new HSSFRichTextString("Cliente"));
        cabecera.getCell(col).setCellStyle(style1);
        col++;
        cabecera.createCell(col).setCellValue(new HSSFRichTextString("Total"));
        cabecera.getCell(col).setCellStyle(style1);
        //FIN CABECERA

        Integer total = 0;
        Integer SubTotal = 0;
        for (int i = 0; i < facturaCabeceraFX.size(); i++) {
            SubTotal = SubTotal + (facturaCabeceraFX.get(i).getTotal());
        }
        total = total + SubTotal;
        //TOTAL EGRESOS
        totalEgreso2.createCell(1).setCellValue(total);
        totalEgreso2.getCell(1).setCellStyle(style4);
        SubTotal = 0;
        for (int i = 0; i < facturaCabeceraFX.size(); i++) {
            Row cuerpo = sheet.createRow(filaActual);
            //FECHA
            cuerpo.createCell(0).setCellValue(facturaCabeceraFX.get(i).getTiempo());
            cuerpo.getCell(0).setCellStyle(dateCellStyle);
            //NRO FACTURA
            cuerpo.createCell(1).setCellValue(facturaCabeceraFX.get(i).getNroFactura());
            //ENTIDAD
            cuerpo.createCell(2).setCellValue(new HSSFRichTextString(facturaCabeceraFX.get(i).getClienteEntidad()));
            //TOTAL
            cuerpo.createCell(3).setCellValue(facturaCabeceraFX.get(i).getTotal());
            cuerpo.getCell(3).setCellStyle(style4);
            filaActual++;
        }
        //AJUSTAR ANCHO DE COLUMNAS
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
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
        System.out.println("Excel.ExportarVentas.escribirCeldasConFecha()");
        int filaActual = 4;
        int idEgCab = egresocabecera;
        boolean b = true;
        Integer SubTotal = 0;
        for (int i = 0; i < facturaDetalle.size(); i++) {
            if (idEgCab == facturaDetalle.get(i).getIdFacturaCabecera()) {
                if (b) {
                    Row asd = sheet.createRow(filaActual);
                    asd.createCell(0).setCellValue(new HSSFRichTextString(facturaDetalle.get(i).getClienteEntidad()));
                    asd.getCell(0).setCellStyle(style2);
                    asd.createCell(1).setCellValue(facturaDetalle.get(i).getTiempo());
                    asd.getCell(1).setCellStyle(dateCellStyle);
                    if (facturaDetalle.get(i).getObservacion() != null) {
                        asd.createCell(2).setCellValue(new HSSFRichTextString(facturaDetalle.get(i).getProductoDescripcion() + "-(" + facturaDetalle.get(i).getObservacion() + ")"));
                    } else {
                        asd.createCell(2).setCellValue(new HSSFRichTextString(facturaDetalle.get(i).getProductoDescripcion()));
                    }
                    asd.createCell(3).setCellValue(facturaDetalle.get(i).getCantidad());
                    asd.createCell(4).setCellValue(facturaDetalle.get(i).getDescuento());
                    asd.createCell(5).setCellValue(facturaDetalle.get(i).getPrecio());
                    asd.createCell(6).setCellValue(facturaDetalle.get(i).getTotal());
                    for (int X = i; X < facturaDetalle.size(); X++) {
                        if (idEgCab == facturaDetalle.get(X).getIdFacturaCabecera()) {
                            SubTotal = SubTotal + facturaDetalle.get(X).getTotal();
                        } else {
                            X = facturaDetalle.size();
                        }
                    }
                    asd.createCell(7).setCellValue(SubTotal);
                    filaActual++;
                }
                if (!b) {
                    Row qwerty = sheet.createRow(filaActual);
                    filaActual++;
                    qwerty.createCell(2).setCellValue(new HSSFRichTextString(facturaDetalle.get(i).getProductoDescripcion()));
                    qwerty.createCell(3).setCellValue(facturaDetalle.get(i).getCantidad());
                    qwerty.createCell(4).setCellValue(facturaDetalle.get(i).getDescuento());
                    qwerty.createCell(5).setCellValue(facturaDetalle.get(i).getPrecio());
                    qwerty.createCell(6).setCellValue(facturaDetalle.get(i).getTotal());
                }
                b = false;
            } else {
                SubTotal = 0;
                b = true;
                idEgCab = facturaDetalle.get(i).getIdFacturaCabecera();
                i--;
                filaActual++;
            }
        }
    }

    private void escribirCeldasSinFecha(int egresocabecera) {
        System.out.println("Excel.ExportarVentas.escribirCeldasSinFecha()");
        int filaActual = 3;
        int idEgCab = egresocabecera;
        boolean b = true;
        Integer SubTotal = 0;
        for (int i = 0; i < facturaDetalle.size(); i++) {
            if (idEgCab == facturaDetalle.get(i).getIdFacturaCabecera()) {
                if (b) {
                    Row asd = sheet.createRow(filaActual);
                    asd.createCell(0).setCellValue(new HSSFRichTextString(facturaDetalle.get(i).getClienteEntidad()));
                    asd.getCell(0).setCellStyle(style2);
                    if (facturaDetalle.get(i).getObservacion() != null) {
                        asd.createCell(1).setCellValue(new HSSFRichTextString(facturaDetalle.get(i).getProductoDescripcion() + "-(" + facturaDetalle.get(i).getObservacion() + ")"));
                    } else {
                        asd.createCell(1).setCellValue(new HSSFRichTextString(facturaDetalle.get(i).getProductoDescripcion()));
                    }
                    asd.createCell(2).setCellValue(facturaDetalle.get(i).getCantidad());
                    asd.createCell(3).setCellValue(facturaDetalle.get(i).getDescuento());
                    asd.createCell(4).setCellValue(facturaDetalle.get(i).getPrecio());
                    asd.createCell(5).setCellValue(facturaDetalle.get(i).getTotal());
                    for (int X = i; X < facturaDetalle.size(); X++) {
                        if (idEgCab == facturaDetalle.get(X).getIdFacturaCabecera()) {
                            SubTotal = SubTotal + facturaDetalle.get(X).getTotal();
                        } else {
                            X = facturaDetalle.size();
                        }
                    }
                    asd.createCell(6).setCellValue(SubTotal);
                    filaActual++;
                }
                if (!b) {
                    Row qwerty = sheet.createRow(filaActual);
                    filaActual++;
                    qwerty.createCell(1).setCellValue(new HSSFRichTextString(facturaDetalle.get(i).getProductoDescripcion()));
                    qwerty.createCell(2).setCellValue(facturaDetalle.get(i).getCantidad());
                    qwerty.createCell(3).setCellValue(facturaDetalle.get(i).getDescuento());
                    qwerty.createCell(4).setCellValue(facturaDetalle.get(i).getPrecio());
                    qwerty.createCell(5).setCellValue(facturaDetalle.get(i).getTotal());
                }
                b = false;
            } else {
                SubTotal = 0;
                b = true;
                idEgCab = facturaDetalle.get(i).getIdFacturaCabecera();
                i--;
                filaActual++;
            }
        }
    }

}
