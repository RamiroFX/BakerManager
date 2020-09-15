/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Excel;

import Entities.M_producto;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellUtil;

/**
 *
 * @author Ramiro Ferreira
 */
public class ExportarProducto {

    private static final String MENSAJE_ERROR = "No hay productos para exportar";
    private static final String TITULO_ERROR = "Atención";

    private HSSFWorkbook workbook;
    private ArrayList<HSSFSheet> sheets;
    private CellStyle style1, style2, style3;
    private File directory;
    private ArrayList<M_producto> productos;

    /**
     *
     * @param nombreHoja Especifíca el nombre del archivo excel a crear.
     */
    public ExportarProducto() {
        createWorkBook();
        createCellStyles();
    }

    public ExportarProducto(ArrayList<M_producto> productos) {
        this.productos = productos;
        createWorkBook();
        createCellStyles();
    }

    private void createWorkBook() {
        workbook = new HSSFWorkbook();
        sheets = new ArrayList<>();
    }

    private void createCellStyles() {
        // FORMAT STYLE
        DataFormat format = workbook.createDataFormat();

        style1 = workbook.createCellStyle();
        style1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style1.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style1.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style1.setBorderLeft(HSSFCellStyle.BORDER_THIN);

        style2 = workbook.createCellStyle();
        style2.setDataFormat(format.getFormat("#,##0"));

        style3 = workbook.createCellStyle();
        style3.setDataFormat(workbook.createDataFormat().getFormat("0.0%"));
        //END FORMAT STYLE
    }

    public void exportar() {
        //PREPARAR CONTENIDO
        int fila = 0;//En preparar cuerpo empieza en cero (0).
        int hoja = 0;//En preparar cuerpo empieza en cero (0).
        if (productos != null && !productos.isEmpty()) {
            sheets.add(workbook.createSheet());
        } else {
            JOptionPane.showMessageDialog(null, MENSAJE_ERROR, TITULO_ERROR, JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        //PREPARAR DOCUMENTO
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
        Row cabeceraCajas = sheets.get(hoja).createRow(fila);
        cabeceraCajas.createCell(0).setCellValue(new HSSFRichTextString("ID"));
        cabeceraCajas.createCell(1).setCellValue(new HSSFRichTextString("Cod."));
        cabeceraCajas.createCell(2).setCellValue(new HSSFRichTextString("Producto"));
        cabeceraCajas.createCell(3).setCellValue(new HSSFRichTextString("Marca"));
        cabeceraCajas.createCell(4).setCellValue(new HSSFRichTextString("Impuesto(%)"));
        cabeceraCajas.createCell(5).setCellValue(new HSSFRichTextString("Rubro"));
        cabeceraCajas.createCell(6).setCellValue(new HSSFRichTextString("Precio costo"));
        cabeceraCajas.createCell(7).setCellValue(new HSSFRichTextString("Precio minorsta"));
        cabeceraCajas.createCell(8).setCellValue(new HSSFRichTextString("Precio mayorista"));
        cabeceraCajas.createCell(9).setCellValue(new HSSFRichTextString("Estado"));
        cabeceraCajas.createCell(10).setCellValue(new HSSFRichTextString("Stock"));
        cabeceraCajas.getCell(0).setCellStyle(style1);
        cabeceraCajas.getCell(1).setCellStyle(style1);
        cabeceraCajas.getCell(2).setCellStyle(style1);
        cabeceraCajas.getCell(3).setCellStyle(style1);
        cabeceraCajas.getCell(4).setCellStyle(style1);
        cabeceraCajas.getCell(5).setCellStyle(style1);
        cabeceraCajas.getCell(6).setCellStyle(style1);
        cabeceraCajas.getCell(7).setCellStyle(style1);
        cabeceraCajas.getCell(8).setCellStyle(style1);
        cabeceraCajas.getCell(9).setCellStyle(style1);
        cabeceraCajas.getCell(10).setCellStyle(style1);
        CellUtil.setAlignment(cabeceraCajas.getCell(0), workbook, CellStyle.ALIGN_CENTER);
        CellUtil.setAlignment(cabeceraCajas.getCell(1), workbook, CellStyle.ALIGN_CENTER);
        CellUtil.setAlignment(cabeceraCajas.getCell(2), workbook, CellStyle.ALIGN_CENTER);
        CellUtil.setAlignment(cabeceraCajas.getCell(3), workbook, CellStyle.ALIGN_CENTER);
        CellUtil.setAlignment(cabeceraCajas.getCell(4), workbook, CellStyle.ALIGN_CENTER);
        CellUtil.setAlignment(cabeceraCajas.getCell(5), workbook, CellStyle.ALIGN_CENTER);
        CellUtil.setAlignment(cabeceraCajas.getCell(6), workbook, CellStyle.ALIGN_CENTER);
        CellUtil.setAlignment(cabeceraCajas.getCell(7), workbook, CellStyle.ALIGN_CENTER);
        CellUtil.setAlignment(cabeceraCajas.getCell(8), workbook, CellStyle.ALIGN_CENTER);
        CellUtil.setAlignment(cabeceraCajas.getCell(9), workbook, CellStyle.ALIGN_CENTER);
        CellUtil.setAlignment(cabeceraCajas.getCell(10), workbook, CellStyle.ALIGN_CENTER);
        fila++;
        for (M_producto producto : productos) {
            Row unProducto = sheets.get(hoja).createRow(fila);
            unProducto.createCell(0).setCellValue(producto.getId());
            unProducto.createCell(1).setCellValue(producto.getCodBarra());
            unProducto.createCell(2).setCellValue(new HSSFRichTextString(producto.getDescripcion()));
            unProducto.createCell(3).setCellValue(new HSSFRichTextString(producto.getMarca()));
            unProducto.createCell(4).setCellValue(producto.getImpuesto());
            unProducto.createCell(5).setCellValue(new HSSFRichTextString(producto.getCategoria()));
            unProducto.createCell(6).setCellValue(producto.getPrecioCosto());
            unProducto.createCell(7).setCellValue(producto.getPrecioVenta());
            unProducto.createCell(8).setCellValue(producto.getPrecioMayorista());
            unProducto.createCell(9).setCellValue(new HSSFRichTextString(producto.getEstado()));
            unProducto.createCell(10).setCellValue(producto.getCantActual());
            //unProducto.getCell(4).setCellStyle(style3);
            unProducto.getCell(6).setCellStyle(style2);
            unProducto.getCell(7).setCellStyle(style2);
            unProducto.getCell(8).setCellStyle(style2);
            fila++;
        }
        sheets.get(hoja).autoSizeColumn(0);
        sheets.get(hoja).autoSizeColumn(1);
        sheets.get(hoja).autoSizeColumn(2);
        sheets.get(hoja).autoSizeColumn(3);
        sheets.get(hoja).autoSizeColumn(4);
        sheets.get(hoja).autoSizeColumn(5);
        sheets.get(hoja).autoSizeColumn(6);
        sheets.get(hoja).autoSizeColumn(7);
        sheets.get(hoja).autoSizeColumn(8);
        sheets.get(hoja).autoSizeColumn(9);
        sheets.get(hoja).autoSizeColumn(10);
        try (FileOutputStream out = new FileOutputStream(directory.getPath() + ".xls")) {
            workbook.write(out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
