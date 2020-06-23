/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Interface.InterfaceSeleccionPagoCabecera;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class SeleccionPagoCabeceraTableModel extends AbstractTableModel {

    private SimpleDateFormat dateFormater;
    private ArrayList<SeleccionPagoCabecera> list;
    private String[] colNames = {"Id", "Nro. recibo", "Proveedor", "Funcionario", "Tiempo", "Total", "Seleccionado"};

    private InterfaceSeleccionPagoCabecera interfaceSeleccionPagoCabecera;

    public SeleccionPagoCabeceraTableModel() {
        this.dateFormater = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
        list = new ArrayList<>();
    }

    public SeleccionPagoCabeceraTableModel(InterfaceSeleccionPagoCabecera interfaceSeleccionPagoCabecera) {
        this.dateFormater = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
        list = new ArrayList<>();
        this.interfaceSeleccionPagoCabecera = interfaceSeleccionPagoCabecera;
    }

    public void setInterface(InterfaceSeleccionPagoCabecera interfaceSeleccionPagoCabecera) {
        this.interfaceSeleccionPagoCabecera = interfaceSeleccionPagoCabecera;
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
        return columnIndex == 6;
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
        SeleccionPagoCabecera seleccionPagoCabecera = this.list.get(rowIndex);
        switch (colIndex) {
            case 0: {
                return seleccionPagoCabecera.getPago().getId();
            }
            case 1: {
                return seleccionPagoCabecera.getPago().getNroRecibo();
            }
            case 2: {
                return seleccionPagoCabecera.getPago().getProveedor().getEntidad();
            }
            case 3: {
                return seleccionPagoCabecera.getPago().getFuncionario().getNombre();
            }
            case 4: {
                return dateFormater.format(seleccionPagoCabecera.getPago().getFechaPago());
            }
            case 5: {
                return seleccionPagoCabecera.getPago().getMonto();
            }
            case 6: {
                return seleccionPagoCabecera.isSeleccionado();
            }
            default: {
                return null;
            }
        }
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        if (aValue instanceof Boolean && column == 6) {
            SeleccionPagoCabecera rowData = list.get(row);
            rowData.setEstaSeleccionado((boolean) aValue);
            interfaceSeleccionPagoCabecera.notificarCambioSeleccionPagoCabecera();
            fireTableCellUpdated(row, column);
        }
    }

    public void setList(ArrayList<SeleccionPagoCabecera> list) {
        this.list = list;
        updateTable();
    }

    public void updateTable() {
        fireTableDataChanged();
    }

    public ArrayList<SeleccionPagoCabecera> getList() {
        return list;
    }
}
