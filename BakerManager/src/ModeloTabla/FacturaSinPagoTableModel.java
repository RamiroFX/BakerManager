/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Entities.E_facturaSinPago;
import Entities.E_movimientoContable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class FacturaSinPagoTableModel extends AbstractTableModel {

    private SimpleDateFormat dateFormater;
    private DecimalFormat decimalFormat;

    private List<E_movimientoContable> list;
    private final String[] colNames = {"Tipo documento", "Nro doc.", "Cliente", "Fecha", "Monto", "Pago", "Saldo"};

    public FacturaSinPagoTableModel() {
        this.dateFormater = new SimpleDateFormat("dd/MM/YYYY hh:mm:ss");
        this.decimalFormat = new DecimalFormat("###,###");
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
        //{"Tipo documento", "Nro Factura", "Cliente", "Fecha", "Monto", "Pago", "Saldo"};
        switch (colIndex) {
            case 0: {
                return row.getTipoDescripcion();
            }
            case 1: {
                switch (row.getTipo()) {
                    case E_movimientoContable.TIPO_VENTA: {
                        return decimalFormat.format(row.getVenta().getNroFactura());
                    }
                    case E_movimientoContable.TIPO_SALDO_INICIAL: {
                        return decimalFormat.format(row.getVenta().getCliente().getIdCliente());
                    }
                }
            }
            case 2: {
                return row.getVenta().getCliente().getEntidad();
            }
            case 3: {
                switch (row.getTipo()) {
                    case E_movimientoContable.TIPO_VENTA: {
                        return dateFormater.format(row.getVenta().getFecha());
                    }
                    case E_movimientoContable.TIPO_SALDO_INICIAL: {
                        return dateFormater.format(row.getVenta().getCliente().getFechaCreacion());
                    }
                }
            }
            case 4: {
                return decimalFormat.format(row.getVenta().getMonto());
            }
            case 5: {
                return decimalFormat.format(row.getVenta().getPago());
            }
            case 6: {
                return decimalFormat.format(row.getVenta().getSaldo());
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
