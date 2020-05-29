/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Entities.E_facturaSinPago;
import Entities.E_movimientoContable;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class MovimientoContableTM extends AbstractTableModel {

    private List<E_movimientoContable> list;
    private final String[] colNames = {"Id.", "Nro Factura", "Cliente", "Fecha", "Monto", "Pago", "Saldo"};

    public MovimientoContableTM() {
        this.list = new ArrayList<>();
    }

    public void setList(List<E_movimientoContable> facturaCabeceraList) {
        this.list = facturaCabeceraList;
        updateTable();
    }

    public List<E_movimientoContable> getList() {
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
        E_movimientoContable row = this.list.get(rowIndex);
        switch (colIndex) {
            case 0: {
                switch (row.getTipo()) {
                    case E_movimientoContable.TIPO_COBRO: {
                        return row.getCobro().getId();
                    }
                    case E_movimientoContable.TIPO_COMPRA: {
                        return row.getCompra().getId_cabecera();
                    }
                    case E_movimientoContable.TIPO_PAGO: {
                        return row.getPago().getId();
                    }
                    case E_movimientoContable.TIPO_SALDO_INICIAL: {
                        return row.getCobro().getId();
                    }
                    case E_movimientoContable.TIPO_VENTA: {
                        return row.getVenta().getIdCabecera();
                    }

                }
            }
            case 1: {
                switch (row.getTipo()) {
                    case E_movimientoContable.TIPO_COBRO: {
                        return row.getCobro().getNroRecibo();
                    }
                    case E_movimientoContable.TIPO_COMPRA: {
                        return row.getCompra().getNro_factura();
                    }
                    case E_movimientoContable.TIPO_PAGO: {
                        return row.getPago().getNroRecibo();
                    }
                    case E_movimientoContable.TIPO_SALDO_INICIAL: {
                        return 0;
                    }
                    case E_movimientoContable.TIPO_VENTA: {
                        return row.getVenta().getNroFactura();
                    }
                }
            }
            case 2: {
                switch (row.getTipo()) {
                    case E_movimientoContable.TIPO_COBRO: {
                        return row.getCobro().getCliente().getEntidad();
                    }
                    case E_movimientoContable.TIPO_COMPRA: {
                        return row.getCompra().getProveedor().getEntidad();
                    }
                    case E_movimientoContable.TIPO_PAGO: {
                        return row.getPago().getProveedor().getEntidad();
                    }
                    case E_movimientoContable.TIPO_SALDO_INICIAL: {
                        return row.getClienteSaldoInicial().getEntidad();
                    }
                    case E_movimientoContable.TIPO_VENTA: {
                        return row.getVenta().getClienteEntidad();
                    }
                }
            }
            case 3: {
                switch (row.getTipo()) {
                    case E_movimientoContable.TIPO_COBRO: {
                        return row.getCobro().getFechaOperacion();
                    }
                    case E_movimientoContable.TIPO_COMPRA: {
                        return row.getCompra().getTiempo();
                    }
                    case E_movimientoContable.TIPO_PAGO: {
                        return row.getPago().getFechaOperacion();
                    }
                    case E_movimientoContable.TIPO_SALDO_INICIAL: {
                        return "";
                    }
                    case E_movimientoContable.TIPO_VENTA: {
                        return row.getVenta().getFecha();
                    }
                }
            }
            case 4: {
                switch (row.getTipo()) {
                    case E_movimientoContable.TIPO_COBRO: {
                        return row.getCobro().getDebito();
                    }
                    case E_movimientoContable.TIPO_COMPRA: {
                        return row.getCompra().getTotal();
                    }
                    case E_movimientoContable.TIPO_PAGO: {
                        return row.getPago().getFechaOperacion();
                    }
                    case E_movimientoContable.TIPO_SALDO_INICIAL: {
                        return "";
                    }
                    case E_movimientoContable.TIPO_VENTA: {
                        return row.getVenta().getMonto();
                    }
                }
            }
            case 5: {
                switch (row.getTipo()) {
                    case E_movimientoContable.TIPO_COBRO: {
                        return row.getCobro().getDebito();
                    }
                    case E_movimientoContable.TIPO_COMPRA: {
                        return row.getCompra().getTotal();
                    }
                    case E_movimientoContable.TIPO_PAGO: {
                        return row.getPago().get();
                    }
                    case E_movimientoContable.TIPO_SALDO_INICIAL: {
                        return "";
                    }
                    case E_movimientoContable.TIPO_VENTA: {
                        return row.getVenta().getPago();
                    }
                }
                return row.getPago();
            }
            case 6: {
                return row.getSaldo();
            }
            default: {
                return null;
            }
        }
    }

    public void agregarDatos(E_movimientoContable data) {
        this.list.add(data);
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
