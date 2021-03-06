/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Entities.E_ajusteStockMotivo;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class AjusteStockMotivoTableModel extends AbstractTableModel {

    private DecimalFormat decimalFormat;
    private List<E_ajusteStockMotivo> list;
    private final String[] colNames = {"Id.", "Motivo", "Descripcion"};

    public AjusteStockMotivoTableModel() {
        this.decimalFormat = new DecimalFormat("###,###");
        this.list = new ArrayList<>();
    }

    public void setList(List<E_ajusteStockMotivo> list) {
        this.list = list;
        updateTable();
    }

    public List<E_ajusteStockMotivo> getList() {
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
        E_ajusteStockMotivo unDetalle = this.list.get(rowIndex);
        switch (colIndex) {
            case 0: {
                return decimalFormat.format(unDetalle.getId());
            }
            case 1: {
                return unDetalle.getDescripcion();
            }
            case 2: {
                if (unDetalle.getObservacion() == null) {
                    return "";
                }
                return unDetalle.getObservacion();
            }
            default: {
                return null;
            }
        }
    }

    public void agregarDatos(E_ajusteStockMotivo cliente) {
        this.list.add(cliente);
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
