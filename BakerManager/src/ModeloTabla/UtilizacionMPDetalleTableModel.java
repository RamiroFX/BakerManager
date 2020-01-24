/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Entities.E_utilizacionMateriaPrimaDetalle;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ramiro
 */
public class UtilizacionMPDetalleTableModel extends AbstractTableModel {

    private List<E_utilizacionMateriaPrimaDetalle> list;
    private final String[] colNames = {"Cantidad", "Código", "Descripcion"};

    public UtilizacionMPDetalleTableModel() {
        this.list = new ArrayList<>();
    }

    public List<E_utilizacionMateriaPrimaDetalle> getList() {
        return list;
    }

    public void setList(List<E_utilizacionMateriaPrimaDetalle> list) {
        this.list = list;
        updateTable();
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
        E_utilizacionMateriaPrimaDetalle detalleUtilizacion = this.list.get(rowIndex);
        switch (colIndex) {
            case 0: {
                return detalleUtilizacion.getCantidad();
            }
            case 1: {
                return detalleUtilizacion.getProducto().getCodigo();
            }
            case 2: {
                return detalleUtilizacion.getProducto().getDescripcion();
            }
            default: {
                return null;
            }
        }
    }

    public void agregarDetalle(E_utilizacionMateriaPrimaDetalle detalle) {
        this.list.add(detalle);
        fireTableDataChanged();
    }

    public void modificarCantidadDetalle(int index, double cantidad) {
        this.list.get(index).setCantidad(cantidad);
        fireTableCellUpdated(index, 0);
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
