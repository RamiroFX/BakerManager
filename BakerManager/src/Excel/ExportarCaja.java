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
import Entities.Caja;
import Entities.CierreCaja;
import Entities.E_facturaCabecera;
import Entities.E_tipoOperacion;
import Entities.Estado;
import Entities.M_egreso_cabecera;
import Entities.M_funcionario;
import Entities.Moneda;
import Interface.MovimientosCaja;
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
    private CellStyle style1, style2, style3, style4, style5, style6, style7,
            style8, style9BorderBoldItalic, style10, style11;
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

        HSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setItalic(false);
        style8 = workbook.createCellStyle();
        style8.setFont(font);

        style9BorderBoldItalic = workbook.createCellStyle();
        style9BorderBoldItalic.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style9BorderBoldItalic.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style9BorderBoldItalic.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style9BorderBoldItalic.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style9BorderBoldItalic.setFont(font);

        style10 = workbook.createCellStyle();
        style10.setFont(font);
        style10.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        style10.setFillPattern(CellStyle.SOLID_FOREGROUND);

        style11 = workbook.createCellStyle();
        style11.setFont(font);
        style11.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        style11.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style11.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style11.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style11.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style11.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style11.setDataFormat(format.getFormat("#,##0"));
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
            int ingresoContado = DB_Ingreso.obtenerTotalIngreso(ini, fi, 1, Estado.ACTIVO);
            int ingresoCretdito = DB_Ingreso.obtenerTotalIngreso(ini, fi, 2, Estado.ACTIVO);
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
            cabecera.createCell(3).setCellValue(new HSSFRichTextString("Total depositado"));
            cabecera.getCell(0).setCellStyle(style8);
            cabecera.getCell(1).setCellStyle(style8);
            cabecera.getCell(2).setCellStyle(style8);
            cabecera.getCell(3).setCellStyle(style8);
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
                cabecera.getCell(0).setCellStyle(style8);
                cabecera.getCell(1).setCellStyle(style8);
                cabecera.getCell(2).setCellStyle(style8);
                cabecera.getCell(3).setCellStyle(style8);
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
            int ingresoContado = DB_Ingreso.obtenerTotalIngreso(ini, fi, 1, Estado.ACTIVO);
            int ingresoCretdito = DB_Ingreso.obtenerTotalIngreso(ini, fi, 2, Estado.ACTIVO);
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
            resumenTotalDepositado = resumenTotalDepositado + totalDepositado;
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

    public void exportarCajaMovimientos(Caja caja, MovimientosCaja movimientosCaja) {
        //PREPARAR CONTENIDO
        int fila = 0;//En preparar cuerpo empieza en cero (0).
        Calendar calendar = Calendar.getInstance();
        int monthCursor = 0;
        int columnaDerecha = 5;
        Date resumenFechaInicio = null;
        Date resumenFechaFin = null;
        sheets.clear();
        sheets.add(workbook.createSheet(sdfs.format(calendar.getTime())));
        sheets.get(monthCursor).getPrintSetup().setLandscape(true);
        //CREAR FECHA DE CAJA
        Row rowTitulo = sheets.get(monthCursor).createRow(fila);
        rowTitulo.createCell(0).setCellValue(new HSSFRichTextString("Planilla de movimiento diario"));
        rowTitulo.getCell(0).setCellStyle(style8);
        sheets.get(monthCursor).addMergedRegion(new CellRangeAddress(fila, fila, 0, 3));
        fila++;
        Row rowFecha = sheets.get(monthCursor).createRow(fila);
        rowFecha.createCell(0).setCellValue(new HSSFRichTextString("Fecha"));
        rowFecha.getCell(0).setCellStyle(style8);
        rowFecha.createCell(1).setCellValue(caja.getTiempoApertura());
        rowFecha.getCell(1).setCellStyle(dateCellStyle);
        fila++;
        /*
        MOVIMIENTO DE VENTAS CONTADO
         */
        int ventaContado = 0;
        Row rowVentaContadoTitulo = sheets.get(monthCursor).createRow(fila);
        rowVentaContadoTitulo.createCell(0).setCellValue(new HSSFRichTextString("Venta contado"));
        rowVentaContadoTitulo.getCell(0).setCellStyle(style8);
        fila++;
        Row rowVentaContadoCabecera = sheets.get(monthCursor).createRow(fila);
        rowVentaContadoCabecera.createCell(0).setCellValue(new HSSFRichTextString("ID venta"));
        rowVentaContadoCabecera.getCell(0).setCellStyle(style8);
        rowVentaContadoCabecera.createCell(1).setCellValue(new HSSFRichTextString("Nro. Factura"));
        rowVentaContadoCabecera.getCell(1).setCellStyle(style8);
        rowVentaContadoCabecera.createCell(2).setCellValue(new HSSFRichTextString("Cliente"));
        rowVentaContadoCabecera.getCell(2).setCellStyle(style8);
        rowVentaContadoCabecera.createCell(3).setCellValue(new HSSFRichTextString("Importe"));
        rowVentaContadoCabecera.getCell(3).setCellStyle(style8);
        fila++;
        for (E_facturaCabecera movimientoVenta : movimientosCaja.getMovimientoVentas()) {
            if (movimientoVenta.getTipoOperacion().getId() == E_tipoOperacion.CONTADO) {
                ventaContado = ventaContado + movimientoVenta.getTotal();
                //limite de lineas por hoja
                if (fila > 33) {
                    Row rowVentaContadoDetalle = sheets.get(monthCursor).createRow(fila);
                    fila++;
                    rowVentaContadoDetalle.createCell(0 + columnaDerecha).setCellValue(movimientoVenta.getIdFacturaCabecera());
                    rowVentaContadoDetalle.getCell(0 + columnaDerecha).setCellStyle(style4);
                    rowVentaContadoDetalle.createCell(1 + columnaDerecha).setCellValue(movimientoVenta.getNroFactura());
                    rowVentaContadoDetalle.getCell(1 + columnaDerecha).setCellStyle(style4);
                    rowVentaContadoDetalle.createCell(2 + columnaDerecha).setCellValue(new HSSFRichTextString(movimientoVenta.getCliente().getEntidad()));
                    rowVentaContadoDetalle.createCell(3 + columnaDerecha).setCellValue(movimientoVenta.getTotal());
                    rowVentaContadoDetalle.getCell(3 + columnaDerecha).setCellStyle(style4);
                } else {
                    Row rowVentaContadoDetalle = sheets.get(monthCursor).createRow(fila);
                    fila++;
                    rowVentaContadoDetalle.createCell(0).setCellValue(movimientoVenta.getIdFacturaCabecera());
                    rowVentaContadoDetalle.getCell(0).setCellStyle(style4);
                    rowVentaContadoDetalle.createCell(1).setCellValue(movimientoVenta.getNroFactura());
                    rowVentaContadoDetalle.getCell(1).setCellStyle(style4);
                    rowVentaContadoDetalle.createCell(2).setCellValue(new HSSFRichTextString(movimientoVenta.getCliente().getEntidad()));
                    rowVentaContadoDetalle.createCell(3).setCellValue(movimientoVenta.getTotal());
                    rowVentaContadoDetalle.getCell(3).setCellStyle(style4);
                }
            }
        }
        rowVentaContadoTitulo.createCell(1).setCellValue(ventaContado);
        rowVentaContadoTitulo.getCell(1).setCellStyle(style4);
        fila++;
        /*
        MOVIMIENTO DE VENTAS CREDITO
         */
        int ventaCredito = 0;
        Row rowVentaCreditoTitulo = sheets.get(monthCursor).createRow(fila);
        rowVentaCreditoTitulo.createCell(0).setCellValue(new HSSFRichTextString("Venta credito"));
        rowVentaCreditoTitulo.getCell(0).setCellStyle(style8);
        fila++;
        Row rowVentaCreditoCabecera = sheets.get(monthCursor).createRow(fila);
        rowVentaCreditoCabecera.createCell(0).setCellValue(new HSSFRichTextString("ID venta"));
        rowVentaCreditoCabecera.getCell(0).setCellStyle(style8);
        rowVentaCreditoCabecera.createCell(1).setCellValue(new HSSFRichTextString("Nro. Factura"));
        rowVentaCreditoCabecera.getCell(1).setCellStyle(style8);
        rowVentaCreditoCabecera.createCell(2).setCellValue(new HSSFRichTextString("Cliente"));
        rowVentaCreditoCabecera.getCell(2).setCellStyle(style8);
        rowVentaCreditoCabecera.createCell(3).setCellValue(new HSSFRichTextString("Importe"));
        rowVentaCreditoCabecera.getCell(3).setCellStyle(style8);
        fila++;
        for (E_facturaCabecera movimientoVenta : movimientosCaja.getMovimientoVentas()) {
            if (movimientoVenta.getTipoOperacion().getId() == E_tipoOperacion.CREDITO_30) {
                ventaCredito = ventaCredito + movimientoVenta.getTotal();
                //limite de lineas por hoja
                if (fila > 33) {
                    Row rowVentaCreditoDetalle = sheets.get(monthCursor).createRow(fila);
                    fila++;
                    rowVentaCreditoDetalle.createCell(0 + columnaDerecha).setCellValue(movimientoVenta.getIdFacturaCabecera());
                    rowVentaCreditoDetalle.getCell(0 + columnaDerecha).setCellStyle(style4);
                    rowVentaCreditoDetalle.createCell(1 + columnaDerecha).setCellValue(movimientoVenta.getNroFactura());
                    rowVentaCreditoDetalle.getCell(1 + columnaDerecha).setCellStyle(style4);
                    rowVentaCreditoDetalle.createCell(2 + columnaDerecha).setCellValue(new HSSFRichTextString(movimientoVenta.getCliente().getEntidad()));
                    rowVentaCreditoDetalle.createCell(3 + columnaDerecha).setCellValue(movimientoVenta.getTotal());
                    rowVentaCreditoDetalle.getCell(3 + columnaDerecha).setCellStyle(style4);
                } else {
                    Row rowVentaCreditoDetalle = sheets.get(monthCursor).createRow(fila);
                    fila++;
                    rowVentaCreditoDetalle.createCell(0).setCellValue(movimientoVenta.getIdFacturaCabecera());
                    rowVentaCreditoDetalle.getCell(0).setCellStyle(style4);
                    rowVentaCreditoDetalle.createCell(1).setCellValue(movimientoVenta.getNroFactura());
                    rowVentaCreditoDetalle.getCell(1).setCellStyle(style4);
                    rowVentaCreditoDetalle.createCell(2).setCellValue(new HSSFRichTextString(movimientoVenta.getCliente().getEntidad()));
                    rowVentaCreditoDetalle.createCell(3).setCellValue(movimientoVenta.getTotal());
                    rowVentaCreditoDetalle.getCell(3).setCellStyle(style4);
                }
            }
        }
        rowVentaCreditoTitulo.createCell(1).setCellValue(ventaCredito);
        rowVentaCreditoTitulo.getCell(1).setCellStyle(style4);
        fila++;

        /*
        MOVIMIENTO DE COMPRAS CONTADO
         */
        int compraContado = 0;
        Row rowCompraContadoTitulo = sheets.get(monthCursor).createRow(fila);
        rowCompraContadoTitulo.createCell(0).setCellValue(new HSSFRichTextString("Compra contado"));
        rowCompraContadoTitulo.getCell(0).setCellStyle(style8);
        fila++;
        Row rowCompraContadoCabecera = sheets.get(monthCursor).createRow(fila);
        rowCompraContadoCabecera.createCell(0).setCellValue(new HSSFRichTextString("ID compra"));
        rowCompraContadoCabecera.getCell(0).setCellStyle(style8);
        rowCompraContadoCabecera.createCell(1).setCellValue(new HSSFRichTextString("Nro. Factura"));
        rowCompraContadoCabecera.getCell(1).setCellStyle(style8);
        rowCompraContadoCabecera.createCell(2).setCellValue(new HSSFRichTextString("Proveedor"));
        rowCompraContadoCabecera.getCell(2).setCellStyle(style8);
        rowCompraContadoCabecera.createCell(3).setCellValue(new HSSFRichTextString("Importe"));
        rowCompraContadoCabecera.getCell(3).setCellStyle(style8);
        fila++;
        for (M_egreso_cabecera movimientoCompra : movimientosCaja.getMovimientoCompras()) {
            if (movimientoCompra.getId_condVenta() == E_tipoOperacion.CONTADO) {
                compraContado = compraContado + movimientoCompra.getTotal();
                //limite de lineas por hoja
                if (fila > 33) {
                    Row rowCompraContadoDetalle = sheets.get(monthCursor).createRow(fila);
                    fila++;
                    rowCompraContadoDetalle.createCell(0 + columnaDerecha).setCellValue(movimientoCompra.getId_cabecera());
                    rowCompraContadoDetalle.getCell(0 + columnaDerecha).setCellStyle(style4);
                    rowCompraContadoDetalle.createCell(1 + columnaDerecha).setCellValue(movimientoCompra.getNro_factura());
                    rowCompraContadoDetalle.getCell(1 + columnaDerecha).setCellStyle(style4);
                    rowCompraContadoDetalle.createCell(2 + columnaDerecha).setCellValue(new HSSFRichTextString(movimientoCompra.getProveedor().getEntidad()));
                    rowCompraContadoDetalle.createCell(3 + columnaDerecha).setCellValue(movimientoCompra.getTotal());
                    rowCompraContadoDetalle.getCell(3 + columnaDerecha).setCellStyle(style4);
                } else {
                    Row rowCompraContadoDetalle = sheets.get(monthCursor).createRow(fila);
                    fila++;
                    rowCompraContadoDetalle.createCell(0).setCellValue(movimientoCompra.getId_cabecera());
                    rowCompraContadoDetalle.getCell(0).setCellStyle(style4);
                    rowCompraContadoDetalle.createCell(1).setCellValue(movimientoCompra.getNro_factura());
                    rowCompraContadoDetalle.getCell(1).setCellStyle(style4);
                    rowCompraContadoDetalle.createCell(2).setCellValue(new HSSFRichTextString(movimientoCompra.getProveedor().getEntidad()));
                    rowCompraContadoDetalle.createCell(3).setCellValue(movimientoCompra.getTotal());
                    rowCompraContadoDetalle.getCell(3).setCellStyle(style4);
                }
            }
        }
        rowCompraContadoTitulo.createCell(1).setCellValue(compraContado);
        rowCompraContadoTitulo.getCell(1).setCellStyle(style4);
        fila++;

        /*
        MOVIMIENTO DE COMPRAS EFECTIVO
         */
        int compraCredito = 0;
        Row rowCompraCreditoTitulo = sheets.get(monthCursor).createRow(fila);
        rowCompraCreditoTitulo.createCell(0).setCellValue(new HSSFRichTextString("Compra credito"));
        rowCompraCreditoTitulo.getCell(0).setCellStyle(style8);
        fila++;
        Row rowCompraCreditoCabecera = sheets.get(monthCursor).createRow(fila);
        rowCompraCreditoCabecera.createCell(0).setCellValue(new HSSFRichTextString("ID compra"));
        rowCompraCreditoCabecera.getCell(0).setCellStyle(style8);
        rowCompraCreditoCabecera.createCell(1).setCellValue(new HSSFRichTextString("Nro. Factura"));
        rowCompraCreditoCabecera.getCell(1).setCellStyle(style8);
        rowCompraCreditoCabecera.createCell(2).setCellValue(new HSSFRichTextString("Proveedor"));
        rowCompraCreditoCabecera.getCell(2).setCellStyle(style8);
        rowCompraCreditoCabecera.createCell(3).setCellValue(new HSSFRichTextString("Importe"));
        rowCompraCreditoCabecera.getCell(3).setCellStyle(style8);
        fila++;
        for (M_egreso_cabecera movimientoCompra : movimientosCaja.getMovimientoCompras()) {
            if (movimientoCompra.getId_condVenta() == E_tipoOperacion.CREDITO_30) {
                compraCredito = compraCredito + movimientoCompra.getTotal();
                //limite de lineas por hoja
                if (fila > 33) {
                    Row rowCompraCreditoDetalle = sheets.get(monthCursor).createRow(fila);
                    fila++;
                    rowCompraCreditoDetalle.createCell(0 + columnaDerecha).setCellValue(movimientoCompra.getId_cabecera());
                    rowCompraCreditoDetalle.getCell(0 + columnaDerecha).setCellStyle(style4);
                    rowCompraCreditoDetalle.createCell(1 + columnaDerecha).setCellValue(movimientoCompra.getNro_factura());
                    rowCompraCreditoDetalle.getCell(1 + columnaDerecha).setCellStyle(style4);
                    rowCompraCreditoDetalle.createCell(2 + columnaDerecha).setCellValue(new HSSFRichTextString(movimientoCompra.getProveedor().getEntidad()));
                    rowCompraCreditoDetalle.createCell(3 + columnaDerecha).setCellValue(movimientoCompra.getTotal());
                    rowCompraCreditoDetalle.getCell(3 + columnaDerecha).setCellStyle(style4);
                } else {
                    Row rowCompraCreditoDetalle = sheets.get(monthCursor).createRow(fila);
                    fila++;
                    rowCompraCreditoDetalle.createCell(0).setCellValue(movimientoCompra.getId_cabecera());
                    rowCompraCreditoDetalle.getCell(0).setCellStyle(style4);
                    rowCompraCreditoDetalle.createCell(1).setCellValue(movimientoCompra.getNro_factura());
                    rowCompraCreditoDetalle.getCell(1).setCellStyle(style4);
                    rowCompraCreditoDetalle.createCell(2).setCellValue(new HSSFRichTextString(movimientoCompra.getProveedor().getEntidad()));
                    rowCompraCreditoDetalle.createCell(3).setCellValue(movimientoCompra.getTotal());
                    rowCompraCreditoDetalle.getCell(3).setCellStyle(style4);
                }
            }
        }
        rowCompraCreditoTitulo.createCell(1).setCellValue(compraCredito);
        rowCompraCreditoTitulo.getCell(1).setCellStyle(style4);

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
        sheets.get(monthCursor).autoSizeColumn(0);
        sheets.get(monthCursor).autoSizeColumn(1);
        sheets.get(monthCursor).autoSizeColumn(2);
        sheets.get(monthCursor).autoSizeColumn(3);
        sheets.get(monthCursor).autoSizeColumn(4);
        sheets.get(monthCursor).autoSizeColumn(5);
        sheets.get(monthCursor).autoSizeColumn(6);

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

    public void exportarInformeCierreCaja(Caja caja, MovimientosCaja movimientosCaja) {
        System.out.println("Excel.ExportarCaja.exportarInformeCierreCaja()");
        //PREPARAR CONTENIDO
        int fila = 0;//En preparar cuerpo empieza en cero (0).
        Calendar calendar = Calendar.getInstance();
        int monthCursor = 0;
        int columnaDerecha = 5;
        Date resumenFechaInicio = null;
        Date resumenFechaFin = null;
        sheets.clear();
        sheets.add(workbook.createSheet(sdfs.format(calendar.getTime())));
        sheets.get(monthCursor).getPrintSetup().setLandscape(true);
        //CREAR FECHA DE CAJA
        Row rowTitulo = sheets.get(monthCursor).createRow(fila);
        rowTitulo.createCell(0).setCellValue(new HSSFRichTextString("Informe Cierre de Caja"));
        rowTitulo.getCell(0).setCellStyle(style8);
        CellUtil.setAlignment(rowTitulo.getCell(0), workbook, CellStyle.ALIGN_CENTER);
        sheets.get(monthCursor).addMergedRegion(new CellRangeAddress(fila, fila, 0, 12));
        fila++;
        Row rowIDYFecha = sheets.get(monthCursor).createRow(fila);
        rowIDYFecha.createCell(0).setCellValue(new HSSFRichTextString("N° Informe"));
        rowIDYFecha.getCell(0).setCellStyle(style8);
        rowIDYFecha.createCell(1).setCellValue(caja.getIdCaja());
        rowIDYFecha.getCell(1).setCellStyle(style4);
        rowIDYFecha.createCell(3).setCellValue(new HSSFRichTextString("Fecha informe"));
        rowIDYFecha.getCell(3).setCellStyle(style8);
        rowIDYFecha.createCell(4).setCellValue(caja.getTiempoCierre());
        rowIDYFecha.getCell(4).setCellStyle(dateCellStyle);
        fila++;
        CellRangeAddress conceptosCRA = new CellRangeAddress(fila, fila + 1, 0, 0);
        CellRangeAddress nroDocCRA = new CellRangeAddress(fila, fila + 1, 1, 1);
        CellRangeAddress fechaCRA = new CellRangeAddress(fila, fila + 1, 2, 2);
        CellRangeAddress rucCRA = new CellRangeAddress(fila, fila + 1, 3, 3);
        CellRangeAddress entidadCRA = new CellRangeAddress(fila, fila + 1, 4, 4);
        CellRangeAddress referenciaCRA = new CellRangeAddress(fila, fila, 5, 8);
        CellRangeAddress efectivoCRA = new CellRangeAddress(fila, fila, 9, 10);
        CellRangeAddress documentosCRA = new CellRangeAddress(fila, fila, 11, 12);
        sheets.get(monthCursor).addMergedRegion(conceptosCRA);
        sheets.get(monthCursor).addMergedRegion(nroDocCRA);
        sheets.get(monthCursor).addMergedRegion(fechaCRA);
        sheets.get(monthCursor).addMergedRegion(rucCRA);
        sheets.get(monthCursor).addMergedRegion(entidadCRA);
        sheets.get(monthCursor).addMergedRegion(referenciaCRA);
        sheets.get(monthCursor).addMergedRegion(efectivoCRA);
        sheets.get(monthCursor).addMergedRegion(documentosCRA);
        int rowCabeceraDetalleIndex = 0;
        Row rowCabeceraDetalle = sheets.get(monthCursor).createRow(fila);
        rowCabeceraDetalle.createCell(rowCabeceraDetalleIndex).setCellValue(new HSSFRichTextString("Conceptos"));
        rowCabeceraDetalle.getCell(rowCabeceraDetalleIndex).setCellStyle(style8);
        CellUtil.setAlignment(rowCabeceraDetalle.getCell(rowCabeceraDetalleIndex), workbook, CellStyle.ALIGN_CENTER);
        rowCabeceraDetalleIndex++;
        rowCabeceraDetalle.createCell(rowCabeceraDetalleIndex).setCellValue(new HSSFRichTextString("Nº Doc."));
        rowCabeceraDetalle.getCell(rowCabeceraDetalleIndex).setCellStyle(style8);
        CellUtil.setAlignment(rowCabeceraDetalle.getCell(rowCabeceraDetalleIndex), workbook, CellStyle.ALIGN_CENTER);
        rowCabeceraDetalleIndex++;
        rowCabeceraDetalle.createCell(rowCabeceraDetalleIndex).setCellValue(new HSSFRichTextString("Fecha"));
        rowCabeceraDetalle.getCell(rowCabeceraDetalleIndex).setCellStyle(style8);
        CellUtil.setAlignment(rowCabeceraDetalle.getCell(rowCabeceraDetalleIndex), workbook, CellStyle.ALIGN_CENTER);
        rowCabeceraDetalleIndex++;
        rowCabeceraDetalle.createCell(rowCabeceraDetalleIndex).setCellValue(new HSSFRichTextString("R.U.C."));
        rowCabeceraDetalle.getCell(rowCabeceraDetalleIndex).setCellStyle(style8);
        CellUtil.setAlignment(rowCabeceraDetalle.getCell(rowCabeceraDetalleIndex), workbook, CellStyle.ALIGN_CENTER);
        rowCabeceraDetalleIndex++;
        rowCabeceraDetalle.createCell(rowCabeceraDetalleIndex).setCellValue(new HSSFRichTextString("Entidad"));
        rowCabeceraDetalle.getCell(rowCabeceraDetalleIndex).setCellStyle(style8);
        CellUtil.setAlignment(rowCabeceraDetalle.getCell(rowCabeceraDetalleIndex), workbook, CellStyle.ALIGN_CENTER);
        rowCabeceraDetalleIndex++;
        rowCabeceraDetalle.createCell(rowCabeceraDetalleIndex).setCellValue(new HSSFRichTextString("Referencia"));
        rowCabeceraDetalle.getCell(rowCabeceraDetalleIndex).setCellStyle(style8);
        CellUtil.setAlignment(rowCabeceraDetalle.getCell(rowCabeceraDetalleIndex), workbook, CellStyle.ALIGN_CENTER);
        rowCabeceraDetalleIndex = rowCabeceraDetalleIndex + 4;
        rowCabeceraDetalle.createCell(rowCabeceraDetalleIndex).setCellValue(new HSSFRichTextString("Efectivo"));
        rowCabeceraDetalle.getCell(rowCabeceraDetalleIndex).setCellStyle(style8);
        CellUtil.setAlignment(rowCabeceraDetalle.getCell(rowCabeceraDetalleIndex), workbook, CellStyle.ALIGN_CENTER);
        rowCabeceraDetalleIndex = rowCabeceraDetalleIndex + 2;
        rowCabeceraDetalle.createCell(rowCabeceraDetalleIndex).setCellValue(new HSSFRichTextString("Documentos"));
        rowCabeceraDetalle.getCell(rowCabeceraDetalleIndex).setCellStyle(style8);
        CellUtil.setAlignment(rowCabeceraDetalle.getCell(rowCabeceraDetalleIndex), workbook, CellStyle.ALIGN_CENTER);
        rowCabeceraDetalleIndex = 5;
        fila++;
        Row rowCabeceraSubDetalle = sheets.get(monthCursor).createRow(fila);
        rowCabeceraSubDetalle.createCell(rowCabeceraDetalleIndex).setCellValue(new HSSFRichTextString("Fecha"));
        rowCabeceraSubDetalle.getCell(rowCabeceraDetalleIndex).setCellStyle(style9BorderBoldItalic);
        rowCabeceraDetalleIndex++;
        rowCabeceraSubDetalle.createCell(rowCabeceraDetalleIndex).setCellValue(new HSSFRichTextString("F. Dif."));
        rowCabeceraSubDetalle.getCell(rowCabeceraDetalleIndex).setCellStyle(style9BorderBoldItalic);
        rowCabeceraDetalleIndex++;
        rowCabeceraSubDetalle.createCell(rowCabeceraDetalleIndex).setCellValue(new HSSFRichTextString("Número"));
        rowCabeceraSubDetalle.getCell(rowCabeceraDetalleIndex).setCellStyle(style9BorderBoldItalic);
        rowCabeceraDetalleIndex++;
        rowCabeceraSubDetalle.createCell(rowCabeceraDetalleIndex).setCellValue(new HSSFRichTextString("Entidad"));
        rowCabeceraSubDetalle.getCell(rowCabeceraDetalleIndex).setCellStyle(style9BorderBoldItalic);
        rowCabeceraDetalleIndex++;
        rowCabeceraSubDetalle.createCell(rowCabeceraDetalleIndex).setCellValue(new HSSFRichTextString("Ingresos"));
        rowCabeceraSubDetalle.getCell(rowCabeceraDetalleIndex).setCellStyle(style9BorderBoldItalic);
        rowCabeceraDetalleIndex++;
        rowCabeceraSubDetalle.createCell(rowCabeceraDetalleIndex).setCellValue(new HSSFRichTextString("Egresos"));
        rowCabeceraSubDetalle.getCell(rowCabeceraDetalleIndex).setCellStyle(style9BorderBoldItalic);
        rowCabeceraDetalleIndex++;
        rowCabeceraSubDetalle.createCell(rowCabeceraDetalleIndex).setCellValue(new HSSFRichTextString("Ingresos"));
        rowCabeceraSubDetalle.getCell(rowCabeceraDetalleIndex).setCellStyle(style9BorderBoldItalic);
        rowCabeceraDetalleIndex++;
        rowCabeceraSubDetalle.createCell(rowCabeceraDetalleIndex).setCellValue(new HSSFRichTextString("Egresos"));
        rowCabeceraSubDetalle.getCell(rowCabeceraDetalleIndex).setCellStyle(style9BorderBoldItalic);
        rowCabeceraDetalleIndex++;
        fila++;
        /*
        BORDES PARA LAS REGIONES COMBINADAS
         */
        HSSFRegionUtil.setBorderTop(CellStyle.BORDER_THIN, conceptosCRA, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderLeft(CellStyle.BORDER_THIN, conceptosCRA, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderRight(CellStyle.BORDER_THIN, conceptosCRA, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderBottom(CellStyle.BORDER_THIN, conceptosCRA, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderTop(CellStyle.BORDER_THIN, nroDocCRA, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderLeft(CellStyle.BORDER_THIN, nroDocCRA, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderRight(CellStyle.BORDER_THIN, nroDocCRA, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderBottom(CellStyle.BORDER_THIN, nroDocCRA, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderTop(CellStyle.BORDER_THIN, fechaCRA, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderLeft(CellStyle.BORDER_THIN, fechaCRA, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderRight(CellStyle.BORDER_THIN, fechaCRA, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderBottom(CellStyle.BORDER_THIN, fechaCRA, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderTop(CellStyle.BORDER_THIN, rucCRA, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderLeft(CellStyle.BORDER_THIN, rucCRA, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderRight(CellStyle.BORDER_THIN, rucCRA, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderBottom(CellStyle.BORDER_THIN, rucCRA, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderTop(CellStyle.BORDER_THIN, entidadCRA, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderLeft(CellStyle.BORDER_THIN, entidadCRA, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderRight(CellStyle.BORDER_THIN, entidadCRA, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderBottom(CellStyle.BORDER_THIN, entidadCRA, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderTop(CellStyle.BORDER_THIN, referenciaCRA, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderLeft(CellStyle.BORDER_THIN, referenciaCRA, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderRight(CellStyle.BORDER_THIN, referenciaCRA, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderBottom(CellStyle.BORDER_THIN, referenciaCRA, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderTop(CellStyle.BORDER_THIN, efectivoCRA, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderLeft(CellStyle.BORDER_THIN, efectivoCRA, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderRight(CellStyle.BORDER_THIN, efectivoCRA, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderBottom(CellStyle.BORDER_THIN, efectivoCRA, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderTop(CellStyle.BORDER_THIN, documentosCRA, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderLeft(CellStyle.BORDER_THIN, documentosCRA, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderRight(CellStyle.BORDER_THIN, documentosCRA, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderBottom(CellStyle.BORDER_THIN, documentosCRA, sheets.get(monthCursor), workbook);

        /*
        INICIO FONDO FIJO
         */
        Row rowFondoFijoDetalle = sheets.get(monthCursor).createRow(fila);
        rowFondoFijoDetalle.createCell(0).setCellValue(new HSSFRichTextString("1 - Fondo Fijo"));
        rowFondoFijoDetalle.getCell(0).setCellStyle(style8);
        rowFondoFijoDetalle.createCell(2).setCellValue(caja.getTiempoCierre());
        rowFondoFijoDetalle.getCell(2).setCellStyle(dateCellStyle);
        rowFondoFijoDetalle.createCell(4).setCellValue(new HSSFRichTextString("Fondo Fijo"));
        rowFondoFijoDetalle.getCell(4).setCellStyle(style8);
        fila++;
        CellRangeAddress fondoFijoResumenCRA = new CellRangeAddress(fila, fila, 0, 8);
        sheets.get(monthCursor).addMergedRegion(fondoFijoResumenCRA);
        Row rowFondoFijoResumen = sheets.get(monthCursor).createRow(fila);
        rowFondoFijoResumen.createCell(0).setCellValue(new HSSFRichTextString("Total 1 - Fondo Fijo"));
        rowFondoFijoResumen.getCell(0).setCellStyle(style10);
        //JUSTIFICAR A LA DERECHA
        CellUtil.setAlignment(rowFondoFijoResumen.getCell(0), workbook, CellStyle.ALIGN_RIGHT);
        rowFondoFijoResumen.createCell(9).setCellValue(caja.getMontoCierre());
        rowFondoFijoResumen.getCell(9).setCellStyle(style11);
        rowFondoFijoResumen.createCell(10).setCellValue(0);
        rowFondoFijoResumen.getCell(10).setCellStyle(style11);
        rowFondoFijoResumen.createCell(11).setCellValue(0);
        rowFondoFijoResumen.getCell(11).setCellStyle(style11);
        rowFondoFijoResumen.createCell(12).setCellValue(0);
        rowFondoFijoResumen.getCell(12).setCellStyle(style11);
        //COLOCAR BORDES
        HSSFRegionUtil.setBorderTop(CellStyle.BORDER_THIN, fondoFijoResumenCRA, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderLeft(CellStyle.BORDER_THIN, fondoFijoResumenCRA, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderRight(CellStyle.BORDER_THIN, fondoFijoResumenCRA, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderBottom(CellStyle.BORDER_THIN, fondoFijoResumenCRA, sheets.get(monthCursor), workbook);
        fila++;
        /*
        FIN FONDO FIJO
         */

 /*
        INICIO VENTA CONTADO
         */
        boolean bmv = true;
        int totalVentaContado = 0;
        for (E_facturaCabecera movimientoVenta : movimientosCaja.getMovimientoVentas()) {
            if (movimientoVenta.getTipoOperacion().getId() == E_tipoOperacion.CONTADO) {
                totalVentaContado = totalVentaContado + movimientoVenta.getTotal();
                Row rowVentaContadoDetalle = sheets.get(monthCursor).createRow(fila);
                if (bmv) {
                    //solo se imprime una vez el sub titulo del documento
                    rowVentaContadoDetalle.createCell(0).setCellValue(new HSSFRichTextString("2 - Venta contado"));
                    rowVentaContadoDetalle.getCell(0).setCellStyle(style8);
                    bmv = false;
                }
                rowFondoFijoDetalle.createCell(1).setCellValue(movimientoVenta.getNroFactura());
                rowFondoFijoDetalle.getCell(1).setCellStyle(style4);
                rowFondoFijoDetalle.createCell(2).setCellValue(movimientoVenta.getTiempo());
                rowFondoFijoDetalle.getCell(2).setCellStyle(dateCellStyle);
                rowFondoFijoDetalle.createCell(2).setCellValue(new HSSFRichTextString(movimientoVenta.getCliente().getEntidad()));
                rowFondoFijoDetalle.createCell(7).setCellValue(movimientoVenta.getTotal());
                rowFondoFijoDetalle.getCell(7).setCellStyle(style4);
                fila++;
            }
        }
        fila++;
        CellRangeAddress fondoVentaContadoResumenCRA = new CellRangeAddress(fila, fila, 0, 8);
        sheets.get(monthCursor).addMergedRegion(fondoVentaContadoResumenCRA);
        Row rowVentaContadoResumen = sheets.get(monthCursor).createRow(fila);
        rowVentaContadoResumen.createCell(0).setCellValue(new HSSFRichTextString("Total 2 - Venta Contado"));
        rowVentaContadoResumen.getCell(0).setCellStyle(style10);
        //JUSTIFICAR A LA DERECHA
        CellUtil.setAlignment(rowVentaContadoResumen.getCell(0), workbook, CellStyle.ALIGN_RIGHT);
        rowVentaContadoResumen.createCell(9).setCellValue(totalVentaContado);
        rowVentaContadoResumen.getCell(9).setCellStyle(style11);
        rowVentaContadoResumen.createCell(10).setCellValue(0);
        rowVentaContadoResumen.getCell(10).setCellStyle(style11);
        rowVentaContadoResumen.createCell(11).setCellValue(0);
        rowVentaContadoResumen.getCell(11).setCellStyle(style11);
        rowVentaContadoResumen.createCell(12).setCellValue(0);
        rowVentaContadoResumen.getCell(12).setCellStyle(style11);
        //COLOCAR BORDES
        HSSFRegionUtil.setBorderTop(CellStyle.BORDER_THIN, fondoVentaContadoResumenCRA, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderLeft(CellStyle.BORDER_THIN, fondoVentaContadoResumenCRA, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderRight(CellStyle.BORDER_THIN, fondoVentaContadoResumenCRA, sheets.get(monthCursor), workbook);
        HSSFRegionUtil.setBorderBottom(CellStyle.BORDER_THIN, fondoVentaContadoResumenCRA, sheets.get(monthCursor), workbook);

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
        sheets.get(monthCursor).autoSizeColumn(0);
        sheets.get(monthCursor).autoSizeColumn(1);
        sheets.get(monthCursor).autoSizeColumn(2);
        sheets.get(monthCursor).autoSizeColumn(3);
        sheets.get(monthCursor).autoSizeColumn(4);
        sheets.get(monthCursor).autoSizeColumn(5);
        sheets.get(monthCursor).autoSizeColumn(6);
        sheets.get(monthCursor).autoSizeColumn(7);
        sheets.get(monthCursor).autoSizeColumn(8);
        sheets.get(monthCursor).autoSizeColumn(9);
        sheets.get(monthCursor).autoSizeColumn(10);
        sheets.get(monthCursor).autoSizeColumn(11);
        sheets.get(monthCursor).autoSizeColumn(12);

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
