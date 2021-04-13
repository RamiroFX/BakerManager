/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Entities.M_proveedor;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class ProveedorTableModel extends AbstractTableModel {

    private DecimalFormat decimalFormat;
    private List<M_proveedor> list;
    private final String[] colNames = {"Id.", "Entidad", "Nombre", "R.U.C."};

    public ProveedorTableModel() {
        this.decimalFormat = new DecimalFormat("###,###");
        this.list = new ArrayList<>();
    }

    public void setList(List<M_proveedor> list) {
        this.list = list;
        updateTable();
    }

    public List<M_proveedor> getList() {
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
        M_proveedor data = this.list.get(rowIndex);
        switch (colIndex) {
            case 0: {
                return decimalFormat.format(data.getId());
            }
            case 1: {
                return data.getEntidad();
            }
            case 2: {
                if (data.getNombre() == null) {
                    return "";
                }
                return data.getNombre();
            }
            case 3: {
                return data.getRucCompleto();
            }
            default: {
                return null;
            }
        }
    }

    public void agregarDatos(M_proveedor cliente) {
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
