/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Entities.E_impuesto;
import Entities.M_egreso_detalle;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class EgresoDetalleTableModel extends AbstractTableModel {

    private static final String SOLO_DECIMAL = "Ingrese solo números decimales (Ej. 3.14)";
    private static final String SOLO_ENTERO = "Ingrese solo números enteros (Ej. 13)";
    private static final String ATENCION = "Atención";

    private List<M_egreso_detalle> list;
    private final String[] colNames = {"Cod.", "Cantidad", "Descripcion", "Precio", "Descuento", "Exenta", "IVA 5%", "IVA 10%", "Observacion"};

    public EgresoDetalleTableModel() {
        this.list = new ArrayList<>();
    }

    public List<M_egreso_detalle> getList() {
        return list;
    }

    public void setList(List<M_egreso_detalle> list) {
        this.list = list;
        updateTable();
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
        M_egreso_detalle fd = this.list.get(rowIndex);
        switch (colIndex) {
            case 0: {
                return fd.getProducto().getCodigo();
            }
            case 1: {
                return fd.getCantidad();
            }
            case 2: {
                return fd.getProducto().getDescripcion();
            }
            case 3: {
                return fd.getPrecio();
            }
            case 4: {
                return fd.getDescuento();
            }
            case 5: {
                if (fd.getProducto().getIdImpuesto() == E_impuesto.EXENTA) {
                    Integer Precio = fd.getPrecio() - Math.round(Math.round(((fd.getPrecio() * fd.getDescuento()) / 100)));
                    return Math.round(Math.round((fd.getCantidad() * Precio)));
                }
                return 0;
            }
            case 6: {
                if (fd.getProducto().getIdImpuesto() == E_impuesto.IVA5) {
                    Integer Precio = fd.getPrecio() - Math.round(Math.round(((fd.getPrecio() * fd.getDescuento()) / 100)));
                    return Math.round(Math.round((fd.getCantidad() * Precio)));
                }
                return 0;
            }
            case 7: {
                if (fd.getProducto().getIdImpuesto() == E_impuesto.IVA10) {
                    Integer Precio = fd.getPrecio() - Math.round(Math.round(((fd.getPrecio() * fd.getDescuento()) / 100)));
                    return Math.round(Math.round((fd.getCantidad() * Precio)));
                }
                return 0;
            }
            case 8: {
                if (fd.getObservacion() != null) {
                    return fd.getObservacion();
                } else {
                    return "";
                }
            }
            default: {
                return null;
            }
        }
    }

    public void agregarDetalle(M_egreso_detalle fd) {
        this.list.add(fd);
        fireTableDataChanged();
    }

    public void quitarDetalle(int index) {
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
