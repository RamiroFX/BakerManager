/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Entities.E_cuentaCorrienteCabecera;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class AdelantoCobroPendienteTableModel extends AbstractTableModel {

    private DecimalFormat decimalFormat;
    private SimpleDateFormat dateFormater;
    private List<E_cuentaCorrienteCabecera> list;
    private final String[] colNames = {"Fecha pago", "Nro Recibo", "Monto", "Asignado", "Pendiente"};

    public AdelantoCobroPendienteTableModel() {
        this.dateFormater = new SimpleDateFormat("dd/MM/YYYY hh:mm:ss");
        this.decimalFormat = new DecimalFormat("#,##0.##");
        this.list = new ArrayList<>();
    }

    public void setList(List<E_cuentaCorrienteCabecera> facturaCabeceraList) {
        this.list = facturaCabeceraList;
        updateTable();
    }

    public List<E_cuentaCorrienteCabecera> getList() {
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
        //{ "Fecha pago", "Nro Recibo", "Monto", "Asignado",  "Pendiente"};
        E_cuentaCorrienteCabecera row = this.list.get(rowIndex);
        switch (colIndex) {
            case 0: {
                return dateFormater.format(row.getFechaPago());
            }
            case 1: {
                return decimalFormat.format(row.getNroRecibo());
            }
            case 2: {
                return decimalFormat.format(row.getDebito());
            }
            case 3: {
                return decimalFormat.format(row.getCredito());
            }
            case 4: {
                return decimalFormat.format(row.getDebito() - row.getCredito());
            }
            default: {
                return null;
            }
        }
    }

    public void agregarDatos(E_cuentaCorrienteCabecera data) {
        this.list.add(data);
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
