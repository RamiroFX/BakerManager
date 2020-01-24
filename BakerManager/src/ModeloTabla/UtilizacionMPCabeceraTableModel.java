/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Entities.E_utilizacionMateriaPrimaCabecera;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ramiro
 */
public class UtilizacionMPCabeceraTableModel extends AbstractTableModel {

    private List<E_utilizacionMateriaPrimaCabecera> list;
    private final String[] colNames = {"ID", "O.T.", "Fecha utilizacion", "Responsable"};

    public UtilizacionMPCabeceraTableModel() {
        this.list = new ArrayList<>();
    }

    public List<E_utilizacionMateriaPrimaCabecera> getList() {
        return list;
    }

    public void setList(List<E_utilizacionMateriaPrimaCabecera> produccionList) {
        this.list = produccionList;
        fireTableDataChanged();
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
        E_utilizacionMateriaPrimaCabecera utilizacionMP = this.list.get(rowIndex);
        switch (colIndex) {
            case 0: {
                return utilizacionMP.getId();
            }
            case 1: {
                return utilizacionMP.getNroOrdenTrabajo();
            }
            case 2: {
                return utilizacionMP.getFechaUtilizacion();
            }
            case 3: {
                return utilizacionMP.getFuncionarioProduccion().getNombre();
            }
            default: {
                return null;
            }
        }
    }

    public void agregarDetalle(E_utilizacionMateriaPrimaCabecera utilizacion) {
        this.list.add(utilizacion);
        fireTableDataChanged();
    }

    public void quitarDetalle(int index) {
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
