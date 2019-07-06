/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Excel;

import DB.DB_Caja;
import DB.DB_Egreso;
import DB.DB_Funcionario;
import DB.DB_Ingreso;
import Entities.ArqueoCajaDetalle;
import Entities.CierreCaja;
import Entities.M_funcionario;
import Entities.Moneda;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
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
import org.apache.poi.ss.util.CellUtil;

/**
 *
 * @author Ramiro Ferreira
 */
public class ExportarCaja {

    private static final String RESUMEN = "Resumen";
    private static final String MENSAJE_ERROR = "No hay cajas para exportar";
    private static final String TITULO_ERROR = "Atención";

    private HSSFWorkbook workbook;
    private ArrayList<HSSFSheet> sheets;
    private CellStyle style1, style2, style3, style4, style5, style6, style7;
    private HSSFCellStyle dateCellStyle;
    private File directory;
    private ArrayList<CierreCaja> cierreCajas;
    private SimpleDateFormat sdfs;

    /**
     *
     * @param nombreHoja Especifíca el nombre del archivo excel a crear.
     */
    public ExportarCaja() {
        this.sdfs = new SimpleDateFormat("MM-yyyy");
        createWorkBook();
        createCellStyles();
    }

    public ExportarCaja(ArrayList<CierreCaja> cierreCajas) {
        this.sdfs = new SimpleDateFormat("MM-yyyy");
        this.cierreCajas = cierreCajas;
        createWorkBook();
        createCellStyles();
    }

    private void createWorkBook() {
        workbook = new HSSFWorkbook();
        sheets = new ArrayList<>();
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

        style6 = workbook.createCellStyle();
        style6.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style6.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style6.setDataFormat(format.getFormat("#,##0"));

        style7 = workbook.createCellStyle();
        style7.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style7.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style7.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style7.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style7.setDataFormat(format.getFormat("#,##0"));

        //END FORMAT STYLE
    }

    public void exportar() {
        //PREPARAR CONTENIDO
        int fila = 0;//En preparar cuerpo empieza en cero (0).
        Date fechaActual = null;
        Calendar calendar = Calendar.getInstance();
        int monthCursor = 0;
        int currentMonth = 0;
        int newMonth = 0;
        int resumenTotalEgreso = 0;
        int resumenTotalIngreso = 0;
        int resumenTotalDepositado = 0;
        Date resumenFechaInicio = null;
        Date resumenFechaFin = null;
        sheets.clear();
        if (cierreCajas != null && !cierreCajas.isEmpty()) {
            fechaActual = cierreCajas.get(0).getCaja().getTiempoCierre();
            resumenFechaInicio = fechaActual;
            resumenFechaFin = fechaActual;
            calendar.setTime(fechaActual);
            currentMonth = calendar.get(Calendar.MONTH);
            sheets.add(workbook.createSheet(sdfs.format(calendar.getTime())));
        } else {
            JOptionPane.showMessageDialog(null, MENSAJE_ERROR, TITULO_ERROR, JOptionPane.ERROR_MESSAGE);
            return;
        }
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
        ArrayList<Moneda> monedas = DB_Caja.obtenerMonedas();
        for (CierreCaja cierreCaja : cierreCajas) {
            fechaActual = cierreCaja.getCaja().getTiempoCierre();
            resumenFechaFin = fechaActual;
            calendar.setTime(fechaActual);
            newMonth = calendar.get(Calendar.MONTH);
            if (currentMonth != newMonth) {
                currentMonth = newMonth;
                sheets.add(workbook.createSheet(sdfs.format(calendar.getTime())));
                monthCursor++;
                fila = 0;
            }
            fila++;//PARA DAR ESPACIO
            //CREAR FECHA DE CAJA
            Row fechaCaja = sheets.get(monthCursor).createRow(fila);
            fechaCaja.createCell(0).setCellValue(new HSSFRichTextString("Fecha:"));
            fechaCaja.createCell(1).setCellValue(cierreCaja.getCaja().getTiempoCierre());
            fechaCaja.getCell(1).setCellStyle(dateCellStyle);
            fila++;
            //FUNCIONARIOS
            Row funcionarioApertura = sheets.get(monthCursor).createRow(fila);
            sheets.get(monthCursor).addMergedRegion(new CellRangeAddress(fila, fila, 0, 1));
            sheets.get(monthCursor).addMergedRegion(new CellRangeAddress(fila, fila, 2, 3));
            funcionarioApertura.createCell(0).setCellValue(new HSSFRichTextString("Funcionario de apertura"));
            funcionarioApertura.createCell(1).setCellValue("");
            int idFuncApertura = cierreCaja.getCaja().getIdEmpleadoApertura();
            M_funcionario funcApertura = DB_Funcionario.obtenerDatosFuncionarioID(idFuncApertura);
            String nombreFuncApertura = funcApertura.getNombre() + " " + funcApertura.getApellido();
            funcionarioApertura.createCell(2).setCellValue(idFuncApertura + "-" + nombreFuncApertura);
            fila++;
            Row funcionarioCierre = sheets.get(monthCursor).createRow(fila);
            sheets.get(monthCursor).addMergedRegion(new CellRangeAddress(fila, fila, 0, 1));
            sheets.get(monthCursor).addMergedRegion(new CellRangeAddress(fila, fila, 2, 3));
            funcionarioCierre.createCell(0).setCellValue(new HSSFRichTextString("Funcionario de cierre"));
            funcionarioCierre.createCell(1).setCellValue("");
            int idFuncCierre = cierreCaja.getCaja().getIdEmpleadoCierre();
            M_funcionario funcCierre = DB_Funcionario.obtenerDatosFuncionarioID(idFuncCierre);
            String nombreFuncCierre = funcCierre.getNombre() + " " + funcCierre.getApellido();
            funcionarioCierre.createCell(2).setCellValue(idFuncCierre + "-" + nombreFuncCierre);
            fila++;
            //CREAR CABECERA DE ARQUEOS (APERTURA,CIERRE,DEPOSITO)
            Row cabeceraCajas = sheets.get(monthCursor).createRow(fila);
            sheets.get(monthCursor).addMergedRegion(new CellRangeAddress(fila, fila, 0, 2));
            sheets.get(monthCursor).addMergedRegion(new CellRangeAddress(fila, fila, 3, 5));
            sheets.get(monthCursor).addMergedRegion(new CellRangeAddress(fila, fila, 6, 8));
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
            Row subCabeceraCajas = sheets.get(monthCursor).createRow(fila);
            subCabeceraCajas.createCell(0).setCellValue(new HSSFRichTextString("Cantidad"));
            subCabeceraCajas.createCell(1).setCellValue(new HSSFRichTextString("Denominación"));
            subCabeceraCajas.createCell(2).setCellValue(new HSSFRichTextString("Importe"));
            subCabeceraCajas.createCell(3).setCellValue(new HSSFRichTextString("Cantidad"));
            subCabeceraCajas.createCell(4).setCellValue(new HSSFRichTextString("Denominación"));
            subCabeceraCajas.createCell(5).setCellValue(new HSSFRichTextString("Importe"));
            subCabeceraCajas.createCell(6).setCellValue(new HSSFRichTextString("Cantidad"));
            subCabeceraCajas.createCell(7).setCellValue(new HSSFRichTextString("Denominación"));
            subCabeceraCajas.createCell(8).setCellValue(new HSSFRichTextString("Importe"));
            subCabeceraCajas.getCell(0).setCellStyle(style5);
            subCabeceraCajas.getCell(1).setCellStyle(style5);
            subCabeceraCajas.getCell(2).setCellStyle(style5);
            subCabeceraCajas.getCell(3).setCellStyle(style5);
            subCabeceraCajas.getCell(4).setCellStyle(style5);
            subCabeceraCajas.getCell(5).setCellStyle(style5);
            subCabeceraCajas.getCell(6).setCellStyle(style5);
            subCabeceraCajas.getCell(7).setCellStyle(style5);
            subCabeceraCajas.getCell(8).setCellStyle(style5);
            fila++;
            //CAJA APERTURA
            int totalApertura = 0;//Para sumar el total de la caja apertura
            int totalCierre = 0;//Para sumar el total de la caja apertura
            int totalDeposito = 0;//Para sumar el total de la caja apertura

            for (Moneda moneda : monedas) {
                Row arqueoCajas = sheets.get(monthCursor).createRow(fila);
                fila++;
                arqueoCajas.createCell(0);
                arqueoCajas.createCell(1).setCellValue(moneda.toString());
                arqueoCajas.createCell(2);
                arqueoCajas.createCell(3);
                arqueoCajas.createCell(4).setCellValue(moneda.toString());
                arqueoCajas.createCell(5);
                arqueoCajas.createCell(6);
                arqueoCajas.createCell(7).setCellValue(moneda.toString());
                arqueoCajas.createCell(8);
                boolean b = true;
                //APERTURA CAJA
                for (ArqueoCajaDetalle arqueoCajaDetalle : cierreCaja.getApertura()) {
                    if (arqueoCajaDetalle.getMoneda().getIdMoneda() == moneda.getIdMoneda()) {
                        int subtotalApertura = arqueoCajaDetalle.getCantidad() * arqueoCajaDetalle.getMoneda().getValor();
                        totalApertura = totalApertura + subtotalApertura;
                        arqueoCajas.getCell(0).setCellValue(arqueoCajaDetalle.getCantidad());
                        arqueoCajas.getCell(2).setCellValue(subtotalApertura);
                        b = false;
                        break;
                    }
                }
                if (b) {
                    arqueoCajas.getCell(0).setCellValue(0);
                    arqueoCajas.getCell(2).setCellValue(0);
                }
                b = true;
                //CIERRE CAJA
                for (ArqueoCajaDetalle arqueoCajaDetalle : cierreCaja.getCierre()) {
                    if (arqueoCajaDetalle.getMoneda().getIdMoneda() == moneda.getIdMoneda()) {
                        int subtotalCierre = arqueoCajaDetalle.getCantidad() * arqueoCajaDetalle.getMoneda().getValor();
                        totalCierre = totalCierre + subtotalCierre;
                        arqueoCajas.getCell(3).setCellValue(arqueoCajaDetalle.getCantidad());
                        arqueoCajas.getCell(5).setCellValue(subtotalCierre);
                        b = false;
                        break;
                    }
                }
                if (b) {
                    arqueoCajas.getCell(3).setCellValue(0);
                    arqueoCajas.getCell(5).setCellValue(0);
                }
                b = true;
                //CAJA DEPOSITO
                for (ArqueoCajaDetalle arqueoCajaDetalle : cierreCaja.getDeposito()) {
                    b = true;
                    if (arqueoCajaDetalle.getMoneda().getIdMoneda() == moneda.getIdMoneda()) {
                        int subtotalDeposito = arqueoCajaDetalle.getCantidad() * arqueoCajaDetalle.getMoneda().getValor();
                        totalDeposito = totalDeposito + subtotalDeposito;
                        arqueoCajas.getCell(6).setCellValue(arqueoCajaDetalle.getCantidad());
                        arqueoCajas.getCell(8).setCellValue(subtotalDeposito);
                        b = false;
                        break;
                    }
                }
                if (b) {
                    arqueoCajas.getCell(6).setCellValue(0);
                    arqueoCajas.getCell(8).setCellValue(0);
                }
                arqueoCajas.getCell(0).setCellStyle(style6);
                arqueoCajas.getCell(1).setCellStyle(style6);
                arqueoCajas.getCell(2).setCellStyle(style6);
                arqueoCajas.getCell(3).setCellStyle(style6);
                arqueoCajas.getCell(4).setCellStyle(style6);
                arqueoCajas.getCell(5).setCellStyle(style6);
                arqueoCajas.getCell(6).setCellStyle(style6);
                arqueoCajas.getCell(7).setCellStyle(style6);
                arqueoCajas.getCell(8).setCellStyle(style6);
            }
            //TOTALES
            Row totales = sheets.get(monthCursor).createRow(fila);
            CellRangeAddress cellRangeAddress1 = new CellRangeAddress(fila, fila, 0, 1);
            CellRangeAddress cellRangeAddress2 = new CellRangeAddress(fila, fila, 3, 4);
            CellRangeAddress cellRangeAddress3 = new CellRangeAddress(fila, fila, 6, 7);
            sheets.get(monthCursor).addMergedRegion(cellRangeAddress1);
            sheets.get(monthCursor).addMergedRegion(cellRangeAddress2);
            sheets.get(monthCursor).addMergedRegion(cellRangeAddress3);
            totales.createCell(0).setCellValue(new HSSFRichTextString("Total apertura"));
            totales.createCell(2).setCellValue(totalApertura);
            totales.createCell(3).setCellValue(new HSSFRichTextString("Total cierre"));
            totales.createCell(5).setCellValue(totalCierre);
            totales.createCell(6).setCellValue(new HSSFRichTextString("Total depósito"));
            totales.createCell(8).setCellValue(totalDeposito);
            //BORDE para totales
            HSSFRegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress1, sheets.get(monthCursor), workbook);
            HSSFRegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress1, sheets.get(monthCursor), workbook);
            HSSFRegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress1, sheets.get(monthCursor), workbook);
            HSSFRegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress1, sheets.get(monthCursor), workbook);
            totales.getCell(2).setCellStyle(style7);
            HSSFRegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress2, sheets.get(monthCursor), workbook);
            HSSFRegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress2, sheets.get(monthCursor), workbook);
            HSSFRegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress2, sheets.get(monthCursor), workbook);
            HSSFRegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress2, sheets.get(monthCursor), workbook);
            totales.getCell(5).setCellStyle(style7);
            HSSFRegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress3, sheets.get(monthCursor), workbook);
            HSSFRegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress3, sheets.get(monthCursor), workbook);
            HSSFRegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress3, sheets.get(monthCursor), workbook);
            HSSFRegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress3, sheets.get(monthCursor), workbook);
            totales.getCell(8).setCellStyle(style7);
            CellUtil.setAlignment(totales.getCell(0), workbook, CellStyle.ALIGN_CENTER);
            CellUtil.setAlignment(totales.getCell(3), workbook, CellStyle.ALIGN_CENTER);
            CellUtil.setAlignment(totales.getCell(6), workbook, CellStyle.ALIGN_CENTER);
            fila++;
            //TOTAL EGRESO
            Calendar inicio = Calendar.getInstance();
            inicio.setTime(cierreCaja.getCaja().getTiempoApertura());
            inicio.set(Calendar.HOUR_OF_DAY, 0);
            inicio.set(Calendar.MINUTE, 0);
            Calendar fin = Calendar.getInstance();
            fin.setTime(cierreCaja.getCaja().getTiempoApertura());
            fin.set(Calendar.HOUR_OF_DAY, 23);
            fin.set(Calendar.MINUTE, 59);
            Timestamp ini = new Timestamp(inicio.getTimeInMillis());
            Timestamp fi = new Timestamp(fin.getTimeInMillis());
            int egresoContado = DB_Egreso.obtenerTotalEgreso(ini, fi, 1);
            int egresoCretdito = DB_Egreso.obtenerTotalEgreso(ini, fi, 2);
            int totalEgresos = egresoContado + egresoCretdito;
            Row totalEgreso = sheets.get(monthCursor).createRow(fila);
            totalEgreso.createCell(0).setCellValue(new HSSFRichTextString("Total egresos"));
            totalEgreso.createCell(2).setCellValue(totalEgresos);
            totalEgreso.getCell(2).setCellStyle(style4);
            sheets.get(monthCursor).addMergedRegion(new CellRangeAddress(fila, fila, 0, 1));
            fila++;
            //TOTAL EGRESO
            int ingresoContado = DB_Ingreso.obtenerTotalIngreso(ini, fi, 1);
            int ingresoCretdito = DB_Ingreso.obtenerTotalIngreso(ini, fi, 2);
            int totalIngresos = ingresoContado + ingresoCretdito;
            Row totalIngreso = sheets.get(monthCursor).createRow(fila);
            totalIngreso.createCell(0).setCellValue(new HSSFRichTextString("Total ingresos"));
            totalIngreso.createCell(2).setCellValue(totalIngresos);
            totalIngreso.getCell(2).setCellStyle(style4);
            sheets.get(monthCursor).addMergedRegion(new CellRangeAddress(fila, fila, 0, 1));
            fila++;
            //EGRESO+DEPOSITADO
            Row egresoDeposito = sheets.get(monthCursor).createRow(fila);
            egresoDeposito.createCell(0).setCellValue(new HSSFRichTextString("Egreso+Depositado"));
            egresoDeposito.createCell(2).setCellValue(totalDeposito + totalEgresos);
            egresoDeposito.getCell(2).setCellStyle(style4);
            sheets.get(monthCursor).addMergedRegion(new CellRangeAddress(fila, fila, 0, 1));
            sheets.get(monthCursor).autoSizeColumn(1);
            sheets.get(monthCursor).autoSizeColumn(2);
            sheets.get(monthCursor).autoSizeColumn(3);
            sheets.get(monthCursor).autoSizeColumn(4);
            sheets.get(monthCursor).autoSizeColumn(5);
            sheets.get(monthCursor).autoSizeColumn(6);
            sheets.get(monthCursor).autoSizeColumn(7);
            sheets.get(monthCursor).autoSizeColumn(8);
            sheets.get(monthCursor).autoSizeColumn(9);

            resumenTotalEgreso = resumenTotalEgreso + totalEgresos;
            resumenTotalIngreso = resumenTotalIngreso + totalIngresos;
            resumenTotalDepositado = resumenTotalDepositado + totalDeposito;
            fila++;
        }
        fila = 0;
        sheets.add(workbook.createSheet(RESUMEN));
        monthCursor++;

        CellRangeAddress cellRangeAddress1 = new CellRangeAddress(2, 2, 0, 1);
        CellRangeAddress cellRangeAddress2 = new CellRangeAddress(3, 3, 0, 1);
        CellRangeAddress cellRangeAddress3 = new CellRangeAddress(4, 4, 0, 1);

        Row fechaInicio = sheets.get(monthCursor).createRow(fila);
        fechaInicio.createCell(0).setCellValue(new HSSFRichTextString("Fecha inicio"));
        fechaInicio.createCell(1).setCellValue(resumenFechaInicio);
        fechaInicio.getCell(1).setCellStyle(dateCellStyle);
        fila++;
        Row fechaFin = sheets.get(monthCursor).createRow(fila);
        fechaFin.createCell(0).setCellValue(new HSSFRichTextString("Fecha fin"));
        fechaFin.createCell(1).setCellValue(resumenFechaFin);
        fechaFin.getCell(1).setCellStyle(dateCellStyle);
        fila++;

        Row resumenTotalEgresos = sheets.get(monthCursor).createRow(fila);
        resumenTotalEgresos.createCell(0).setCellValue(new HSSFRichTextString("Total egresos"));
        resumenTotalEgresos.createCell(1).setCellValue(resumenTotalEgreso);
        HSSFRegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress1, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress1, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress1, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress1, sheets.get(monthCursor), workbook);
        resumenTotalEgresos.getCell(1).setCellStyle(style7);
        fila++;

        Row resumenTotalIngresos = sheets.get(monthCursor).createRow(fila);
        resumenTotalIngresos.createCell(0).setCellValue(new HSSFRichTextString("Total ingresos"));
        resumenTotalIngresos.createCell(1).setCellValue(resumenTotalIngreso);
        HSSFRegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress2, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress2, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress2, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress2, sheets.get(monthCursor), workbook);
        resumenTotalIngresos.getCell(1).setCellStyle(style7);
        fila++;

        Row resumenTotalDepo = sheets.get(monthCursor).createRow(fila);
        resumenTotalDepo.createCell(0).setCellValue(new HSSFRichTextString("Total depositado"));
        resumenTotalDepo.createCell(1).setCellValue(resumenTotalDepositado);
        HSSFRegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress3, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress3, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress3, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress3, sheets.get(monthCursor), workbook);
        resumenTotalDepo.getCell(1).setCellStyle(style7);
        fila++;

        sheets.get(monthCursor).autoSizeColumn(0);
        sheets.get(monthCursor).autoSizeColumn(1);

        //PREPARAR DOCUMENTO
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

    public void exportarMinimalista() {
        //PREPARAR CONTENIDO
        int fila = 0;//En preparar cuerpo empieza en cero (0).
        Date fechaActual = null;
        Calendar calendar = Calendar.getInstance();
        int monthCursor = 0;
        int currentMonth = 0;
        int newMonth = 0;
        int resumenTotalEgreso = 0;
        int resumenTotalIngreso = 0;
        int resumenTotalDepositado = 0;
        Date resumenFechaInicio = null;
        Date resumenFechaFin = null;
        sheets.clear();
        if (cierreCajas != null && !cierreCajas.isEmpty()) {
            fechaActual = cierreCajas.get(0).getCaja().getTiempoCierre();
            resumenFechaInicio = fechaActual;
            resumenFechaFin = fechaActual;
            calendar.setTime(fechaActual);
            currentMonth = calendar.get(Calendar.MONTH);
            sheets.add(workbook.createSheet(sdfs.format(calendar.getTime())));
            //CREAR FECHA DE CAJA
            Row cabecera = sheets.get(monthCursor).createRow(fila);
            cabecera.createCell(0).setCellValue(new HSSFRichTextString("Fecha"));
            cabecera.createCell(1).setCellValue(new HSSFRichTextString("Total egreso"));
            cabecera.createCell(2).setCellValue(new HSSFRichTextString("Total ingreso"));
            fila++;
        } else {
            JOptionPane.showMessageDialog(null, MENSAJE_ERROR, TITULO_ERROR, JOptionPane.ERROR_MESSAGE);
            return;
        }
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
        for (CierreCaja cierreCaja : cierreCajas) {
            fechaActual = cierreCaja.getCaja().getTiempoCierre();
            resumenFechaFin = fechaActual;
            calendar.setTime(fechaActual);
            newMonth = calendar.get(Calendar.MONTH);
            if (currentMonth != newMonth) {
                currentMonth = newMonth;
                sheets.add(workbook.createSheet(sdfs.format(calendar.getTime())));
                monthCursor++;
                fila = 0;
                //CREAR FECHA DE CAJA
                Row cabecera = sheets.get(monthCursor).createRow(fila);
                cabecera.createCell(0).setCellValue(new HSSFRichTextString("Fecha"));
                cabecera.createCell(1).setCellValue(new HSSFRichTextString("Total egreso"));
                cabecera.createCell(2).setCellValue(new HSSFRichTextString("Total ingreso"));
                cabecera.createCell(3).setCellValue(new HSSFRichTextString("Depositado"));
                fila++;
            }
            //TOTAL EGRESO
            Calendar inicio = Calendar.getInstance();
            inicio.setTime(cierreCaja.getCaja().getTiempoApertura());
            inicio.set(Calendar.HOUR_OF_DAY, 0);
            inicio.set(Calendar.MINUTE, 0);
            Calendar fin = Calendar.getInstance();
            fin.setTime(cierreCaja.getCaja().getTiempoApertura());
            fin.set(Calendar.HOUR_OF_DAY, 23);
            fin.set(Calendar.MINUTE, 59);
            Timestamp ini = new Timestamp(inicio.getTimeInMillis());
            Timestamp fi = new Timestamp(fin.getTimeInMillis());
            //TOTAL EGRESO
            int egresoContado = DB_Egreso.obtenerTotalEgreso(ini, fi, 1);
            int egresoCretdito = DB_Egreso.obtenerTotalEgreso(ini, fi, 2);
            int totalEgresos = egresoContado + egresoCretdito;
            //TOTAL INGRESO
            int ingresoContado = DB_Ingreso.obtenerTotalIngreso(ini, fi, 1);
            int ingresoCretdito = DB_Ingreso.obtenerTotalIngreso(ini, fi, 2);
            int totalIngresos = ingresoContado + ingresoCretdito;
            //TOTAL DEPOSITADO
            int totalDepositado = 0;
            int idCaja = 0;
            for (ArqueoCajaDetalle arqueoCajaDetalle : cierreCaja.getDeposito()) {
                //System.out.println("arqueoCajaDetalle.toString()" + arqueoCajaDetalle.toString());
                System.out.println("arqueoCajaDetalle.getIdArqueoCajaDetalle()" + arqueoCajaDetalle.getIdArqueoCajaDetalle());
                idCaja = arqueoCajaDetalle.getIdCaja();
                totalDepositado = totalDepositado + (arqueoCajaDetalle.getCantidad() * arqueoCajaDetalle.getMoneda().getValor());
                /*System.out.println("totalDepositado: " + totalDepositado);
                System.out.println("getCantidad*getValor: " + (arqueoCajaDetalle.getCantidad() * arqueoCajaDetalle.getMoneda().getValor()));*/
            }
            //FILA DE CONTENIDO
            Row contenido = sheets.get(monthCursor).createRow(fila);
            contenido.createCell(0).setCellValue(cierreCaja.getCaja().getTiempoCierre());
            contenido.getCell(0).setCellStyle(dateCellStyle);
            contenido.createCell(1).setCellValue(totalEgresos);
            contenido.getCell(1).setCellStyle(style4);
            contenido.createCell(2).setCellValue(totalIngresos);
            contenido.getCell(2).setCellStyle(style4);
            contenido.createCell(3).setCellValue(totalDepositado);
            contenido.getCell(3).setCellStyle(style4);
            fila++;

            //AUTO EXTENDER COLUMNAS
            sheets.get(monthCursor).autoSizeColumn(0);
            sheets.get(monthCursor).autoSizeColumn(1);
            sheets.get(monthCursor).autoSizeColumn(2);
            sheets.get(monthCursor).autoSizeColumn(3);
            sheets.get(monthCursor).autoSizeColumn(4);

            resumenTotalEgreso = resumenTotalEgreso + totalEgresos;
            resumenTotalIngreso = resumenTotalIngreso + totalIngresos;
        }
        //RESUMEN EN OTRO PESTANHA
        fila = 0;
        sheets.add(workbook.createSheet(RESUMEN));
        monthCursor++;

        CellRangeAddress cellRangeAddress1 = new CellRangeAddress(2, 2, 0, 1);
        CellRangeAddress cellRangeAddress2 = new CellRangeAddress(3, 3, 0, 1);
        CellRangeAddress cellRangeAddress3 = new CellRangeAddress(4, 4, 0, 1);

        Row fechaInicio = sheets.get(monthCursor).createRow(fila);
        fechaInicio.createCell(0).setCellValue(new HSSFRichTextString("Fecha inicio"));
        fechaInicio.createCell(1).setCellValue(resumenFechaInicio);
        fechaInicio.getCell(1).setCellStyle(dateCellStyle);
        fila++;
        Row fechaFin = sheets.get(monthCursor).createRow(fila);
        fechaFin.createCell(0).setCellValue(new HSSFRichTextString("Fecha fin"));
        fechaFin.createCell(1).setCellValue(resumenFechaFin);
        fechaFin.getCell(1).setCellStyle(dateCellStyle);
        fila++;

        Row resumenTotalEgresos = sheets.get(monthCursor).createRow(fila);
        resumenTotalEgresos.createCell(0).setCellValue(new HSSFRichTextString("Total egresos"));
        resumenTotalEgresos.createCell(1).setCellValue(resumenTotalEgreso);
        HSSFRegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress1, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress1, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress1, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress1, sheets.get(monthCursor), workbook);
        resumenTotalEgresos.getCell(1).setCellStyle(style7);
        fila++;

        Row resumenTotalIngresos = sheets.get(monthCursor).createRow(fila);
        resumenTotalIngresos.createCell(0).setCellValue(new HSSFRichTextString("Total ingresos"));
        resumenTotalIngresos.createCell(1).setCellValue(resumenTotalIngreso);
        HSSFRegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress2, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress2, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress2, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress2, sheets.get(monthCursor), workbook);
        resumenTotalIngresos.getCell(1).setCellStyle(style7);
        fila++;

        Row resumenTotalDepo = sheets.get(monthCursor).createRow(fila);
        resumenTotalDepo.createCell(0).setCellValue(new HSSFRichTextString("Total depositado"));
        resumenTotalDepo.createCell(1).setCellValue(resumenTotalDepositado);
        HSSFRegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress3, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress3, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress3, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress3, sheets.get(monthCursor), workbook);
        resumenTotalDepo.getCell(1).setCellStyle(style7);
        fila++;

        sheets.get(monthCursor).autoSizeColumn(0);
        sheets.get(monthCursor).autoSizeColumn(1);

        //PREPARAR DOCUMENTO
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
