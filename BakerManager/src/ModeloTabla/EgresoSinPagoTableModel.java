/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Entities.E_egresoSinPago;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class EgresoSinPagoTableModel extends AbstractTableModel {

    private List<E_egresoSinPago> list;
    private final String[] colNames = {"Id.", "Nro Factura", "Cliente", "Fecha", "Monto", "Pago", "Saldo"};

    public EgresoSinPagoTableModel() {
        this.list = new ArrayList<>();
    }

    public void setList(List<E_egresoSinPago> facturaCabeceraList) {
        this.list = facturaCabeceraList;
        updateTable();
    }

    public List<E_egresoSinPago> getList() {
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
        E_egresoSinPago row = this.list.get(rowIndex);
        switch (colIndex) {
            case 0: {
                return row.getIdCabecera();
            }
            case 1: {
                return row.getNroFactura();
            }
            case 2: {
                return row.getProveedorEntidad();
            }
            case 3: {
                return row.getFecha();
            }
            case 4: {
                return row.getMonto();
            }
            case 5: {
                return row.getPago();
            }
            case 6: {
                return row.getSaldo();
            }
            default: {
                return null;
            }
        }
    }

    public void agregarDatos(E_egresoSinPago data) {
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
