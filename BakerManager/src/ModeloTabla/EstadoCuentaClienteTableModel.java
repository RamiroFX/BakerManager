/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Entities.E_cuentaCorrienteCabecera;
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
public class EstadoCuentaClienteTableModel extends AbstractTableModel {

    private DecimalFormat decimalFormat;
    private SimpleDateFormat dateFormater;
    private List<E_movimientoContable> list;
    private final String[] colNames = {"Fecha", "Documento", "Debe", "Haber", "Saldo"};
    private int debe = 0;
    private int haber = 0;
    private int saldo = 0;

    public EstadoCuentaClienteTableModel() {
        this.dateFormater = new SimpleDateFormat("dd/MM/YYYY");
        this.decimalFormat = new DecimalFormat("#,##0.##");
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
        //"Fecha", "Documento", "Debe", "Haber", "Saldo"
        switch (colIndex) {
            case 0: {
                switch (row.getTipo()) {
                    case E_movimientoContable.TIPO_SALDO_INICIAL: {
                        return dateFormater.format(row.getFechaSaldoInicial());
                    }
                    case E_movimientoContable.TIPO_VENTA: {
                        return dateFormater.format(row.getVenta().getFecha());
                    }
                    case E_movimientoContable.TIPO_COBRO: {
                        return dateFormater.format(row.getCobro().getCuentaCorrienteCabecera().getFechaPago());
                    }
                    case E_movimientoContable.TIPO_NOTA_CREDITO: {
                        return dateFormater.format(row.getNotaCredito().getTiempo());
                    }
                    case E_movimientoContable.TIPO_RETENCION_VENTA: {
                        return dateFormater.format(row.getNotaCredito().getTiempo());
                    }
                }
            }
            case 1: {
                switch (row.getTipo()) {
                    case E_movimientoContable.TIPO_SALDO_INICIAL: {
                        return row.getTipoDescripcion();
                    }
                    case E_movimientoContable.TIPO_VENTA: {
                        int nroFactura = row.getVenta().getNroFactura();
                        String sNroFactura = decimalFormat.format(nroFactura);
                        return row.getTipoDescripcion() + " N° " + sNroFactura;
                    }
                    case E_movimientoContable.TIPO_COBRO: {
                        haber = haber + (int) row.getCobro().getMonto();
                        saldo = debe - haber;
                        int nroFactura = row.getCobro().getFacturaVenta().getNroFactura();
                        int nroRecibo = row.getCobro().getCuentaCorrienteCabecera().getNroRecibo();
                        String sNroFactura = decimalFormat.format(nroFactura);
                        String sNroRecibo = decimalFormat.format(nroRecibo);
                        return row.getTipoDescripcion() + " N° " + sNroRecibo + " (Fact. N° " + sNroFactura + ")";
                    }
                    case E_movimientoContable.TIPO_NOTA_CREDITO: {
                        int nroFactura = row.getNotaCredito().getFacturaCabecera().getNroFactura();
                        int nroNotaCredito = row.getNotaCredito().getNroNotaCredito();
                        String sNroFactura = decimalFormat.format(nroFactura);
                        String sNroNotaCredito = decimalFormat.format(nroNotaCredito);
                        return row.getTipoDescripcion() + " N° " + sNroNotaCredito + " (Fact. N° " + sNroFactura + ")";
                    }
                    case E_movimientoContable.TIPO_RETENCION_VENTA: {
                        int nroRetencion = row.getRetencionVenta().getNroRetencion();
                        String sNroFactura = decimalFormat.format(row.getRetencionVenta().getVenta().getNroFactura());
                        String sNroRetencion = decimalFormat.format(nroRetencion);
                        return row.getTipoDescripcion() + " N° " + sNroRetencion + " (Fact. N° " + sNroFactura + ")";
                    }
                }
            }
            case 4: {
                //return dateFormater.format(r);
            }
            case 5: {
                //return decimalFormat.format(row.getDebito());
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
