/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Caja;

import Entities.ArqueoCajaDetalle;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class ArqueoCajaTableModel extends AbstractTableModel {

    List<ArqueoCajaDetalle> arqueoCajaDetalleList;
    private String[] colNames = {"Id", "Cantidad", "Denominación", "Tipo", "Importe"};

    public ArqueoCajaTableModel() {
        arqueoCajaDetalleList = new ArrayList<ArqueoCajaDetalle>();
    }

    @Override
    public String getColumnName(int i) {
        return this.colNames[i];
    }

    @Override
    public int getRowCount() {
        return this.arqueoCajaDetalleList.size();
    }

    @Override
    public int getColumnCount() {
        return this.colNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int colIndex) {
        ArqueoCajaDetalle arqueoCaja = this.arqueoCajaDetalleList.get(rowIndex);
        switch (colIndex) {
            case 0: {
                return arqueoCaja.getMoneda().getIdMoneda();
            }
            case 1: {
                return arqueoCaja.getCantidad();
            }
            case 2: {
                return arqueoCaja.getMoneda().getValor();
            }
            case 3: {
                return arqueoCaja.getMoneda().getDescripcion();
            }
            case 4: {
                return arqueoCaja.getCantidad() * arqueoCaja.getMoneda().getValor();
            }
            default: {
                return null;
            }
        }
    }

    public boolean isCellEditable(int row, int col) {
        if (col == 1) {
            return true;
        }
        return false;
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        switch (col) {
            case 1: {
                try {
                    arqueoCajaDetalleList.get(row).setCantidad((Integer.valueOf(value.toString())));
                } catch (Exception e) {
                    arqueoCajaDetalleList.get(row).setCantidad(0);
                    JOptionPane.showMessageDialog(null, "Ingrese solo números enteros", "Atención", JOptionPane.WARNING_MESSAGE);
                }
                break;
            }
        }
        fireTableCellUpdated(row, col);
    }

    public void setArqueoCajaList(List<ArqueoCajaDetalle> arqueoCajaDetalle) {
        this.arqueoCajaDetalleList = arqueoCajaDetalle;
    }

    public void updateTable() {
        fireTableDataChanged();
    }
}
