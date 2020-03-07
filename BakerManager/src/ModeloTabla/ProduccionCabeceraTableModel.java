/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Entities.E_produccionCabecera;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class ProduccionCabeceraTableModel extends AbstractTableModel {

    SimpleDateFormat dateFormater = new SimpleDateFormat("dd/MM/YYYY");
    private List<E_produccionCabecera> produccionList;
    private final String[] colNames = {"ID", "O.T.", "Fecha producción", "Responsable", "Tipo"};

    public ProduccionCabeceraTableModel() {
        this.produccionList = new ArrayList<>();
    }

    public List<E_produccionCabecera> getList() {
        return produccionList;
    }

    public void setList(List<E_produccionCabecera> produccionList) {
        this.produccionList = produccionList;
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
        return this.produccionList.size();
    }

    @Override
    public int getColumnCount() {
        return this.colNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int colIndex) {
        E_produccionCabecera produccion = this.produccionList.get(rowIndex);
        switch (colIndex) {
            case 0: {
                return produccion.getId();
            }
            case 1: {
                return produccion.getNroOrdenTrabajo();
            }
            case 2: {
                return dateFormater.format(produccion.getFechaProduccion());
            }
            case 3: {
                return produccion.getFuncionarioProduccion().getNombre();
            }
            case 4: {
                return produccion.getTipo();
            }
            default: {
                return null;
            }
        }
    }

    public void agregarDetalle(E_produccionCabecera produccion) {
        this.produccionList.add(produccion);
        fireTableDataChanged();
    }

    public void quitarDetalle(int index) {
        this.produccionList.remove(index);
        fireTableDataChanged();
    }

    public void vaciarLista() {
        this.produccionList.clear();
        fireTableDataChanged();
    }

    public void updateTable() {
        fireTableDataChanged();
    }

}
