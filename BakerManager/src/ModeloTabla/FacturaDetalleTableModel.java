/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Interface.InterfaceFacturaDetalle;
import Entities.M_facturaDetalle;
import Parametros.Impuesto;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class FacturaDetalleTableModel extends AbstractTableModel {

    private static final String SOLO_DECIMAL = "Ingrese solo números decimales (Ej. 3.14)";
    private static final String SOLO_ENTERO = "Ingrese solo números enteros (Ej. 13)";
    private static final String ATENCION = "Atención";

    private List<M_facturaDetalle> facturaDetalleList;
    private final String[] colNames = {"Cod.", "Cantidad", "Descripcion", "Precio", "Descuento", "Exenta", "IVA 5%", "IVA 10%", "Observacion"};

    private final InterfaceFacturaDetalle interfaceFacturaDetalle;

    public FacturaDetalleTableModel(InterfaceFacturaDetalle interfaceFacturaDetalle) {
        this.facturaDetalleList = new ArrayList<>();
        this.interfaceFacturaDetalle = interfaceFacturaDetalle;
    }

    public List<M_facturaDetalle> getFacturaDetalleList() {
        return facturaDetalleList;
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
        switch (columnIndex) {
            case 1: {
                return true;
            }
            case 3: {
                return true;
            }
            case 4: {
                return true;
            }
            default: {
                return false;
            }
        }
    }

    @Override
    public int getRowCount() {
        return this.facturaDetalleList.size();
    }

    @Override
    public int getColumnCount() {
        return this.colNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int colIndex) {
        M_facturaDetalle fd = this.facturaDetalleList.get(rowIndex);
        switch (colIndex) {
            case 0: {
                return fd.getProducto().getCodBarra();
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
                if (fd.getProducto().getImpuesto() == 0) {
                    Integer Precio = fd.getPrecio() - Math.round(Math.round(((fd.getPrecio() * fd.getDescuento()) / 100)));
                    return Math.round(Math.round((fd.getCantidad() * Precio)));
                }
                return 0;
            }
            case 6: {
                if (fd.getProducto().getImpuesto() == 5) {
                    Integer Precio = fd.getPrecio() - Math.round(Math.round(((fd.getPrecio() * fd.getDescuento()) / 100)));
                    return Math.round(Math.round((fd.getCantidad() * Precio)));
                }
                return 0;
            }
            case 7: {
                if (fd.getProducto().getImpuesto() == 10) {
                    Integer Precio = fd.getPrecio() - Math.round(Math.round(((fd.getPrecio() * fd.getDescuento()) / 100)));
                    return Math.round(Math.round((fd.getCantidad() * Precio)));
                }
                return 0;
            }
            case 8: {
                return fd.getObservacion();
            }
            default: {
                return null;
            }
        }
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        switch (column) {
            case 1: {
                modificarCantidadFila(aValue, row);
                break;
            }
            case 3: {
                modificarPrecioFila(aValue, row);
                break;
            }
            case 4: {
                modificarDescuentoFila(aValue, row);
                break;
            }
        }
        interfaceFacturaDetalle.notificarCambioFacturaDetalle();
        fireTableCellUpdated(row, column);
        fireTableDataChanged();
    }

    public void setFacturaDetalleList(List<M_facturaDetalle> facturaDetalleList) {
        this.facturaDetalleList = facturaDetalleList;
        updateTable();
    }

    public void agregarDetalle(M_facturaDetalle fd) {
        this.facturaDetalleList.add(fd);
        fireTableDataChanged();
    }

    public void quitarDetalle(int index) {
        this.facturaDetalleList.remove(index);
        fireTableDataChanged();
    }

    public void vaciarLista() {
        this.facturaDetalleList.clear();
        fireTableDataChanged();
    }

    private void modificarCantidadFila(Object aValue, int row) {
        try {
            Double cantidad = (Double.valueOf(aValue.toString()));
            Double descuento = facturaDetalleList.get(row).getDescuento();
            Integer precio = facturaDetalleList.get(row).getPrecio();
            Integer precioConDescuento = precio - Math.round(Math.round(((precio * descuento) / 100)));
            Integer total = Math.round(Math.round((cantidad * precioConDescuento)));
            facturaDetalleList.get(row).setCantidad(cantidad);
            modificarImpuestos(row, total);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, SOLO_DECIMAL, ATENCION, JOptionPane.WARNING_MESSAGE);
        }
    }

    private void modificarPrecioFila(Object aValue, int row) {
        try {
            Integer precio = (Integer.valueOf(aValue.toString()));
            Double cantidad = facturaDetalleList.get(row).getCantidad();
            Double descuento = facturaDetalleList.get(row).getDescuento();
            Integer precioConDescuento = precio - Math.round(Math.round(((precio * descuento) / 100)));
            Integer total = Math.round(Math.round((cantidad * precioConDescuento)));
            facturaDetalleList.get(row).setPrecio(precio);
            modificarImpuestos(row, total);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, SOLO_ENTERO, ATENCION, JOptionPane.WARNING_MESSAGE);
        }
    }

    private void modificarDescuentoFila(Object aValue, int row) {
        try {
            Double descuento = (Double.valueOf(aValue.toString()));
            Double cantidad = facturaDetalleList.get(row).getCantidad();
            Integer precio = facturaDetalleList.get(row).getPrecio();
            Integer precioConDescuento = precio - Math.round(Math.round(((precio * descuento) / 100)));
            Integer total = Math.round(Math.round((cantidad * precioConDescuento)));
            facturaDetalleList.get(row).setDescuento(descuento);
            modificarImpuestos(row, total);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, SOLO_DECIMAL, ATENCION, JOptionPane.WARNING_MESSAGE);
        }
    }

    private void modificarImpuestos(int row, int total) {
        switch (facturaDetalleList.get(row).getProducto().getImpuesto()) {
            case Impuesto.EXENTA:
                facturaDetalleList.get(row).setExenta(total);
                facturaDetalleList.get(row).setIva5(0);
                facturaDetalleList.get(row).setIva10(0);
                break;
            case Impuesto.IVA5:
                facturaDetalleList.get(row).setExenta(0);
                facturaDetalleList.get(row).setIva5(total);
                facturaDetalleList.get(row).setIva10(0);
                break;
            case Impuesto.IVA10:
                facturaDetalleList.get(row).setExenta(0);
                facturaDetalleList.get(row).setIva5(0);
                facturaDetalleList.get(row).setIva10(total);
                break;
        }
    }

    public void updateTable() {
        fireTableDataChanged();
    }
}
