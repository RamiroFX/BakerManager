/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Entities.E_NotaCreditoDetalle;
import Entities.E_impuesto;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class NotaCreditoDetalleTableModel extends AbstractTableModel {

    private DecimalFormat decimalFormat;
    private List<E_NotaCreditoDetalle> list;
    private final String[] colNames = {"Cod.", "Cantidad", "Producto", "Precio", "Descuento", "Exenta", "IVA5%", "IVA10%"};

    public NotaCreditoDetalleTableModel() {
        this.decimalFormat = new DecimalFormat("#,##0.##");
        this.list = new ArrayList<>();
    }

    public void setList(List<E_NotaCreditoDetalle> list) {
        this.list = list;
        updateTable();
    }

    public List<E_NotaCreditoDetalle> getList() {
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
        E_NotaCreditoDetalle nd = this.list.get(rowIndex);
//{"Cod.", "Cantidad", "Producto", "Precio", "Descuento", "Exenta", "IVA5%", "IVA10%", "Observación"}
        switch (colIndex) {
            case 0: {
                return nd.getProducto().getCodigo();
            }
            case 1: {
                return decimalFormat.format(nd.getCantidad());
            }
            case 2: {
                return nd.getProducto().getDescripcion();
            }
            case 3: {
                return decimalFormat.format(nd.getPrecio());
            }
            case 4: {
                return nd.getDescuento();
            }
            case 5:
                if (nd.getProducto().getIdImpuesto() == E_impuesto.EXENTA) {
                    return decimalFormat.format(nd.getSubTotal());
                }
                return 0;
            case 6: {
                if (nd.getProducto().getIdImpuesto() == E_impuesto.IVA5) {
                    return decimalFormat.format(nd.getSubTotal());
                }
                return 0;
            }
            case 7: {
                if (nd.getProducto().getIdImpuesto() == E_impuesto.IVA10) {
                    return decimalFormat.format(nd.getSubTotal());
                }
                return 0;
            }
            default: {
                return null;
            }
        }
    }

    public void agregarDatos(E_NotaCreditoDetalle nc) {
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

    public void modificarCantidadDetalle(int index, double cantidad) {
        this.list.get(index).setCantidad(cantidad);
        fireTableCellUpdated(index, 1);
        fireTableDataChanged();
    }
}
