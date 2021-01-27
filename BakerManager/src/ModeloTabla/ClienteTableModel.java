/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Entities.M_cliente;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class ClienteTableModel extends AbstractTableModel {

    private DecimalFormat decimalFormat;
    private List<M_cliente> list;
    private final String[] colNames = {"Id.", "Entidad", "Nombre", "R.U.C."};

    public ClienteTableModel() {
        this.decimalFormat = new DecimalFormat("###,###");
        this.list = new ArrayList<>();
    }

    public void setList(List<M_cliente> list) {
        this.list = list;
        updateTable();
    }

    public List<M_cliente> getList() {
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
        M_cliente unCliente = this.list.get(rowIndex);
        switch (colIndex) {
            case 0: {
                return decimalFormat.format(unCliente.getIdCliente());
            }
            case 1: {
                return unCliente.getEntidad();
            }
            case 2: {
                if (unCliente.getNombre() == null) {
                    return "";
                }
                return unCliente.getNombre();
            }
            case 3: {
                return unCliente.getRucCompleto();
            }
            default: {
                return null;
            }
        }
    }

    public void agregarDatos(M_cliente cliente) {
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
