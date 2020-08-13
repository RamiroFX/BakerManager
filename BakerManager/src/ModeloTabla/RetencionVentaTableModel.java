/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Entities.E_retencionVenta;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class RetencionVentaTableModel extends AbstractTableModel {

    private SimpleDateFormat dateFormater;
    private DecimalFormat decimalFormat, integerFormat;
    private List<E_retencionVenta> list;
    private final String[] colNames = {"Id.", "Nro. Retención", "Nro. Factura", "Cliente", "Funcionario", "Tiempo", "Retención(%)", "Total retención"};

    public RetencionVentaTableModel() {
        this.dateFormater = new SimpleDateFormat("dd/MM/YYYY hh:mm:ss");
        this.decimalFormat = new DecimalFormat("###,###.### '%'");
        this.integerFormat = new DecimalFormat("###,###");
        this.list = new ArrayList<>();
    }

    public void setList(List<E_retencionVenta> list) {
        this.list = list;
        updateTable();
    }

    public List<E_retencionVenta> getList() {
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
        E_retencionVenta rv = this.list.get(rowIndex);
        switch (colIndex) {
            case 0: {
                return integerFormat.format(rv.getId());
            }
            case 1: {
                return integerFormat.format(rv.getNroRetencion());
            }
            case 2: {
                return integerFormat.format(rv.getVenta().getNroFactura());
            }
            case 3: {
                return rv.getVenta().getCliente().getEntidad();
            }
            case 4: {
                return rv.getVenta().getFuncionario().getNombre();
            }
            case 5: {
                return dateFormater.format(rv.getTiempo());
            }
            case 6: {
                return decimalFormat.format(rv.getPorcentaje());
            }
            case 7: {
                return integerFormat.format(rv.getMonto());
            }
            default: {
                return null;
            }
        }
    }

    public void agregarDatos(E_retencionVenta rv) {
        this.list.add(rv);
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
