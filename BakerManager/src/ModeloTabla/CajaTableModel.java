/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Entities.Caja;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class CajaTableModel extends AbstractTableModel {

    private SimpleDateFormat dateFormater;
    List<Caja> list;
    private String[] colNames = {"ID", "Func. apertura", "Func. cierre", "Monto inicial", "Monto final", "Tiempo apertura", "Tiempo cierre"};

    public CajaTableModel() {
        this.dateFormater = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
        list = new ArrayList<>();
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
        return columnIndex == 2;
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
        Caja caja = this.list.get(rowIndex);
        switch (colIndex) {
            case 0: {
                return caja.getIdCaja();
            }
            case 1: {
                return caja.getFuncionarioApertura().getNombre();
            }
            case 2: {
                return caja.getFuncionarioCierre().getNombre();
            }
            case 3: {
                return caja.getMontoApertura();
            }
            case 4: {
                return caja.getMontoCierre();
            }
            case 5: {
                return dateFormater.format(caja.getTiempoApertura());
            }
            case 6: {
                return dateFormater.format(caja.getTiempoCierre());
            }
            default: {
                return null;
            }
        }
    }

    public void setList(List<Caja> list) {
        this.list = list;
        updateTable();
    }

    public List<Caja> getList() {
        return list;
    }

    public void updateTable() {
        fireTableDataChanged();
    }
}
