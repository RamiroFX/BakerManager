/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Caja;

import Entities.ArqueoCajaDetalle;
import Entities.Moneda;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class ArqueoCajaTableModel extends AbstractTableModel {

    List<ArqueoCajaDetalle> arqueoCajaDetalleList;
    private String[] colNames = {"Id", "Cantidad", "Denominación", "Importe"};

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
                return arqueoCaja.getMoneda().getValor() + " - " + arqueoCaja.getMoneda().getDescripcion();
            }
            case 3: {
                return arqueoCaja.getCantidad() * arqueoCaja.getMoneda().getValor();
            }
            default: {
                return null;
            }
        }
    }

    public void setArqueoCajaList(List<ArqueoCajaDetalle> arqueoCajaDetalle) {
        this.arqueoCajaDetalleList = arqueoCajaDetalle;
    }

    public void updateTable() {
        fireTableDataChanged();
    }
}
