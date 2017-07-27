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
import org.apache.poi.ss.util.CellUtil;

/**
 *
 * @author Ramiro Ferreira
 */
public class ExportarCaja {

    private HSSFWorkbook workbook;
    private HSSFSheet sheet;
    private CellStyle style1, style2, style3, style4, style5;
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
        style4.setDataFormat(format.getFormat("#,##0"));

        dateCellStyle = workbook.createCellStyle();
        short df = workbook.createDataFormat().getFormat("dd-MM-yyyy");
        dateCellStyle.setDataFormat(df);

        style5 = workbook.createCellStyle();
        style5.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style5.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style5.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style5.setBorderLeft(HSSFCellStyle.BORDER_THIN);

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
            fila++;//Para dar una linea de espacio
            //CREAR FECHA DE CAJA
            Row fechaCaja = sheet.createRow(fila);
            fila++;
            fechaCaja.createCell(0).setCellValue(new HSSFRichTextString("Fecha:"));
            fechaCaja.createCell(1).setCellValue(cierreCaja.getCaja().getTiempoCierre());
            fechaCaja.getCell(1).setCellStyle(dateCellStyle);
            //CREAR CABECERA DE ARQUEOS (APERTURA,CIERRE,DEPOSITO)
            Row cabeceraCajas = sheet.createRow(fila);
            sheet.addMergedRegion(new CellRangeAddress(fila, fila, 0, 2));
            sheet.addMergedRegion(new CellRangeAddress(fila, fila, 3, 5));
            sheet.addMergedRegion(new CellRangeAddress(fila, fila, 6, 8));
            cabeceraCajas.createCell(0).setCellValue(new HSSFRichTextString("Caja apertura"));
            cabeceraCajas.createCell(1).setCellValue("");
            cabeceraCajas.createCell(2).setCellValue("");
            cabeceraCajas.createCell(3).setCellValue(new HSSFRichTextString("Caja cierre"));
            cabeceraCajas.createCell(4).setCellValue("");
            cabeceraCajas.createCell(5).setCellValue("");
            cabeceraCajas.createCell(6).setCellValue(new HSSFRichTextString("Depositado"));
            cabeceraCajas.createCell(7).setCellValue("");
            cabeceraCajas.createCell(8).setCellValue("");
            cabeceraCajas.getCell(0).setCellStyle(style5);
            cabeceraCajas.getCell(1).setCellStyle(style5);
            cabeceraCajas.getCell(2).setCellStyle(style5);
            cabeceraCajas.getCell(3).setCellStyle(style5);
            cabeceraCajas.getCell(4).setCellStyle(style5);
            cabeceraCajas.getCell(5).setCellStyle(style5);
            cabeceraCajas.getCell(6).setCellStyle(style5);
            cabeceraCajas.getCell(7).setCellStyle(style5);
            cabeceraCajas.getCell(8).setCellStyle(style5);
            CellUtil.setAlignment(cabeceraCajas.getCell(0), workbook, CellStyle.ALIGN_CENTER);
            CellUtil.setAlignment(cabeceraCajas.getCell(3), workbook, CellStyle.ALIGN_CENTER);
            CellUtil.setAlignment(cabeceraCajas.getCell(6), workbook, CellStyle.ALIGN_CENTER);
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
            int totalApertura = 0;//Para sumar el total de la caja apertura
            int totalCierre = 0;//Para sumar el total de la caja apertura
            int totalDeposito = 0;//Para sumar el total de la caja apertura

            for (int i = 0; i < cierreCaja.obtenerCajaConMasMonedas(); i++) {
                Row arqueoCajas = sheet.createRow(fila);
                if (i < cierreCaja.getApertura().size()) {
                    int subtotalApertura = cierreCaja.getApertura().get(i).getCantidad() * cierreCaja.getApertura().get(i).getMoneda().getValor();
                    totalApertura = totalApertura + subtotalApertura;
                    arqueoCajas.createCell(0).setCellValue(cierreCaja.getApertura().get(i).getCantidad());
                    arqueoCajas.getCell(0).setCellStyle(style4);
                    arqueoCajas.createCell(1).setCellValue(cierreCaja.getApertura().get(i).getMoneda().toString());
                    arqueoCajas.createCell(2).setCellValue(subtotalApertura);
                    arqueoCajas.getCell(2).setCellStyle(style4);
                }
                if (i < cierreCaja.getCierre().size()) {
                    int subtotalCierre = cierreCaja.getCierre().get(i).getCantidad() * cierreCaja.getCierre().get(i).getMoneda().getValor();
                    totalCierre = totalCierre + subtotalCierre;
                    arqueoCajas.createCell(3).setCellValue(cierreCaja.getCierre().get(i).getCantidad());
                    arqueoCajas.getCell(3).setCellStyle(style4);
                    arqueoCajas.createCell(4).setCellValue(cierreCaja.getCierre().get(i).getMoneda().toString());
                    arqueoCajas.createCell(5).setCellValue(subtotalCierre);
                    arqueoCajas.getCell(5).setCellStyle(style4);
                }
                if (i < cierreCaja.getDeposito().size()) {
                    int subtotalDeposito = cierreCaja.getDeposito().get(i).getCantidad() * cierreCaja.getDeposito().get(i).getMoneda().getValor();
                    totalDeposito = totalDeposito + subtotalDeposito;
                    arqueoCajas.createCell(6).setCellValue(cierreCaja.getDeposito().get(i).getCantidad());
                    arqueoCajas.getCell(6).setCellStyle(style4);
                    arqueoCajas.createCell(7).setCellValue(cierreCaja.getDeposito().get(i).getMoneda().toString());
                    arqueoCajas.createCell(8).setCellValue(subtotalDeposito);
                    arqueoCajas.getCell(8).setCellStyle(style4);
                }
                fila++;
            }
            //TOTALES
            Row totales = sheet.createRow(fila);
            sheet.addMergedRegion(new CellRangeAddress(fila, fila, 0, 1));
            sheet.addMergedRegion(new CellRangeAddress(fila, fila, 3, 4));
            sheet.addMergedRegion(new CellRangeAddress(fila, fila, 6, 7));
            totales.createCell(0).setCellValue(new HSSFRichTextString("Total apertura"));
            totales.createCell(2).setCellValue(totalApertura);
            totales.getCell(2).setCellStyle(style4);
            totales.createCell(3).setCellValue(new HSSFRichTextString("Total cierre"));
            totales.createCell(5).setCellValue(totalCierre);
            totales.getCell(5).setCellStyle(style4);
            totales.createCell(6).setCellValue(new HSSFRichTextString("Total depósito"));
            totales.createCell(8).setCellValue(totalDeposito);
            totales.getCell(8).setCellStyle(style4);
            CellUtil.setAlignment(totales.getCell(0), workbook, CellStyle.ALIGN_CENTER);
            CellUtil.setAlignment(totales.getCell(3), workbook, CellStyle.ALIGN_CENTER);
            CellUtil.setAlignment(totales.getCell(6), workbook, CellStyle.ALIGN_CENTER);
            fila++;
            //TOTAL EGRESO
            int totalEgresos = cierreCaja.getCaja().getEgresoContado() + cierreCaja.getCaja().getEgresoCredito();
            Row totalEgreso = sheet.createRow(fila);
            totalEgreso.createCell(0).setCellValue(new HSSFRichTextString("Total egresos"));
            totalEgreso.createCell(2).setCellValue(totalEgresos);
            totalEgreso.getCell(2).setCellStyle(style4);
            sheet.addMergedRegion(new CellRangeAddress(fila, fila, 0, 1));
            fila++;
            //TOTAL EGRESO
            int totalIngresos = cierreCaja.getCaja().getIngresoContado() + cierreCaja.getCaja().getIngresoCredito();
            Row totalIngreso = sheet.createRow(fila);
            totalIngreso.createCell(0).setCellValue(new HSSFRichTextString("Total ingresos"));
            totalIngreso.createCell(2).setCellValue(totalIngresos);
            totalIngreso.getCell(2).setCellStyle(style4);
            sheet.addMergedRegion(new CellRangeAddress(fila, fila, 0, 1));
            fila++;
            //EGRESO+DEPOSITADO
            Row egresoDeposito = sheet.createRow(fila);
            egresoDeposito.createCell(0).setCellValue(new HSSFRichTextString("Egreso+Depositado"));
            egresoDeposito.createCell(2).setCellValue(totalDeposito);
            egresoDeposito.getCell(2).setCellStyle(style4);
            sheet.addMergedRegion(new CellRangeAddress(fila, fila, 0, 1));
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
            sheet.autoSizeColumn(5);
            sheet.autoSizeColumn(6);
            sheet.autoSizeColumn(7);
            sheet.autoSizeColumn(8);
            sheet.autoSizeColumn(9);
            fila++;
        }
    }

    public void exportar() {
        prepararCabeza();
        prepararCuerpo();
        prepararPie();
    }
}
