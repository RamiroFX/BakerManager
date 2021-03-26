/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Entities.E_ajusteStockDetalle;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class AjusteStockDetalleTableModel extends AbstractTableModel {

    private SimpleDateFormat dateFormater;
    private DecimalFormat decimalFormat;
    private List<E_ajusteStockDetalle> list;
    private final String[] colNames = {"id", "Cod.", "Producto", "Cant. actual", "Cant. nueva", "Tiempo", "Motivo", "Obs."};

    public AjusteStockDetalleTableModel() {
        this.dateFormater = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
        this.decimalFormat = new DecimalFormat("#,##0.##");
        this.list = new ArrayList<>();
    }

    public void setList(List<E_ajusteStockDetalle> list) {
        this.list = list;
        updateTable();
    }

    public List<E_ajusteStockDetalle> getList() {
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
        E_ajusteStockDetalle data = this.list.get(rowIndex);
        switch (colIndex) {
            case 0: {
                return data.getProducto().getId();
            }
            case 1: {
                return data.getProducto().getCodigo();
            }
            case 2: {
                return data.getProducto().getDescripcion();
            }
            case 3: {
                return decimalFormat.format(data.getCantidadVieja());
            }
            case 4: {
                return decimalFormat.format(data.getCantidadNueva());
            }
            case 5:
                return dateFormater.format(data.getTiempoRegistro());
            case 6: {
                return data.getMotivo().getDescripcion();
            }
            case 7: {
                if (data.getObservacion() != null) {
                    return data.getObservacion();
                }else{
                    return "";
                }
            }
            default: {
                return null;
            }
        }
    }

    public void agregarDatos(E_ajusteStockDetalle nc) {
        this.list.add(nc);
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

    public void modificarCantidadViejaDetalle(int index, double cantidad) {
        this.list.get(index).setCantidadVieja(cantidad);
        fireTableCellUpdated(index, 1);
        fireTableDataChanged();
    }

    public void modificarCantidadNuevaDetalle(int index, double cantidad) {
        this.list.get(index).setCantidadNueva(cantidad);
        fireTableCellUpdated(index, 1);
        fireTableDataChanged();
    }
}
