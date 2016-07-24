/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Excel;

import Entities.M_egreso_detalleFX;
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
public class C_create_excel {

    public static int MULTIPLES_FECHAS = 1;
    public static int UNA_FECHA = 2;
    String nombreHoja;
    ArrayList<M_egreso_detalleFX> egresoDetalle;
    Date fechaInic, fechaFinal;
    HSSFWorkbook workbook;
    HSSFSheet sheet;
    CellStyle style1, style2, style3, style4;
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
}
