/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Interface.InterfaceSeleccionCobroCabecera;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class SeleccionCobroCabeceraTableModel extends AbstractTableModel {

    private SimpleDateFormat dateFormater;
    private ArrayList<SeleccionCobroCabecera> list;
    private String[] colNames = {"Id", "Nro. recibo", "Cliente", "Funcionario", "Tiempo", "Total", "Seleccionado"};

    private InterfaceSeleccionCobroCabecera interfaceSeleccionCobroCabecera;

    public SeleccionCobroCabeceraTableModel() {
        this.dateFormater = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
        list = new ArrayList<>();
    }

    public SeleccionCobroCabeceraTableModel(InterfaceSeleccionCobroCabecera interfaceSeleccionCobroCabecera) {
        this.dateFormater = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
        list = new ArrayList<>();
        this.interfaceSeleccionCobroCabecera = interfaceSeleccionCobroCabecera;
    }

    public void setInterface(InterfaceSeleccionCobroCabecera interfaceSeleccionCobroCabecera) {
        this.interfaceSeleccionCobroCabecera = interfaceSeleccionCobroCabecera;
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
        SeleccionCobroCabecera seleccionCompraCabecera = this.list.get(rowIndex);
        switch (colIndex) {
            case 0: {
                return seleccionCompraCabecera.getCobro().getId();
            }
            case 1: {
                return seleccionCompraCabecera.getCobro().getNroRecibo();
            }
            case 2: {
                return seleccionCompraCabecera.getCobro().getCliente().getEntidad();
            }
            case 3: {
                return seleccionCompraCabecera.getCobro().getFuncionario().getNombre();
            }
            case 4: {
                return dateFormater.format(seleccionCompraCabecera.getCobro().getFechaPago());
            }
            case 5: {
                return seleccionCompraCabecera.getCobro().getDebito();
            }
            case 6: {
                return seleccionCompraCabecera.isSeleccionado();
            }
            default: {
                return null;
            }
        }
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        if (aValue instanceof Boolean && column == 6) {
            SeleccionCobroCabecera rowData = list.get(row);
            rowData.setEstaSeleccionado((boolean) aValue);
            interfaceSeleccionCobroCabecera.notificarCambioSeleccionCobroCabecera();
            fireTableCellUpdated(row, column);
        }
    }

    public void setList(ArrayList<SeleccionCobroCabecera> list) {
        this.list = list;
        updateTable();
    }

    public void updateTable() {
        fireTableDataChanged();
    }

    public ArrayList<SeleccionCobroCabecera> getList() {
        return list;
    }
}
