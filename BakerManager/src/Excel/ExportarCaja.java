/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Excel;

import Entities.ArqueoCajaDetalle;
import Entities.CierreCaja;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.apache.poi.ss.util.CellRangeAddress;

/**
 *
 * @author Ramiro Ferreira
 */
public class ExportarCaja {

    private HSSFWorkbook workbook;
    private HSSFSheet sheet;
    private CellStyle style1, style2, style3, style4;
    private HSSFCellStyle dateCellStyle;
    private final String NOMBRE_HOJA;
    private File directory;
    private ArrayList<CierreCaja> cierreCajas;

    /**
     *
     * @param nombreHoja Especifíca el nombre del archivo excel a crear.
     */
    public ExportarCaja(String nombreHoja) {
        this.NOMBRE_HOJA = nombreHoja;
        createWorkBook();
        createCellStyles();
    }

    public ExportarCaja(String nombreHoja, ArrayList<CierreCaja> cierreCajas) {
        this.NOMBRE_HOJA = nombreHoja;
        this.cierreCajas = cierreCajas;
        createWorkBook();
        createCellStyles();
    }

    private void createWorkBook() {
        workbook = new HSSFWorkbook();
        sheet = workbook.createSheet(NOMBRE_HOJA);
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

    private void prepararCabeza() {
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
        Date fecha = Calendar.getInstance().getTime();
        int fila = 0;
        Row fechaActual = sheet.createRow(fila);
        fechaActual.createCell(0).setCellValue(new HSSFRichTextString("Fecha creación:"));
        fechaActual.createCell(1).setCellValue(fecha);
        fechaActual.getCell(1).setCellStyle(dateCellStyle);
    }

    private void prepararPie() {
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

    private void prepararCuerpo() {
        int fila = 1;//En preparar cuerpo empieza en cero (0).
        for (CierreCaja cierreCaja : cierreCajas) {
            //CREAR FECHA DE CAJA
            Row fechaCaja = sheet.createRow(fila);
            fila++;
            fechaCaja.createCell(0).setCellValue(new HSSFRichTextString("Fecha:"));
            fechaCaja.createCell(1).setCellValue(cierreCaja.getCaja().getTiempoCierre());
            fechaCaja.getCell(1).setCellStyle(dateCellStyle);
            //CREAR CABECERA DE ARQUEOS (APERTURA,CIERRE,DEPOSITO)
            Row cabeceraCajas = sheet.createRow(fila);
            cabeceraCajas.createCell(0).setCellValue(new HSSFRichTextString("Caja apertura"));
            cabeceraCajas.createCell(3).setCellValue(new HSSFRichTextString("Caja cierre"));
            cabeceraCajas.createCell(6).setCellValue(new HSSFRichTextString("Depositado"));
            sheet.addMergedRegion(new CellRangeAddress(fila, fila, 0, 2));
            sheet.addMergedRegion(new CellRangeAddress(fila, fila, 3, 5));
            sheet.addMergedRegion(new CellRangeAddress(fila, fila, 6, 8));
            fila++;
            //SUB CABECERA DE ARQUEOS
            Row subCabeceraCajas = sheet.createRow(fila);
            subCabeceraCajas.createCell(0).setCellValue(new HSSFRichTextString("Cantidad"));
            subCabeceraCajas.createCell(1).setCellValue(new HSSFRichTextString("Denominación"));
            subCabeceraCajas.createCell(2).setCellValue(new HSSFRichTextString("Importe"));
            subCabeceraCajas.createCell(3).setCellValue(new HSSFRichTextString("Cantidad"));
            subCabeceraCajas.createCell(4).setCellValue(new HSSFRichTextString("Denominación"));
            subCabeceraCajas.createCell(5).setCellValue(new HSSFRichTextString("Importe"));
            subCabeceraCajas.createCell(6).setCellValue(new HSSFRichTextString("Cantidad"));
            subCabeceraCajas.createCell(7).setCellValue(new HSSFRichTextString("Denominación"));
            subCabeceraCajas.createCell(8).setCellValue(new HSSFRichTextString("Importe"));
            fila++;
            //CAJA APERTURA
            int SubCabeceraInicio = fila;
            int totalApertura = 0;
            for (ArqueoCajaDetalle arqueoCajaDetalle : cierreCaja.getApertura()) {
                Row cajaApertura = sheet.createRow(fila);
                int subtotal = arqueoCajaDetalle.getCantidad() * arqueoCajaDetalle.getMoneda().getValor();
                totalApertura = totalApertura + subtotal;
                cajaApertura.createCell(0).setCellValue(arqueoCajaDetalle.getCantidad());
                cajaApertura.createCell(1).setCellValue(arqueoCajaDetalle.getMoneda().toString());
                cajaApertura.createCell(2).setCellValue(subtotal);
                fila++;
            }
            //CAJA APERTURA
            fila = SubCabeceraInicio;
            int totalCierre = 0;
            for (ArqueoCajaDetalle arqueoCajaDetalle : cierreCaja.getCierre()) {
                Row cajaCierre = sheet.createRow(fila);
                int subtotal = arqueoCajaDetalle.getCantidad() * arqueoCajaDetalle.getMoneda().getValor();
                totalCierre = totalCierre + subtotal;
                cajaCierre.createCell(3).setCellValue(arqueoCajaDetalle.getCantidad());
                cajaCierre.createCell(4).setCellValue(arqueoCajaDetalle.getMoneda().toString());
                cajaCierre.createCell(5).setCellValue(subtotal);
                fila++;
            }
            //CAJA DEPOSITO
            fila = SubCabeceraInicio;
            int totalDeposito = 0;
            for (ArqueoCajaDetalle arqueoCajaDetalle : cierreCaja.getDeposito()) {
                Row cajaDeposito = sheet.createRow(fila);
                int subtotal = arqueoCajaDetalle.getCantidad() * arqueoCajaDetalle.getMoneda().getValor();
                totalDeposito = totalDeposito + subtotal;
                cajaDeposito.createCell(6).setCellValue(arqueoCajaDetalle.getCantidad());
                cajaDeposito.createCell(7).setCellValue(arqueoCajaDetalle.getMoneda().toString());
                cajaDeposito.createCell(8).setCellValue(subtotal);
                fila++;
            }
            //TOTALES
            Row totales = sheet.createRow(fila);
            totales.createCell(0).setCellValue(new HSSFRichTextString("Total apertura"));
            totales.createCell(2).setCellValue(totalApertura);
            totales.createCell(3).setCellValue(new HSSFRichTextString("Total cierre"));
            totales.createCell(5).setCellValue(totalCierre);
            totales.createCell(6).setCellValue(new HSSFRichTextString("Total depósito"));
            totales.createCell(8).setCellValue(totalDeposito);
            sheet.addMergedRegion(new CellRangeAddress(fila, fila, 0, 1));
            sheet.addMergedRegion(new CellRangeAddress(fila, fila, 3, 4));
            sheet.addMergedRegion(new CellRangeAddress(fila, fila, 6, 7));
            fila++;
            //TOTAL EGRESO
            int totalEgresos = cierreCaja.getCaja().getEgresoContado() + cierreCaja.getCaja().getEgresoCredito();
            Row totalEgreso = sheet.createRow(fila);
            totalEgreso.createCell(0).setCellValue(new HSSFRichTextString("Total egresos"));
            totalEgreso.createCell(2).setCellValue(totalEgresos);
            sheet.addMergedRegion(new CellRangeAddress(fila, fila, 0, 1));
            fila++;
            //TOTAL EGRESO
            int totalIngresos = cierreCaja.getCaja().getIngresoContado() + cierreCaja.getCaja().getIngresoCredito();
            Row totalIngreso = sheet.createRow(fila);
            totalIngreso.createCell(0).setCellValue(new HSSFRichTextString("Total ingresos"));
            totalIngreso.createCell(2).setCellValue(totalIngresos);
            sheet.addMergedRegion(new CellRangeAddress(fila, fila, 0, 1));
            fila++;
            //EGRESO+DEPOSITADO
            Row egresoDeposito = sheet.createRow(fila);
            egresoDeposito.createCell(0).setCellValue(new HSSFRichTextString("Egreso+Depositado"));
            egresoDeposito.createCell(2).setCellValue(totalDeposito);
            sheet.addMergedRegion(new CellRangeAddress(fila, fila, 0, 1));
            fila++;
        }
    }

    public void exportar() {
        prepararCabeza();
        prepararCuerpo();
        prepararPie();
    }
}
