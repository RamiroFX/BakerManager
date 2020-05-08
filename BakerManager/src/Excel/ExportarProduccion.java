/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Excel;

import DB.DB_Produccion;
import Entities.E_produccionCabecera;
import Entities.E_produccionDetalle;
import Entities.E_produccionFilm;
import Entities.E_produccionTipo;
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
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author Ramiro Ferreira
 */
public class ExportarProduccion {

    String nombreHoja;
    Date fechaInic, fechaFinal;
    ArrayList<E_produccionCabecera> prodCabeceraList;
    ArrayList<E_produccionFilm> produccionFilmCabecera;
    HSSFWorkbook workbook;
    HSSFSheet sheet;
    CellStyle style1, style2, styleNumber1, styleNumber2, style5;
    HSSFCellStyle dateCellStyle;

    public ExportarProduccion(String nombreHoja, Date inicio, Date fin, ArrayList<E_produccionCabecera> prodCabeceraList) {
        this.nombreHoja = nombreHoja;
        this.fechaInic = inicio;
        this.prodCabeceraList = prodCabeceraList;
        this.fechaFinal = fin;
        createWorkBook();
        createCellStyles();
    }

    public ExportarProduccion(String nombreHoja, ArrayList<E_produccionFilm> produccionFilmCabecera) {
        this.nombreHoja = nombreHoja;
        this.produccionFilmCabecera = produccionFilmCabecera;
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
        styleNumber1 = workbook.createCellStyle();
        styleNumber1.setDataFormat(format.getFormat("0.0"));

        styleNumber2 = workbook.createCellStyle();
        styleNumber2.setDataFormat(format.getFormat("#,##0"));

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
        for (int q = 0; q < prodCabeceraList.size(); q++) {
            E_produccionCabecera prodCabecera = prodCabeceraList.get(q);

            //INICIO PRODUCCION CABECERA
            Row produccionCabeceraRowFecha = sheet.createRow(filaActual);
            filaActual++;
            produccionCabeceraRowFecha.createCell(0).setCellValue(new HSSFRichTextString("Fecha"));
            produccionCabeceraRowFecha.createCell(1).setCellValue(prodCabecera.getFechaProduccion());
            produccionCabeceraRowFecha.getCell(0).setCellStyle(style5);
            produccionCabeceraRowFecha.getCell(1).setCellStyle(dateCellStyle);

            Row produccionCabeceraRowResp = sheet.createRow(filaActual);
            filaActual++;
            produccionCabeceraRowResp.createCell(0).setCellValue(new HSSFRichTextString("Responsable"));
            produccionCabeceraRowResp.createCell(1).setCellValue(prodCabecera.getFuncionarioProduccion().getNombre());
            produccionCabeceraRowResp.getCell(0).setCellStyle(style5);

            Row produccionCabeceraRowReg = sheet.createRow(filaActual);
            filaActual++;
            produccionCabeceraRowReg.createCell(0).setCellValue(new HSSFRichTextString("Registrado por"));
            produccionCabeceraRowReg.createCell(1).setCellValue(prodCabecera.getFuncionarioSistema().getNombre());
            produccionCabeceraRowReg.getCell(0).setCellStyle(style5);

            Row produccionCabeceraRowOT = sheet.createRow(filaActual);
            filaActual++;
            produccionCabeceraRowOT.createCell(0).setCellValue(new HSSFRichTextString("Nro Orden trabajo"));
            produccionCabeceraRowOT.createCell(1).setCellValue(prodCabecera.getNroOrdenTrabajo());
            produccionCabeceraRowOT.getCell(0).setCellStyle(style5);
            //FIN PRODUCCION CABECERA

            //INICIO CABECERA PRODUCCION DETALLE
            Row cabeceraProduccionDetalle = sheet.createRow(filaActual);
            filaActual++;
            cabeceraProduccionDetalle.createCell(0).setCellValue(new HSSFRichTextString("Cod."));
            cabeceraProduccionDetalle.getCell(0).setCellStyle(style1);
            cabeceraProduccionDetalle.createCell(1).setCellValue(new HSSFRichTextString("Producto"));
            cabeceraProduccionDetalle.getCell(1).setCellStyle(style1);
            cabeceraProduccionDetalle.createCell(2).setCellValue(new HSSFRichTextString("Cantidad"));
            cabeceraProduccionDetalle.getCell(2).setCellStyle(style1);
            //FIN CABECERA PRODUCCION DETALLE

            //INICIO DETALLE PRODUCCION DETALLE
            ArrayList<E_produccionDetalle> prodDetalleList;
            prodDetalleList = new ArrayList<>(DB_Produccion.consultarProduccionDetalle(prodCabecera.getId()));
            for (int i = 0; i < prodDetalleList.size(); i++) {
                Row filaProdDetalle = sheet.createRow(filaActual);
                filaActual++;
                E_produccionDetalle get = prodDetalleList.get(i);
                filaProdDetalle.createCell(0).setCellValue(get.getProducto().getCodigo());
                filaProdDetalle.createCell(1).setCellValue(get.getProducto().getDescripcion());
                filaProdDetalle.createCell(2).setCellValue(get.getCantidad());
            }
            filaActual++;
        }

        //INICIO AJUSTAR COLUMNAS
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
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

    public void exportacionIndividualBauplst() {
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
        for (int q = 0; q < prodCabeceraList.size(); q++) {
            E_produccionCabecera prodCabecera = prodCabeceraList.get(q);

            //INICIO PRODUCCION CABECERA
            Row produccionCabeceraRowFecha = sheet.createRow(filaActual);
            filaActual++;
            produccionCabeceraRowFecha.createCell(0).setCellValue(new HSSFRichTextString("Fecha"));
            produccionCabeceraRowFecha.createCell(1).setCellValue(prodCabecera.getFechaProduccion());
            produccionCabeceraRowFecha.getCell(0).setCellStyle(style5);
            produccionCabeceraRowFecha.getCell(1).setCellStyle(dateCellStyle);

            Row produccionCabeceraRowResp = sheet.createRow(filaActual);
            filaActual++;
            produccionCabeceraRowResp.createCell(0).setCellValue(new HSSFRichTextString("Responsable"));
            produccionCabeceraRowResp.createCell(1).setCellValue(prodCabecera.getFuncionarioProduccion().getNombre());
            produccionCabeceraRowResp.getCell(0).setCellStyle(style5);

            Row produccionCabeceraRowReg = sheet.createRow(filaActual);
            filaActual++;
            produccionCabeceraRowReg.createCell(0).setCellValue(new HSSFRichTextString("Registrado por"));
            produccionCabeceraRowReg.createCell(1).setCellValue(prodCabecera.getFuncionarioSistema().getNombre());
            produccionCabeceraRowReg.getCell(0).setCellStyle(style5);

            Row produccionCabeceraRowOT = sheet.createRow(filaActual);
            filaActual++;
            produccionCabeceraRowOT.createCell(0).setCellValue(new HSSFRichTextString("Nro Orden trabajo"));
            produccionCabeceraRowOT.createCell(1).setCellValue(prodCabecera.getNroOrdenTrabajo());
            produccionCabeceraRowOT.getCell(0).setCellStyle(style5);
            //FIN PRODUCCION CABECERA

            //INICIO CABECERA PRODUCCION DETALLE
            Row cabeceraProduccionDetalle = sheet.createRow(filaActual);
            filaActual++;
            cabeceraProduccionDetalle.createCell(0).setCellValue(new HSSFRichTextString("Cod."));
            cabeceraProduccionDetalle.getCell(0).setCellStyle(style1);
            cabeceraProduccionDetalle.createCell(1).setCellValue(new HSSFRichTextString("Producto"));
            cabeceraProduccionDetalle.getCell(1).setCellStyle(style1);
            cabeceraProduccionDetalle.createCell(2).setCellValue(new HSSFRichTextString("Cantidad"));
            cabeceraProduccionDetalle.getCell(2).setCellStyle(style1);
            //FIN CABECERA PRODUCCION DETALLE

            //INICIO DETALLE PRODUCCION DETALLE
            ArrayList<E_produccionDetalle> prodDetalleList;
            prodDetalleList = new ArrayList<>(DB_Produccion.consultarProduccionDetalle(prodCabecera.getId()));
            for (int i = 0; i < prodDetalleList.size(); i++) {
                Row filaProdDetalle = sheet.createRow(filaActual);
                filaActual++;
                E_produccionDetalle get = prodDetalleList.get(i);
                filaProdDetalle.createCell(0).setCellValue(get.getProducto().getCodigo());
                filaProdDetalle.createCell(1).setCellValue(get.getProducto().getDescripcion());
                filaProdDetalle.createCell(2).setCellValue(get.getCantidad());
            }
            if (prodCabecera.getTipo().getId() == E_produccionTipo.PRODUCTO_TERMINADO) {
                //INICIO CABECERA PRODUCCION DETALLE (ROLLO UTILIZADO)
                Row cabeceraProduccionDetalleRollo = sheet.createRow(filaActual);
                filaActual++;
                int col = 0;
                cabeceraProduccionDetalleRollo.createCell(col).setCellValue(new HSSFRichTextString("Nro. Film"));
                cabeceraProduccionDetalleRollo.getCell(col).setCellStyle(style1);
                col++;
                cabeceraProduccionDetalleRollo.createCell(col).setCellValue(new HSSFRichTextString("Cod."));
                cabeceraProduccionDetalleRollo.getCell(col).setCellStyle(style1);
                col++;
                cabeceraProduccionDetalleRollo.createCell(col).setCellValue(new HSSFRichTextString("Rollo"));
                cabeceraProduccionDetalleRollo.getCell(col).setCellStyle(style1);
                col++;
                cabeceraProduccionDetalleRollo.createCell(col).setCellValue(new HSSFRichTextString("Peso utilizado"));
                cabeceraProduccionDetalleRollo.getCell(col).setCellStyle(style1);
                col++;
                cabeceraProduccionDetalleRollo.createCell(col).setCellValue(new HSSFRichTextString("Cono"));
                cabeceraProduccionDetalleRollo.getCell(col).setCellStyle(style1);
                col++;
                cabeceraProduccionDetalleRollo.createCell(col).setCellValue(new HSSFRichTextString("Medida"));
                cabeceraProduccionDetalleRollo.getCell(col).setCellStyle(style1);
                col++;
                cabeceraProduccionDetalleRollo.createCell(col).setCellValue(new HSSFRichTextString("Micron"));
                cabeceraProduccionDetalleRollo.getCell(col).setCellStyle(style1);
                col++;
                cabeceraProduccionDetalleRollo.createCell(col).setCellValue(new HSSFRichTextString("Tipo materia prima"));
                cabeceraProduccionDetalleRollo.getCell(col).setCellStyle(style1);
                col++;
                //FIN CABECERA PRODUCCION DETALLE

                //INICIO DETALLE PRODUCCION DETALLE(ROLLO UTILIZADO)
                ArrayList<E_produccionFilm> prodDetalleListRollo;
                prodDetalleListRollo = new ArrayList<>(DB_Produccion.consultarProduccionFilmBaja(prodCabecera.getId()));
                for (int i = 0; i < prodDetalleListRollo.size(); i++) {
                    Row filaProdDetalle = sheet.createRow(filaActual);
                    filaActual++;
                    E_produccionFilm get = prodDetalleListRollo.get(i);
                    filaProdDetalle.createCell(0).setCellValue(get.getNroFilm());
                    filaProdDetalle.createCell(1).setCellValue(get.getProducto().getCodigo());
                    filaProdDetalle.createCell(2).setCellValue(get.getProducto().getDescripcion());
                    filaProdDetalle.createCell(3).setCellValue(get.getPeso());
                    filaProdDetalle.createCell(4).setCellValue(get.getCono());
                    filaProdDetalle.createCell(5).setCellValue(get.getMedida());
                    filaProdDetalle.createCell(6).setCellValue(get.getMicron());
                    filaProdDetalle.createCell(7).setCellValue(get.getProductoClasificacion().getDescripcion());
                }
            }
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
        sheet.autoSizeColumn(7);
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

    public void exportacionAgrupada() {
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
        //INICIO CABECERA PRODUCCION DETALLE
        Row cabeceraProduccionDetalle = sheet.createRow(filaActual);
        filaActual++;
        cabeceraProduccionDetalle.createCell(0).setCellValue(new HSSFRichTextString("Cod."));
        cabeceraProduccionDetalle.getCell(0).setCellStyle(style1);
        cabeceraProduccionDetalle.createCell(1).setCellValue(new HSSFRichTextString("Producto"));
        cabeceraProduccionDetalle.getCell(1).setCellStyle(style1);
        cabeceraProduccionDetalle.createCell(2).setCellValue(new HSSFRichTextString("Cantidad"));
        cabeceraProduccionDetalle.getCell(2).setCellStyle(style1);
        //FIN CABECERA PRODUCCION DETALLE

        //INICIO DETALLE PRODUCCION DETALLE
        ArrayList<E_produccionDetalle> prodDetalleList;
        prodDetalleList = new ArrayList<>(DB_Produccion.consultarProduccionDetalleAgrupado(prodCabeceraList));
        for (int i = 0; i < prodDetalleList.size(); i++) {
            Row filaProdDetalle = sheet.createRow(filaActual);
            filaActual++;
            E_produccionDetalle get = prodDetalleList.get(i);
            filaProdDetalle.createCell(0).setCellValue(get.getProducto().getCodigo());
            filaProdDetalle.createCell(1).setCellValue(get.getProducto().getDescripcion());
            filaProdDetalle.createCell(2).setCellValue(get.getCantidad());
        }
        filaActual++;

        //INICIO AJUSTAR COLUMNAS
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
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

    /*
    Funcion utilizado para buscar produccion de rollos 
     */
    public void exportacionAgrupadaPorDetalle() {
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
        //INICIO CABECERA PRODUCCION DETALLE
        Row cabeceraProduccionDetalle = sheet.createRow(filaActual);
        filaActual++;
        cabeceraProduccionDetalle.createCell(0).setCellValue(new HSSFRichTextString("Cod."));
        cabeceraProduccionDetalle.getCell(0).setCellStyle(style1);
        cabeceraProduccionDetalle.createCell(1).setCellValue(new HSSFRichTextString("Producto"));
        cabeceraProduccionDetalle.getCell(1).setCellStyle(style1);
        cabeceraProduccionDetalle.createCell(2).setCellValue(new HSSFRichTextString("Cono"));
        cabeceraProduccionDetalle.getCell(2).setCellStyle(style1);
        cabeceraProduccionDetalle.createCell(3).setCellValue(new HSSFRichTextString("Medida"));
        cabeceraProduccionDetalle.getCell(3).setCellStyle(style1);
        cabeceraProduccionDetalle.createCell(4).setCellValue(new HSSFRichTextString("Micron"));
        cabeceraProduccionDetalle.getCell(4).setCellStyle(style1);
        cabeceraProduccionDetalle.createCell(5).setCellValue(new HSSFRichTextString("Peso producido"));
        cabeceraProduccionDetalle.getCell(5).setCellStyle(style1);
        cabeceraProduccionDetalle.createCell(6).setCellValue(new HSSFRichTextString("Peso utilizado"));
        cabeceraProduccionDetalle.getCell(6).setCellStyle(style1);
        cabeceraProduccionDetalle.createCell(7).setCellValue(new HSSFRichTextString("Peso disponible"));
        cabeceraProduccionDetalle.getCell(7).setCellStyle(style1);
        //FIN CABECERA PRODUCCION DETALLE

        //INICIO DETALLE PRODUCCION DETALLE
        double totalProducido = 0;
        double totalUtilizado = 0;
        double totalDisponible = 0;
        for (int i = 0; i < produccionFilmCabecera.size(); i++) {
            Row filaProdDetalle = sheet.createRow(filaActual);
            filaActual++;
            E_produccionFilm get = produccionFilmCabecera.get(i);
            filaProdDetalle.createCell(0).setCellValue(get.getProducto().getCodigo());
            filaProdDetalle.createCell(1).setCellValue(get.getProducto().getDescripcion());
            filaProdDetalle.createCell(2).setCellValue(get.getCono());
            filaProdDetalle.createCell(3).setCellValue(get.getMedida());
            filaProdDetalle.createCell(4).setCellValue(get.getMicron());
            filaProdDetalle.createCell(5).setCellValue(get.getPeso());
            filaProdDetalle.createCell(6).setCellValue(get.getPesoUtilizado());
            filaProdDetalle.createCell(7).setCellValue(get.getPesoActual());
            totalProducido = totalProducido + get.getPeso();
            totalUtilizado = totalUtilizado + get.getPesoUtilizado();
            totalDisponible = totalDisponible + get.getPesoActual();
        }
        filaActual++;
        /*
        FILA TOTAL PRODUCIDO
         */
        Row rowTotalProducido = sheet.createRow(filaActual);
        filaActual++;
        rowTotalProducido.createCell(0).setCellValue(new HSSFRichTextString("Total producido"));
        rowTotalProducido.getCell(0).setCellStyle(style2);
        rowTotalProducido.createCell(1).setCellValue(totalProducido);
        rowTotalProducido.getCell(1).setCellStyle(styleNumber1);
        /*
        FILA TOTAL PRODUCIDO
         */
        Row rowTotalUtilizado = sheet.createRow(filaActual);
        filaActual++;
        rowTotalUtilizado.createCell(0).setCellValue(new HSSFRichTextString("Total utilizado"));
        rowTotalUtilizado.getCell(0).setCellStyle(style2);
        rowTotalUtilizado.createCell(1).setCellValue(totalUtilizado);
        rowTotalUtilizado.getCell(1).setCellStyle(styleNumber1);
        /*
        FILA TOTAL PRODUCIDO
         */
        Row rowTotalDisponible = sheet.createRow(filaActual);
        filaActual++;
        rowTotalDisponible.createCell(0).setCellValue(new HSSFRichTextString("Total disponible"));
        rowTotalDisponible.getCell(0).setCellStyle(style2);
        rowTotalDisponible.createCell(1).setCellValue(totalDisponible);
        rowTotalDisponible.getCell(1).setCellStyle(styleNumber1);
        //INICIO AJUSTAR COLUMNAS
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(4);
        sheet.autoSizeColumn(5);
        sheet.autoSizeColumn(6);
        sheet.autoSizeColumn(7);
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
