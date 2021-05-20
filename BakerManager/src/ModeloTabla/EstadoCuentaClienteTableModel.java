/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

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

    public EstadoCuentaClienteTableModel() {
        this.dateFormater = new SimpleDateFormat("dd/MM/YYYY");
        this.decimalFormat = new DecimalFormat("#,##0.##");
        this.list = new ArrayList<>();

    }

    public void setList(List<E_movimientoContable> facturaCabeceraList) {
        this.list = facturaCabeceraList;
        /*
        PRIMERA ITERACION PARA PREPARAR BASE DEL BALANCE
         */
        E_movimientoContable firsRow = list.get(0);
        switch (firsRow.getTipo()) {
            case E_movimientoContable.TIPO_SALDO_INICIAL: {
                firsRow.setDebe(firsRow.getClienteSaldoInicial().getSaldoInicial());
                firsRow.setBalance(firsRow.getClienteSaldoInicial().getSaldoInicial());
                break;
            }
            case E_movimientoContable.TIPO_VENTA: {
                firsRow.setDebe(firsRow.getVenta().getMonto());
                firsRow.setBalance(firsRow.getVenta().getMonto());
                break;
            }
            case E_movimientoContable.TIPO_COBRO: {
                firsRow.setHaber(firsRow.getCobro().getMonto());
                firsRow.setBalance(-firsRow.getCobro().getMonto());
                break;
            }
            case E_movimientoContable.TIPO_NOTA_CREDITO: {
                firsRow.setHaber(firsRow.getNotaCredito().getTotal());
                firsRow.setBalance(-firsRow.getNotaCredito().getTotal());
                break;
            }
            case E_movimientoContable.TIPO_RETENCION_VENTA: {
                firsRow.setHaber(firsRow.getRetencionVenta().getMonto());
                firsRow.setBalance(-firsRow.getRetencionVenta().getMonto());
                break;
            }
        }
        /*
        COMIENZO DE ITERACIONES
         */
        for (int i = 1; i < list.size(); i++) {
            E_movimientoContable movAnt = list.get(i - 1);
            E_movimientoContable mov = list.get(i);
            switch (mov.getTipo()) {
                case E_movimientoContable.TIPO_SALDO_INICIAL: {
                    mov.setDebe(mov.getClienteSaldoInicial().getSaldoInicial());
                    mov.setBalance(mov.getDebe() + movAnt.getBalance());
                    break;
                }
                case E_movimientoContable.TIPO_VENTA: {
                    mov.setDebe(mov.getVenta().getMonto());
                    mov.setBalance(mov.getDebe() + movAnt.getBalance());
                    break;
                }
                case E_movimientoContable.TIPO_COBRO: {
                    mov.setHaber(mov.getCobro().getMonto());
                    mov.setBalance(-mov.getHaber() + movAnt.getBalance());
                    break;
                }
                case E_movimientoContable.TIPO_NOTA_CREDITO: {
                    mov.setHaber(mov.getNotaCredito().getTotal());
                    mov.setBalance(-mov.getHaber() + movAnt.getBalance());
                    break;
                }
                case E_movimientoContable.TIPO_RETENCION_VENTA: {
                    mov.setHaber(mov.getRetencionVenta().getMonto());
                    mov.setBalance(-mov.getHaber() + movAnt.getBalance());
                    break;
                }
            }
        }
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
                return dateFormater.format(row.getMovFecha());
            }
            case 1: {
                return row.getMovDescripcion();
            }
            case 2: {
                return decimalFormat.format(row.getDebe());
            }
            case 3: {
                return decimalFormat.format(row.getHaber());
            }
            case 4: {
                return decimalFormat.format(row.getBalance());

            }
            default: {
                return null;
            }
        }
    }

    public void agregarDatos(E_movimientoContable data) {
        this.list.add(data);
        updateTable();
    }

    public void quitarDatos(int index) {
        this.list.remove(index);
        updateTable();
    }

    public void vaciarLista() {
        this.list.clear();
        updateTable();
    }

    public void updateTable() {
        fireTableDataChanged();
    }
}
