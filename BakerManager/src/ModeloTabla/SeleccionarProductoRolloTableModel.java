/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Entities.M_producto;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class SeleccionarProductoRolloTableModel extends AbstractTableModel {

    List<M_producto> list;
    private String[] colNames = {"Id", "Código", "Descripción"};

    public SeleccionarProductoRolloTableModel() {
        list = new ArrayList<>();
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
        return this.list.size();
    }

    @Override
    public int getColumnCount() {
        return this.colNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int colIndex) {
        M_producto producto = this.list.get(rowIndex);
        switch (colIndex) {
            case 0: {
                return producto.getId();
            }
            case 1: {
                return producto.getCodigo();
            }
            case 2: {
                return producto.getDescripcion();
            }
            default: {
                return null;
            }
        }
    }

    public void setList(List<M_producto> productoList) {
        this.list = productoList;
        updateTable();
    }

    public List<M_producto> getProductoList() {
        return list;
    }

    public void quitarDatos(int index) {
        this.list.remove(index);
        fireTableDataChanged();
    }

    public void vaciarLista() {
        this.list.clear();
        fireTableDataChanged();
    }

    public void updateTable() {
        fireTableDataChanged();
    }
}
