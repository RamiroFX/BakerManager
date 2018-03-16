/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class CajaTableModel extends AbstractTableModel {

    List<SeleccionProductoCategoria> productoCategoriaList;
    private String[] colNames = {"Id", "Descripcion", "Seleccionado"};

    public CajaTableModel() {
        productoCategoriaList = new ArrayList<>();
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
        return columnIndex == 2;
    }

    @Override
    public int getRowCount() {
        return this.productoCategoriaList.size();
    }

    @Override
    public int getColumnCount() {
        return this.colNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int colIndex) {
        SeleccionProductoCategoria productoCategoria = this.productoCategoriaList.get(rowIndex);
        switch (colIndex) {
            case 0: {
                return productoCategoria.getProductoCategoria().getId();
            }
            case 1: {
                return productoCategoria.getProductoCategoria().getDescripcion();
            }
            case 2: {
                return productoCategoria.isEstaSeleccionado();
            }
            default: {
                return null;
            }
        }
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        if (aValue instanceof Boolean && column == 2) {
            SeleccionProductoCategoria rowData = productoCategoriaList.get(row);
            rowData.setEstaSeleccionado((boolean) aValue);
            fireTableCellUpdated(row, column);
        }
    }

    public void setProductoCategoriaList(List<SeleccionProductoCategoria> productoCategoriaList) {
        this.productoCategoriaList = productoCategoriaList;
    }

    public void updateTable() {
        fireTableDataChanged();
    }
}
