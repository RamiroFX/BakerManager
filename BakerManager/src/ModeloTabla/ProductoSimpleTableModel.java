/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Entities.M_producto;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class ProductoSimpleTableModel extends AbstractTableModel {

    List<M_producto> list;
    private DecimalFormat integerFormat, decimalFormat;
    private String[] colNames = {"Id", "Descripcion", "Cant. actual"};

    public ProductoSimpleTableModel() {
        list = new ArrayList<>();
        this.integerFormat = new DecimalFormat("###,###");
        this.decimalFormat = new DecimalFormat("#,##0.##");
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
        M_producto unProducto = this.list.get(rowIndex);
        switch (colIndex) {
            case 0: {
                return integerFormat.format(unProducto.getId());
            }
            case 1: {
                return unProducto.getDescripcion();
            }
            case 2: {
                return decimalFormat.format(unProducto.getCantActual());
            }
            default: {
                return null;
            }
        }
    }

    public void setList(List<M_producto> list) {
        this.list = list;
        updateTable();
    }

    public List<M_producto> getList() {
        return list;
    }

    public void updateTable() {
        fireTableDataChanged();
    }
}
