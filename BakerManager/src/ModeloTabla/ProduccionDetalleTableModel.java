/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Entities.E_produccionDetalle;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class ProduccionDetalleTableModel extends AbstractTableModel {

    private static final String SOLO_DECIMAL = "Ingrese solo números decimales (Ej. 3.14)";
    private static final String SOLO_ENTERO = "Ingrese solo números enteros (Ej. 13)";
    private static final String ATENCION = "Atención";

    private List<E_produccionDetalle> produccionList;
    private final String[] colNames = {"Cantidad", "Código", "Descripcion"};

    public ProduccionDetalleTableModel() {
        this.produccionList = new ArrayList<>();
    }

    public List<E_produccionDetalle> getList() {
        return produccionList;
    }

    public void setList(List<E_produccionDetalle> produccionList) {
        this.produccionList = produccionList;
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
        E_produccionDetalle produccion = this.produccionList.get(rowIndex);
        switch (colIndex) {
            case 0: {
                return produccion.getCantidad();
            }
            case 1: {
                return produccion.getProducto().getCodigo();
            }
            case 2: {
                return produccion.getProducto().getDescripcion();
            }
            default: {
                return null;
            }
        }
    }

    public void agregarDetalle(E_produccionDetalle produccion) {
        this.produccionList.add(produccion);
        fireTableDataChanged();
    }

    public void modificarCantidadDetalle(int index, double cantidad) {
        this.produccionList.get(index).setCantidad(cantidad);
        fireTableCellUpdated(index, 0);
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