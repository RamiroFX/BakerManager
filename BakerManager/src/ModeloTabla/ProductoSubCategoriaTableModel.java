/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Entities.ProductoCategoria;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class ProductoSubCategoriaTableModel extends AbstractTableModel {

    List<ProductoCategoria> list;
    private String[] colNames = {"Id", "Categoría", "Sub categoría"};

    public ProductoSubCategoriaTableModel() {
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
        ProductoCategoria productoCategoria = this.list.get(rowIndex);
        switch (colIndex) {
            case 0: {
                return productoCategoria.getId();
            }
            case 1: {
                return productoCategoria.getPadre().getDescripcion();
            }
            case 2: {
                return productoCategoria.getDescripcion();
            }
            default: {
                return null;
            }
        }
    }

    public void setList(List<ProductoCategoria> list) {
        this.list = list;
        updateTable();
    }

    public void updateTable() {
        fireTableDataChanged();
    }
}
