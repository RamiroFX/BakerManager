/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Entities.E_impuesto;
import Entities.E_clienteproducto;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class ClienteProductoTableModel extends AbstractTableModel {

    private DecimalFormat decimalFormat;
    private List<E_clienteproducto> list;
    private final String[] colNames = {"Cliente", "Impuesto", "Precio"};

    public ClienteProductoTableModel() {
        this.decimalFormat = new DecimalFormat("#,##0.##");
        this.list = new ArrayList<>();
    }

    public void setList(List<E_clienteproducto> list) {
        this.list = list;
        updateTable();
    }

    public List<E_clienteproducto> getList() {
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
        E_clienteproducto nd = this.list.get(rowIndex);
        switch (colIndex) {
            case 0: {
                return nd.getCliente().getEntidad();
            }
            case 1: {
                return nd.getImpuesto();
            }
            case 2: {
                return decimalFormat.format(nd.getPrecio());
            }
            default: {
                return null;
            }
        }
    }

    public void agregarDatos(E_clienteproducto nc) {
        this.list.add(nc);
        updateTable();
    }

    public void quitarDatos(int index) {
        this.list.remove(index);
        updateTable();
    }

    public void vaciarLista() {
        this.list.clear();
        updateTable();
    }

    public void updateTable() {
        fireTableDataChanged();
    }

    public void modificarDetalle(int index, E_impuesto impuesto, int precio) {
        this.list.get(index).setPrecio(precio);
        this.list.get(index).setImpuesto(impuesto);
        fireTableDataChanged();
    }
}
