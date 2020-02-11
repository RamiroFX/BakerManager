/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Entities.E_cuentaCorrienteCabecera;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class CtaCteCabeceraTableModel extends AbstractTableModel {

    private List<E_cuentaCorrienteCabecera> list;
    private final String[] colNames = {"Id", "Nro Recibo", "Cliente", "Cobrador", "Fecha pago", "Monto"};

    public CtaCteCabeceraTableModel() {
        this.list = new ArrayList<>();
    }

    public void setList(List<E_cuentaCorrienteCabecera> facturaCabeceraList) {
        this.list = facturaCabeceraList;
        updateTable();
    }

    public List<E_cuentaCorrienteCabecera> getList() {
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
        E_cuentaCorrienteCabecera row = this.list.get(rowIndex);
        switch (colIndex) {
            case 0: {
                return row.getId();
            }
            case 1: {
                return row.getNroRecibo();
            }
            case 2: {
                return row.getCliente().getEntidad();
            }
            case 3: {
                return row.getCobrador().getNombre();
            }
            case 4: {
                return row.getFechaPago();
            }
            case 5: {
                return row.getDebito();
            }
            default: {
                return null;
            }
        }
    }

    public void agregarDatos(E_cuentaCorrienteCabecera data) {
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
