/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Interface.InterfaceSeleccionVentaCabecera;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class SeleccionVentaCabeceraTableModel extends AbstractTableModel {

    ArrayList<SeleccionVentaCabecera> list;
    private String[] colNames = {"Id", "Cliente", "Funcionario", "Tiempo", "Total", "Cond.", "Seleccionado"};

    private InterfaceSeleccionVentaCabecera interfaceSeleccionVentaCabecera;

    public SeleccionVentaCabeceraTableModel() {
        list = new ArrayList<>();
    }

    public SeleccionVentaCabeceraTableModel(InterfaceSeleccionVentaCabecera interfaceSeleccionVentaCabecera) {
        list = new ArrayList<>();
        this.interfaceSeleccionVentaCabecera = interfaceSeleccionVentaCabecera;
    }

    public void setInterface(InterfaceSeleccionVentaCabecera interfaceSeleccionVentaCabecera) {
        this.interfaceSeleccionVentaCabecera = interfaceSeleccionVentaCabecera;
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
        SeleccionVentaCabecera seleccionVentaCabecera = this.list.get(rowIndex);
        switch (colIndex) {
            case 0: {
                return seleccionVentaCabecera.getFacturaCabecera().getIdFacturaCabecera();
            }
            case 1: {
                return seleccionVentaCabecera.getFacturaCabecera().getCliente().getEntidad();
            }
            case 2: {
                return seleccionVentaCabecera.getFacturaCabecera().getFuncionario().getNombre();
            }
            case 3: {
                return seleccionVentaCabecera.getFacturaCabecera().getTiempo();
            }
            case 4: {
                return seleccionVentaCabecera.getFacturaCabecera().getTotal();
            }
            case 5: {
                return seleccionVentaCabecera.getFacturaCabecera().getTipoOperacion().getDescripcion();
            }
            case 6: {
                return seleccionVentaCabecera.isEstaSeleccionado();
            }
            default: {
                return null;
            }
        }
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        if (aValue instanceof Boolean && column == 6) {
            SeleccionVentaCabecera rowData = list.get(row);
            rowData.setEstaSeleccionado((boolean) aValue);
            interfaceSeleccionVentaCabecera.notificarCambioSeleccion();
            fireTableCellUpdated(row, column);
        }
    }

    public void setList(ArrayList<SeleccionVentaCabecera> list) {
        this.list = list;
        updateTable();
    }

    public void updateTable() {
        fireTableDataChanged();
    }

    public ArrayList<SeleccionVentaCabecera> getList() {
        return list;
    }
}
