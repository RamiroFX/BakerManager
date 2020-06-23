/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Interface.InterfaceSeleccionCompraCabecera;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class SeleccionCompraCabeceraTableModel extends AbstractTableModel {

    private SimpleDateFormat dateFormater;
    private ArrayList<SeleccionCompraCabecera> list;
    private String[] colNames = {"Id", "Nro. factura", "Proveedor", "Funcionario", "Tiempo", "Total", "Cond.", "Seleccionado"};

    private InterfaceSeleccionCompraCabecera interfaceSeleccionVentaCabecera;

    public SeleccionCompraCabeceraTableModel() {
        this.dateFormater = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
        list = new ArrayList<>();
    }

    public SeleccionCompraCabeceraTableModel(InterfaceSeleccionCompraCabecera nterfaceSeleccionCompraCabecera) {
        this.dateFormater = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
        list = new ArrayList<>();
        this.interfaceSeleccionVentaCabecera = nterfaceSeleccionCompraCabecera;
    }

    public void setInterface(InterfaceSeleccionCompraCabecera nterfaceSeleccionCompraCabecera) {
        this.interfaceSeleccionVentaCabecera = nterfaceSeleccionCompraCabecera;
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
        return columnIndex == 7;
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
        SeleccionCompraCabecera seleccionCompraCabecera = this.list.get(rowIndex);
        switch (colIndex) {
            case 0: {
                return seleccionCompraCabecera.getFacturaCabecera().getId_cabecera();
            }
            case 1: {
                return seleccionCompraCabecera.getFacturaCabecera().getNro_factura();
            }
            case 2: {
                return seleccionCompraCabecera.getFacturaCabecera().getProveedor().getEntidad();
            }
            case 3: {
                return seleccionCompraCabecera.getFacturaCabecera().getFuncionario().getNombre();
            }
            case 4: {
                return dateFormater.format(seleccionCompraCabecera.getFacturaCabecera().getTiempo());
            }
            case 5: {
                return seleccionCompraCabecera.getFacturaCabecera().getTotal();
            }
            case 6: {
                return seleccionCompraCabecera.getFacturaCabecera().getCondVenta();
            }
            case 7: {
                return seleccionCompraCabecera.isEstaSeleccionado();
            }
            default: {
                return null;
            }
        }
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        if (aValue instanceof Boolean && column == 7) {
            SeleccionCompraCabecera rowData = list.get(row);
            rowData.setEstaSeleccionado((boolean) aValue);
            interfaceSeleccionVentaCabecera.notificarCambioSeleccionCompraCabecera();
            fireTableCellUpdated(row, column);
        }
    }

    public void setList(ArrayList<SeleccionCompraCabecera> list) {
        this.list = list;
        updateTable();
    }

    public void updateTable() {
        fireTableDataChanged();
    }

    public ArrayList<SeleccionCompraCabecera> getList() {
        return list;
    }
}
