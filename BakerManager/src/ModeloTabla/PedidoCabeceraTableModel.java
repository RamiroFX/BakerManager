/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Entities.M_pedidoCabecera;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class PedidoCabeceraTableModel extends AbstractTableModel {

    public static final int SIMPLE = 1, MEDIANO = 2, COMPLETO = 3;
    private DecimalFormat decimalFormat;
    private SimpleDateFormat dateFormater;
    private List<M_pedidoCabecera> list;
    private final String[] colNames = {"Id.", "Vendedor", "Cliente", "Tiempo recepción", "Tiempo entrega", "Total", "Estado", "Cond. venta"};
    private int formato;

    public PedidoCabeceraTableModel(int formato) {
        this.formato = formato;
        this.dateFormater = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
        this.decimalFormat = new DecimalFormat("#,##0.##");
        this.list = new ArrayList<>();
    }

    public void setList(List<M_pedidoCabecera> list) {
        this.list = list;
        updateTable();
    }

    public List<M_pedidoCabecera> getList() {
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
        M_pedidoCabecera fc = this.list.get(rowIndex);
        switch (colIndex) {
            case 0: {
                return decimalFormat.format(fc.getIdPedido());
            }
            case 1: {
                return fc.getFuncionario().getNombre();
            }
            case 2: {
                return fc.getCliente().getEntidad();
            }
            case 3: {
                return dateFormater.format(fc.getTiempoRecepcion());
            }
            case 4: {
                return dateFormater.format(fc.getTiempoEntrega());
            }
            case 5: {
                return decimalFormat.format(fc.getTotal());
            }
            case 6: {
                return fc.getEstadoPedido().getDescripcion();
            }
            case 7: {
                return fc.getTipoOperacion().getDescripcion();
            }
            default: {
                return null;
            }
        }
    }

    public void agregarDatos(M_pedidoCabecera fc) {
        this.list.add(fc);
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
