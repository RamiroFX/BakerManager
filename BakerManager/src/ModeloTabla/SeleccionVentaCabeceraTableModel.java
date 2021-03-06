/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Interface.InterfaceSeleccionVentaCabecera;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class SeleccionVentaCabeceraTableModel extends AbstractTableModel {

    private SimpleDateFormat dateFormater;
    private ArrayList<SeleccionVentaCabecera> list;
    private String[] colNames = {"Id", "Nro. factura", "Cliente", "Funcionario", "Tiempo", "Total", "Cond.", "Seleccionado"};

    private InterfaceSeleccionVentaCabecera interfaceSeleccionVentaCabecera;

    public SeleccionVentaCabeceraTableModel() {
        this.dateFormater = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
        list = new ArrayList<>();
    }

    public SeleccionVentaCabeceraTableModel(InterfaceSeleccionVentaCabecera interfaceSeleccionVentaCabecera) {
        this.dateFormater = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
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
        SeleccionVentaCabecera seleccionVentaCabecera = this.list.get(rowIndex);
        switch (colIndex) {
            case 0: {
                return seleccionVentaCabecera.getFacturaCabecera().getIdFacturaCabecera();
            }
            case 1: {
                return seleccionVentaCabecera.getFacturaCabecera().getNroFactura();
            }
            case 2: {
                return seleccionVentaCabecera.getFacturaCabecera().getCliente().getEntidad();
            }
            case 3: {
                return seleccionVentaCabecera.getFacturaCabecera().getFuncionario().getNombre();
            }
            case 4: {
                return dateFormater.format(seleccionVentaCabecera.getFacturaCabecera().getTiempo());
            }
            case 5: {
                return seleccionVentaCabecera.getFacturaCabecera().getTotal();
            }
            case 6: {
                return seleccionVentaCabecera.getFacturaCabecera().getTipoOperacion().getDescripcion();
            }
            case 7: {
                return seleccionVentaCabecera.isEstaSeleccionado();
            }
            default: {
                return null;
            }
        }
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        if (aValue instanceof Boolean && column == 7) {
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
