/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Entities.E_produccionDesperdicioDetalle;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class DesperdicioDetalleAgrupadoTableModel extends AbstractTableModel {

    private DecimalFormat decimalFormat;
    private List<E_produccionDesperdicioDetalle> list;
    private final String[] colNames = {"Código", "Producto", "Cantidad", "Tipo"};

    public DesperdicioDetalleAgrupadoTableModel() {
        this.decimalFormat = new DecimalFormat("#,##0.##");
        this.list = new ArrayList<>();
    }

    public void setList(List<E_produccionDesperdicioDetalle> list) {
        this.list = list;
        updateTable();
    }

    public List<E_produccionDesperdicioDetalle> getList() {
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
        E_produccionDesperdicioDetalle pdd = this.list.get(rowIndex);
        switch (colIndex) {
            case 0: {
                return pdd.getProducto().getCodigo();
            }
            case 1: {
                return pdd.getProducto().getDescripcion();
            }
            case 2: {
                return decimalFormat.format(pdd.getCantidad());
            }
            case 3: {
                return pdd.getTipoBaja().getDescripcion();
            }
            default: {
                return null;
            }
        }
    }

    public void agregarDatos(E_produccionDesperdicioDetalle pdd) {
        this.list.add(pdd);
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

    public void modificarCantidadDetalle(int index, double cantidad) {
        this.list.get(index).setCantidad(cantidad);
        fireTableCellUpdated(index, 1);
        fireTableDataChanged();
    }
}
