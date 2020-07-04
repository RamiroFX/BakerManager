/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Entities.E_NotaCreditoCabecera;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class NotaCreditoCabeceraTableModel extends AbstractTableModel {

    private SimpleDateFormat dateFormater;
    private DecimalFormat decimalFormat;
    private List<E_NotaCreditoCabecera> list;
    private final String[] colNames = {"Id.", "Nro Nota Crédito", "Cliente", "Funcionario", "Tiempo", "Total", "Cond. venta"};

    public NotaCreditoCabeceraTableModel() {
        this.dateFormater = new SimpleDateFormat("dd/MM/YYYY hh:mm:ss");
        this.decimalFormat = new DecimalFormat("###,###");
        this.list = new ArrayList<>();
    }

    public void setList(List<E_NotaCreditoCabecera> list) {
        this.list = list;
        updateTable();
    }

    public List<E_NotaCreditoCabecera> getList() {
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
        E_NotaCreditoCabecera nc = this.list.get(rowIndex);
        switch (colIndex) {
            case 0: {
                return nc.getId();
            }
            case 1: {
                return nc.getNroNotaCredito();
            }
            case 2: {
                return nc.getCliente().getEntidad();
            }
            case 3: {
                return nc.getFuncionario().getNombre();
            }
            case 4: {
                return dateFormater.format(nc.getTiempo());
            }
            case 5: {
                return decimalFormat.format(nc.getTotal());
            }
            case 6: {
                return nc.getFacturaCabecera().getTipoOperacion().getDescripcion();
            }
            default: {
                return null;
            }
        }
    }

    public void agregarDatos(E_NotaCreditoCabecera nc) {
        this.list.add(nc);
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
