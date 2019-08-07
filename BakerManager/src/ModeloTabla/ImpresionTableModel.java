/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Entities.M_campoImpresion;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class ImpresionTableModel extends AbstractTableModel {

    List<M_campoImpresion> campoImpresionList;
    private String[] colNames = {"Campo", "coord. X", "coord. Y"};

    public ImpresionTableModel() {
        campoImpresionList = new ArrayList<>();
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
        return columnIndex == 2 || columnIndex == 1;
    }

    @Override
    public int getRowCount() {
        return this.campoImpresionList.size();
    }

    @Override
    public int getColumnCount() {
        return this.colNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int colIndex) {
        M_campoImpresion campoImpresion = this.campoImpresionList.get(rowIndex);
        switch (colIndex) {
            case 0: {
                return campoImpresion.getCampo();
            }
            case 1: {
                return campoImpresion.getX();
            }
            case 2: {
                return campoImpresion.getY();
            }
            default: {
                return null;
            }
        }
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        if (aValue instanceof Integer && column == 1) {
            M_campoImpresion rowData = campoImpresionList.get(row);
            rowData.setX((Double) aValue);
            fireTableCellUpdated(row, column);
        } else if (aValue instanceof Integer && column == 2) {
            M_campoImpresion rowData = campoImpresionList.get(row);
            rowData.setY((Double) aValue);
            fireTableCellUpdated(row, column);
        }
    }

    public void setCampoImpresionList(List<M_campoImpresion> campoImpresionList) {
        this.campoImpresionList = campoImpresionList;
    }

    public void updateTable() {
        fireTableDataChanged();
    }
}
