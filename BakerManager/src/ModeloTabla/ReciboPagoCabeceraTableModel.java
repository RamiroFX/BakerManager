/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Entities.E_reciboPagoCabecera;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class ReciboPagoCabeceraTableModel extends AbstractTableModel {

    private List<E_reciboPagoCabecera> list;
    private final String[] colNames = {"Id", "Nro Recibo", "Proveedor", "Fecha pago", "Monto"};

    public ReciboPagoCabeceraTableModel() {
        this.list = new ArrayList<>();
    }

    public void setList(List<E_reciboPagoCabecera> facturaCabeceraList) {
        this.list = facturaCabeceraList;
        updateTable();
    }

    public List<E_reciboPagoCabecera> getList() {
        return list;
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
        E_reciboPagoCabecera row = this.list.get(rowIndex);
        switch (colIndex) {
            case 0: {
                return row.getId();
            }
            case 1: {
                return row.getNroRecibo();
            }
            case 2: {
                return row.getProveedor().getEntidad();
            }
            case 3: {
                return row.getFechaPago();
            }
            case 4: {
                return row.getMonto();
            }
            default: {
                return null;
            }
        }
    }

    public void agregarDatos(E_reciboPagoCabecera data) {
        this.list.add(data);
        fireTableDataChanged();
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
