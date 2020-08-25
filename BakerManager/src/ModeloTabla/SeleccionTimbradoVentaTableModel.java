/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Entities.E_Timbrado;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class SeleccionTimbradoVentaTableModel extends AbstractTableModel {

    private SimpleDateFormat dateFormater;
    private DecimalFormat integerFormat;
    NumberFormat nfSmall, nfLarge;
    private List<E_Timbrado> list;
    private final String[] colNames = {"Nro. Timbrado-Sucursal-Punto venta", "Factura inicial", "Factura final", "Fecha vencimiento"};

    public SeleccionTimbradoVentaTableModel() {
        this.dateFormater = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
        this.integerFormat = new DecimalFormat("###,###");
        this.list = new ArrayList<>();
        nfSmall = new DecimalFormat("000");
        nfLarge = new DecimalFormat("0000000");
    }

    public void setList(List<E_Timbrado> list) {
        this.list = list;
        updateTable();
    }

    public List<E_Timbrado> getList() {
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
        E_Timbrado timbrado = this.list.get(rowIndex);
        switch (colIndex) {
            case 0: {
                return nfLarge.format(timbrado.getNroTimbrado()) + " - " + nfSmall.format(timbrado.getNroSucursal()) + " - " + nfSmall.format(timbrado.getNroPuntoVenta());
            }
            case 1: {
                return integerFormat.format(timbrado.getNroBoletaInicial());
            }
            case 2: {
                return integerFormat.format(timbrado.getNroBoletaFinal());
            }
            case 3: {
                return dateFormater.format(timbrado.getFechaVencimiento());
            }
            default: {
                return null;
            }
        }
    }

    public void agregarDatos(E_Timbrado dato) {
        this.list.add(dato);
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
