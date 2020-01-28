/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Entities.M_facturaCabecera;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class FacturaCabeceraTableModel extends AbstractTableModel {

    private List<M_facturaCabecera> facturaCabeceraList;
    private final String[] colNames = {"Id.", "Nro Factura", "Cliente", "Funcionario", "Tiempo", "Total", "Cond. venta"};

    public FacturaCabeceraTableModel() {
        this.facturaCabeceraList = new ArrayList<>();
    }

    public void setFacturaCabeceraList(List<M_facturaCabecera> facturaCabeceraList) {
        this.facturaCabeceraList = facturaCabeceraList;
        updateTable();
    }

    public List<M_facturaCabecera> getFacturaCabeceraList() {
        return facturaCabeceraList;
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
        return this.facturaCabeceraList.size();
    }

    @Override
    public int getColumnCount() {
        return this.colNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int colIndex) {
        M_facturaCabecera fc = this.facturaCabeceraList.get(rowIndex);
        switch (colIndex) {
            case 0: {
                return fc.getIdFacturaCabecera();
            }
            case 1: {
                return fc.getNroFactura();
            }
            case 2: {
                return fc.getCliente().getEntidad();
            }
            case 3: {
                return fc.getFuncionario().getAlias();
            }
            case 4: {
                return fc.getTiempo();
            }
            case 5: {
                return fc.getTotal();
            }
            case 6: {
                return fc.getCondVenta().getDescripcion();
            }
            default: {
                return null;
            }
        }
    }

    public void agregarDatos(M_facturaCabecera fc) {
        this.facturaCabeceraList.add(fc);
        fireTableDataChanged();
    }

    public void quitarDatos(int index) {
        this.facturaCabeceraList.remove(index);
        fireTableDataChanged();
    }

    public void vaciarLista() {
        this.facturaCabeceraList.clear();
        fireTableDataChanged();
    }

    public void updateTable() {
        fireTableDataChanged();
    }
}
