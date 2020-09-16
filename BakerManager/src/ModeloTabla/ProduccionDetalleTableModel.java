/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Entities.E_produccionDetalle;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class ProduccionDetalleTableModel extends AbstractTableModel {

    private List<E_produccionDetalle> produccionList;
    private final String[] colNames = {"Cantidad", "Código", "Descripcion"};

    public ProduccionDetalleTableModel() {
        this.produccionList = new ArrayList<>();
    }

    public List<E_produccionDetalle> getList() {
        return produccionList;
    }

    public void setList(List<E_produccionDetalle> produccionList) {
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
        E_produccionDetalle produccion = this.produccionList.get(rowIndex);
        switch (colIndex) {
            case 0: {
                return produccion.getCantidad();
            }
            case 1: {
                return produccion.getProducto().getCodigo();
            }
            case 2: {
                return produccion.getProducto().getDescripcion();
            }
            default: {
                return null;
            }
        }
    }

    public void agregarDetalle(E_produccionDetalle produccion) {
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
