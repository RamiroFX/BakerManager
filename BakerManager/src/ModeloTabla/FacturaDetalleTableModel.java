/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Entities.E_impuesto;
import Interface.InterfaceFacturaDetalle;
import Entities.M_facturaDetalle;
import Parametros.Impuesto;
import java.text.DecimalFormat;
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
    private DecimalFormat integerFormat, decimalFormat;
    private final String[] colNames = {"Cod.", "Cantidad", "Descripcion", "Precio", "Descuento", "Exenta", "IVA 5%", "IVA 10%", "Observacion"};

    private InterfaceFacturaDetalle interfaceFacturaDetalle;

    public FacturaDetalleTableModel(InterfaceFacturaDetalle interfaceFacturaDetalle) {
        this.facturaDetalleList = new ArrayList<>();
        this.interfaceFacturaDetalle = interfaceFacturaDetalle;
        this.integerFormat = new DecimalFormat("###,###");
        this.decimalFormat = new DecimalFormat("#,##0.##");
    }

    public FacturaDetalleTableModel() {
        this.facturaDetalleList = new ArrayList<>();
        this.integerFormat = new DecimalFormat("###,###");
        this.decimalFormat = new DecimalFormat("#,##0.##");
    }

    public void setInterface(InterfaceFacturaDetalle interfaceFacturaDetalle) {
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
                return fd.getProducto().getCodigo();
            }
            case 1: {
                return fd.getCantidad();
            }
            case 2: {
                return fd.getProducto().getDescripcion();
            }
            case 3: {
                return integerFormat.format(fd.getPrecio());
            }
            case 4: {
                return fd.getDescuento();
            }
            case 5: {
                if (fd.getProducto().getIdImpuesto() == E_impuesto.EXENTA) {
                    return integerFormat.format(fd.calcularSubTotal());
                }
                return 0;
            }
            case 6: {
                if (fd.getProducto().getIdImpuesto() == E_impuesto.IVA5) {
                    return integerFormat.format(fd.calcularSubTotal());
                }
                return 0;
            }
            case 7: {
                if (fd.getProducto().getIdImpuesto() == E_impuesto.IVA10) {
                    return integerFormat.format(fd.calcularSubTotal());
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
            double precio = facturaDetalleList.get(row).getPrecio();
            double precioConDescuento = precio - Math.round(Math.round(((precio * descuento) / 100)));
            Integer total = Math.round(Math.round((cantidad * precioConDescuento)));
            facturaDetalleList.get(row).setCantidad(cantidad);
            modificarImpuestos(row, total);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, SOLO_DECIMAL, ATENCION, JOptionPane.WARNING_MESSAGE);
        }
    }

    private void modificarPrecioFila(Object aValue, int row) {
        try {
            double precio = (Integer.valueOf(aValue.toString()));
            Double cantidad = facturaDetalleList.get(row).getCantidad();
            Double descuento = facturaDetalleList.get(row).getDescuento();
            double precioConDescuento = precio - Math.round(Math.round(((precio * descuento) / 100)));
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
            double precio = facturaDetalleList.get(row).getPrecio();
            double precioConDescuento = precio - Math.round(Math.round(((precio * descuento) / 100)));
            Integer total = Math.round(Math.round((cantidad * precioConDescuento)));
            facturaDetalleList.get(row).setDescuento(descuento);
            modificarImpuestos(row, total);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, SOLO_DECIMAL, ATENCION, JOptionPane.WARNING_MESSAGE);
        }
    }

    private void modificarImpuestos(int row, double total) {
        switch (facturaDetalleList.get(row).getProducto().getImpuesto()) {
            case Impuesto.EXENTA:
                facturaDetalleList.get(row).setExenta(total);
                facturaDetalleList.get(row).setIva5(0.0);
                facturaDetalleList.get(row).setIva10(0.0);
                break;
            case Impuesto.IVA5:
                facturaDetalleList.get(row).setExenta(0.0);
                facturaDetalleList.get(row).setIva5(total);
                facturaDetalleList.get(row).setIva10(0.0);
                break;
            case Impuesto.IVA10:
                facturaDetalleList.get(row).setExenta(0.0);
                facturaDetalleList.get(row).setIva5(0.0);
                facturaDetalleList.get(row).setIva10(total);
                break;
        }
    }

    public void modificarDetalle(int index, double cantidad, double descuento, double precio, String obs) {
        this.facturaDetalleList.get(index).setCantidad(cantidad);
        this.facturaDetalleList.get(index).setDescuento(descuento);
        this.facturaDetalleList.get(index).setPrecio(precio);
        this.facturaDetalleList.get(index).setObservacion(obs);
        //fireTableCellUpdated(index, 0);
        fireTableDataChanged();
    }

    public void updateTable() {
        fireTableDataChanged();
    }
}
