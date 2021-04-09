/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Entities.E_produccionDetalle;
import Entities.E_produccionDetallePlus;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class ProduccionDetalleAgrupadaTableModel extends AbstractTableModel {

    public static final int SIMPLE = 1, DETALLE = 2, COMPLETA = 3;
    private List<E_produccionDetallePlus> produccionList;
    private String[] colNames;
    private DecimalFormat decimalFormat;
    private int tipo;

    public ProduccionDetalleAgrupadaTableModel(int tipo) {
        this.tipo = tipo;
        this.decimalFormat = new DecimalFormat("#,##0.##");
        this.produccionList = new ArrayList<>();
        switch (tipo) {
            case SIMPLE: {
                this.colNames = new String[]{"Código", "Descripcion", "Cant. producida"};
                break;
            }
            case DETALLE: {
                this.colNames = new String[]{"Código", "Descripción", "Cant. producida", "Cant. actual"};
                break;
            }
            case COMPLETA: {
                this.colNames = new String[]{"Código", "Descripción", "Cant. producida", "Cant. vendida", "Cant. actual"};
                break;
            }
        }
    }

    public List<E_produccionDetallePlus> getList() {
        return produccionList;
    }

    public void setList(List<E_produccionDetallePlus> produccionList) {
        this.produccionList = produccionList;
        updateTable();
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
        return this.produccionList.size();
    }

    @Override
    public int getColumnCount() {
        return this.colNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int colIndex) {
        E_produccionDetallePlus produccion = this.produccionList.get(rowIndex);
        switch (colIndex) {
            case 0: {
                return produccion.getProducto().getCodigo();
            }
            case 1: {
                return produccion.getProducto().getDescripcion();
            }
            case 2: {
                return decimalFormat.format(produccion.getCantidad());
            }
            case 3: {
                return decimalFormat.format(produccion.getCantidadVendida());
            }
            case 4: {
                return decimalFormat.format(produccion.getProducto().getCantActual());
            }
            default: {
                return null;
            }
        }
    }

    public void agregarDetalle(E_produccionDetallePlus produccion) {
        this.produccionList.add(produccion);
        updateTable();
    }

    public void modificarCantidadDetalle(int index, double cantidad) {
        this.produccionList.get(index).setCantidad(cantidad);
        fireTableCellUpdated(index, 0);
        updateTable();
    }

    public void quitarDetalle(int index) {
        this.produccionList.remove(index);
        updateTable();
    }

    public void vaciarLista() {
        this.produccionList.clear();
        updateTable();
    }

    public void updateTable() {
        fireTableDataChanged();
    }

}
