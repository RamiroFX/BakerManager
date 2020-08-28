/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Entities.M_facturaCabecera;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class FacturaCabeceraTableModel extends AbstractTableModel {

    public static final int SIMPLE = 1, MEDIANO = 2, COMPLETO = 3;
    private DecimalFormat decimalFormat;
    private SimpleDateFormat dateFormater;
    private NumberFormat nfSmall, nfLarge;
    private List<M_facturaCabecera> facturaCabeceraList;
    private final String[] colNames = {"Id.", "Nro Factura", "Cliente", "Funcionario", "Tiempo", "Total", "Cond. venta"};
    private int formato;

    public FacturaCabeceraTableModel(int formato) {
        this.formato = formato;
        this.dateFormater = new SimpleDateFormat("dd/MM/YYYY hh:mm:ss");
        this.decimalFormat = new DecimalFormat("#,##0.##");
        this.nfSmall = new DecimalFormat("000");
        this.nfLarge = new DecimalFormat("0000000");
        this.facturaCabeceraList = new ArrayList<>();
    }

    public void setFacturaCabeceraList(List<M_facturaCabecera> facturaCabeceraList) {
        this.facturaCabeceraList = facturaCabeceraList;
        updateTable();
    }

    public List<M_facturaCabecera> getFacturaCabeceraList() {
        return facturaCabeceraList;
    }

    @Override
    public String getColumnName(int i) {
        return this.colNames[i];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return getValueAt(0, columnIndex).getClass();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public int getRowCount() {
        return this.facturaCabeceraList.size();
    }

    @Override
    public int getColumnCount() {
        return this.colNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int colIndex) {
        M_facturaCabecera fc = this.facturaCabeceraList.get(rowIndex);
        switch (colIndex) {
            case 0: {
                return decimalFormat.format(fc.getIdFacturaCabecera());
            }
            case 1: {
                switch (formato) {
                    case SIMPLE: {
                        return decimalFormat.format(fc.getNroFactura());
                    }
                    case MEDIANO: {
                        String sucursal = this.nfSmall.format(fc.getTimbrado().getNroSucursal());
                        String ptva = this.nfSmall.format(fc.getTimbrado().getNroPuntoVenta());
                        String nroFactura = decimalFormat.format(fc.getNroFactura());
                        String value = sucursal + "-" + ptva + "-" + nroFactura;
                        return value;
                    }
                    case COMPLETO: {
                        String timbrado = this.nfLarge.format(fc.getTimbrado().getNroTimbrado());
                        String sucursal = this.nfSmall.format(fc.getTimbrado().getNroSucursal());
                        String ptva = this.nfSmall.format(fc.getTimbrado().getNroPuntoVenta());
                        String nroFactura = decimalFormat.format(fc.getNroFactura());
                        String value = timbrado + "-" + sucursal + "-" + ptva + "-" + nroFactura;
                        return value;
                    }
                }
            }
            case 2: {
                return fc.getCliente().getEntidad();
            }
            case 3: {
                return fc.getFuncionario().getAlias();
            }
            case 4: {
                return dateFormater.format(fc.getTiempo());
            }
            case 5: {
                return decimalFormat.format(fc.getTotal());
            }
            case 6: {
                return fc.getCondVenta().getDescripcion();
            }
            default: {
                return null;
            }
        }
    }

    public void agregarDatos(M_facturaCabecera fc) {
        this.facturaCabeceraList.add(fc);
        fireTableDataChanged();
    }

    public void quitarDatos(int index) {
        this.facturaCabeceraList.remove(index);
        fireTableDataChanged();
    }

    public void vaciarLista() {
        this.facturaCabeceraList.clear();
        fireTableDataChanged();
    }

    public void updateTable() {
        fireTableDataChanged();
    }
}
