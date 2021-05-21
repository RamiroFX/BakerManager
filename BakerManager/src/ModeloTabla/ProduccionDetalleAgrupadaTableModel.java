/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import DB.DB_Produccion;
import Entities.E_produccionDetallePlus;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class ProduccionDetalleAgrupadaTableModel extends AbstractTableModel {

    public static final int TIPO_FECHA_MES = 1, TIPO_FECHA_DIA = 2;
    public static final int SIMPLE = 1, DETALLE = 2, COMPLETA = 3, SUPER = 4;
    private List<E_produccionDetallePlus> produccionList;
    private String[] colNames;
    private DecimalFormat decimalFormat;
    private int tipoFormato;
    private int tipoFecha;
    private int cantTiempo;
    private boolean esHistorico;
    private Date fechaLimite;

    public ProduccionDetalleAgrupadaTableModel(int tipo) {
        this.tipoFormato = tipo;
        this.decimalFormat = new DecimalFormat("#,##0.##");
        this.produccionList = new ArrayList<>();
        switch (tipo) {
            case SIMPLE: {
                this.colNames = new String[]{"Código", "Descripcion", "Cant. producida"};
                break;
            }
            case DETALLE: {
                this.colNames = new String[]{"Código", "Descripción", "Cant. producida", "Cant. actual"};
                break;
            }
            case COMPLETA: {
                this.colNames = new String[]{"Código", "Descripción", "Cant. producida", "Cant. vendida", "Cant. actual"};
                break;
            }
            case SUPER: {
                this.colNames = new String[]{"Código", "Descripción", "Saldo", "Cant. producida", "Cant. vendida", "Balance", "Cant. actual"};
                break;
            }
        }
    }

    public void setRangoSaldoAnterior(int tipoFecha, int cant, boolean esHistorico) {
        this.tipoFecha = tipoFecha;
        this.esHistorico = esHistorico;
        this.cantTiempo = cant;
    }

    public Date getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(Date fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public List<E_produccionDetallePlus> getList() {
        return produccionList;
    }

    public void setList(List<E_produccionDetallePlus> produccionList) {
        this.produccionList = produccionList;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar fechaInicio = Calendar.getInstance();
        switch (tipoFecha) {
            case TIPO_FECHA_DIA: {
                fechaInicio.setTime(fechaLimite);
                fechaInicio.add(Calendar.DATE, cantTiempo * -1);
                break;
            }
            case TIPO_FECHA_MES: {
                fechaInicio.setTime(fechaLimite);
                fechaInicio.add(Calendar.MONTH, cantTiempo * -1);
                break;
            }
        }
        System.out.println("ModeloTabla.ProduccionDetalleAgrupadaTableModel.setList()");
        System.out.println("fechaInicio: " + sdf.format(fechaInicio.getTime()));
        System.out.println("fechaLimite: " + sdf.format(fechaLimite));
        for (E_produccionDetallePlus aProd : produccionList) {
            aProd.setBalanceAnterior(DB_Produccion.consultarBalanceProduccion(fechaInicio.getTime(), getFechaLimite(), aProd.getProducto().getId(), esHistorico));
        }
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
        return this.produccionList.size();
    }

    @Override
    public int getColumnCount() {
        return this.colNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int colIndex) {
        switch (this.tipoFormato) {
            case COMPLETA: {
                return getValueCompleteMode(rowIndex, colIndex);
            }
            case SUPER: {
                return getValueSuperMode(rowIndex, colIndex);
            }
            default: {
                return null;
            }
        }
    }

    public void agregarDetalle(E_produccionDetallePlus produccion) {
        this.produccionList.add(produccion);
        updateTable();
    }

    public void modificarCantidadDetalle(int index, double cantidad) {
        this.produccionList.get(index).setCantidad(cantidad);
        fireTableCellUpdated(index, 0);
        updateTable();
    }

    public void quitarDetalle(int index) {
        this.produccionList.remove(index);
        updateTable();
    }

    public void vaciarLista() {
        this.produccionList.clear();
        updateTable();
    }

    public void updateTable() {
        fireTableDataChanged();
    }

    public Object getValueCompleteMode(int rowIndex, int colIndex) {
        E_produccionDetallePlus produccion = this.produccionList.get(rowIndex);
        switch (colIndex) {
            case 0: {
                return produccion.getProducto().getCodigo();
            }
            case 1: {
                return produccion.getProducto().getDescripcion();
            }
            case 2: {
                return decimalFormat.format(produccion.getCantidad());
            }
            case 3: {
                return decimalFormat.format(produccion.getCantidad());
            }
            case 4: {
                return decimalFormat.format(produccion.getCantidadVendida());
            }
            case 5: {
                return decimalFormat.format(produccion.getCantidad() - produccion.getCantidadVendida());
            }
            case 6: {
                return decimalFormat.format(produccion.getProducto().getCantActual());
            }
            default: {
                return null;
            }
        }
    }

    public Object getValueSuperMode(int rowIndex, int colIndex) {
        E_produccionDetallePlus produccion = this.produccionList.get(rowIndex);
        switch (colIndex) {
            case 0: {
                return produccion.getProducto().getCodigo();
            }
            case 1: {
                return produccion.getProducto().getDescripcion();
            }
            case 2: {
                return decimalFormat.format(produccion.getBalanceAnterior());
            }
            case 3: {
                return decimalFormat.format(produccion.getCantidad());
            }
            case 4: {
                return decimalFormat.format(produccion.getCantidadVendida());
            }
            case 5: {
                return decimalFormat.format((produccion.getBalanceAnterior() + produccion.getCantidad()) - produccion.getCantidadVendida());
            }
            case 6: {
                return decimalFormat.format(produccion.getProducto().getCantActual());
            }
            default: {
                return null;
            }
        }
    }
}
