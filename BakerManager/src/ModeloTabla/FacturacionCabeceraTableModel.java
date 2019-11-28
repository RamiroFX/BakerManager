/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Entities.E_facturacionCabecera;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class FacturacionCabeceraTableModel extends AbstractTableModel {

    private List<E_facturacionCabecera> facturacionCabeceraList;
    private final String[] colNames = {"Id.", "Nro Factura", "Cliente", "Funcionario", "Tiempo", "Total"};

    public FacturacionCabeceraTableModel() {
        this.facturacionCabeceraList = new ArrayList<>();
    }

    public void setFacturacionCabeceraList(List<E_facturacionCabecera> facturaCabeceraList) {
        this.facturacionCabeceraList = facturaCabeceraList;
    }

    public List<E_facturacionCabecera> getFacturacionCabeceraList() {
        return facturacionCabeceraList;
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
        return this.facturacionCabeceraList.size();
    }

    @Override
    public int getColumnCount() {
        return this.colNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int colIndex) {
        E_facturacionCabecera fc = this.facturacionCabeceraList.get(rowIndex);
        switch (colIndex) {
            case 0: {
                return fc.getId();
            }
            case 1: {
                return fc.getNroFactura();
            }
            case 2: {
                return fc.getCliente().getEntidad();
            }
            case 3: {
                return fc.getFuncionario().getNombre();
            }
            case 4: {
                return fc.getTiempo();
            }
            case 5: {
                return fc.getTotal();
            }
            default: {
                return null;
            }
        }
    }

    public void agregarDatos(E_facturacionCabecera fc) {
        this.facturacionCabeceraList.add(fc);
        fireTableDataChanged();
    }

    public void quitarDatos(int index) {
        this.facturacionCabeceraList.remove(index);
        fireTableDataChanged();
    }

    public void vaciarLista() {
        this.facturacionCabeceraList.clear();
        fireTableDataChanged();
    }

    public void updateTable() {
        fireTableDataChanged();
    }
}
