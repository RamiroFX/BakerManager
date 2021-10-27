/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Entities.E_movimientoProduccion;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class MovimientoProduccionTableModel extends AbstractTableModel {

    private DecimalFormat decimalFormat;
    private SimpleDateFormat dateFormater;
    private List<E_movimientoProduccion> list;
    private final String[] colNames = {"Fecha", "Tipo movimiento", "Entrada", "Salida", "Balance"};

    public MovimientoProduccionTableModel() {
        this.dateFormater = new SimpleDateFormat("dd/MM/YYYY");
        this.decimalFormat = new DecimalFormat("#,##0.##");
        this.list = new ArrayList<>();

    }

    public void setList(List<E_movimientoProduccion> facturaCabeceraList) {
        this.list = facturaCabeceraList;
        if (this.list.isEmpty()) {
            updateTable();
            return;
        }
        /*
        PRIMERA ITERACION PARA PREPARAR BASE DEL BALANCE
         */
        E_movimientoProduccion firsRow = list.get(0);
        switch (firsRow.getTipo()) {
            case E_movimientoProduccion.TIPO_PRODUCCION: {
                firsRow.setEntrada(firsRow.getProduccionDetalle().getCantidad());
                firsRow.setBalance(firsRow.getEntrada());
                break;
            }
            case E_movimientoProduccion.TIPO_COMPRA: {
                firsRow.setEntrada(firsRow.getCompraDetalle().getCantidad());
                firsRow.setBalance(firsRow.getEntrada());
                break;
            }
            case E_movimientoProduccion.TIPO_VENTA: {
                firsRow.setSalida(firsRow.getVentaDetalle().getCantidad());
                firsRow.setBalance(-firsRow.getSalida());
                break;
            }
            case E_movimientoProduccion.TIPO_DESPERDICIO: {
                firsRow.setSalida(firsRow.getDesperdicioDetalle().getCantidad());
                firsRow.setBalance(-firsRow.getSalida());
                break;
            }
            case E_movimientoProduccion.TIPO_INVENTARIO: {
                if (firsRow.getInventarioDetalle().getCantidadNueva() > 0) {
                    firsRow.setSalida(firsRow.getInventarioDetalle().getCantidadNueva());
                    firsRow.setBalance(-firsRow.getSalida());
                } else {
                    firsRow.setSalida(firsRow.getInventarioDetalle().getCantidadNueva());
                    firsRow.setBalance(firsRow.getSalida());
                }
                break;
            }
            case E_movimientoProduccion.TIPO_UTILIZACION: {
                firsRow.setSalida(firsRow.getProduccionFilmBaja().getPesoUtilizado());
                firsRow.setBalance(-firsRow.getSalida());
                break;
            }
        }
        /*
        COMIENZO DE ITERACIONES
         */
        for (int i = 1; i < list.size(); i++) {
            E_movimientoProduccion movAnt = list.get(i - 1);
            E_movimientoProduccion mov = list.get(i);
            switch (mov.getTipo()) {
                case E_movimientoProduccion.TIPO_PRODUCCION: {
                    mov.setEntrada(mov.getProduccionDetalle().getCantidad());
                    mov.setBalance(mov.getEntrada() + movAnt.getBalance());
                    break;
                }
                case E_movimientoProduccion.TIPO_COMPRA: {
                    mov.setEntrada(mov.getCompraDetalle().getCantidad());
                    mov.setBalance(-mov.getEntrada() + movAnt.getBalance());
                    break;
                }
                case E_movimientoProduccion.TIPO_VENTA: {
                    mov.setSalida(mov.getVentaDetalle().getCantidad());
                    mov.setBalance(-mov.getSalida() + movAnt.getBalance());
                    break;
                }
                case E_movimientoProduccion.TIPO_DESPERDICIO: {
                    mov.setSalida(mov.getDesperdicioDetalle().getCantidad());
                    mov.setBalance(-mov.getSalida() + movAnt.getBalance());
                    break;
                }
                case E_movimientoProduccion.TIPO_INVENTARIO: {
                    if (mov.getInventarioDetalle().getCantidadNueva() > 0) {
                        mov.setEntrada(mov.getInventarioDetalle().getCantidadNueva());
                        mov.setBalance(mov.getEntrada()+ movAnt.getBalance());
                    } else {
                        mov.setSalida(mov.getInventarioDetalle().getCantidadNueva());
                        mov.setBalance(-mov.getSalida() + movAnt.getBalance());
                    }
                    break;
                }
                case E_movimientoProduccion.TIPO_UTILIZACION: {
                    mov.setSalida(mov.getProduccionFilmBaja().getPesoUtilizado());
                    mov.setBalance(-mov.getSalida() + movAnt.getBalance());
                    break;
                }
            }
        }
        updateTable();
    }

    public List<E_movimientoProduccion> getList() {
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
        E_movimientoProduccion row = this.list.get(rowIndex);
        switch (colIndex) {
            case 0: {
                return dateFormater.format(row.getMovFecha());
            }
            case 1: {
                return row.getMovDescripcion();
            }
            case 2: {
                return decimalFormat.format(row.getEntrada());
            }
            case 3: {
                return decimalFormat.format(row.getSalida());
            }
            case 4: {
                return decimalFormat.format(row.getBalance());

            }
            default: {
                return null;
            }
        }
    }

    public void agregarDatos(E_movimientoProduccion data) {
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
