/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Entities.E_produccionDesperdicioDetalle;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class ProduccionTerminadosTableModel extends AbstractTableModel {

    private DecimalFormat decimalFormat;
    private SimpleDateFormat dateFormater;
    List<E_produccionDesperdicioDetalle> list;
    private final String[] colNames = {"Fecha", "O.T.", "Código", "Producto", "Cant. producida", "Cant. actual"};

    public ProduccionTerminadosTableModel() {
        this.decimalFormat = new DecimalFormat("#,##0.##");
        this.dateFormater = new SimpleDateFormat("dd/MM/YYYY hh:mm:ss");
        this.list = new ArrayList<>();
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
                return dateFormater.format(pdd.getDesperdicioCabecera().getProduccionCabecera().getFechaProduccion());
            }
            case 1: {
                return decimalFormat.format(pdd.getDesperdicioCabecera().getProduccionCabecera().getNroOrdenTrabajo());
            }
            case 2: {
                return pdd.getProducto().getCodigo();
            }
            case 3: {
                return pdd.getProducto().getDescripcion();
            }
            case 4: {
                return decimalFormat.format(pdd.getCantidad());
            }
            case 5: {
                return decimalFormat.format(pdd.getProducto().getCantActual());
            }
            default: {
                return null;
            }
        }
    }

    public void setList(List<E_produccionDesperdicioDetalle> productoList) {
        this.list = productoList;
        updateTable();
    }

    public List<E_produccionDesperdicioDetalle> getList() {
        return list;
    }

    public void agregarDatos(E_produccionDesperdicioDetalle film) {
        this.list.add(film);
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
}
