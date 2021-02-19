/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Entities.M_egreso_cabecera;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class EgresoCabeceraTableModel extends AbstractTableModel {

    public static final int SIMPLE = 1, MEDIANO = 2, COMPLETO = 3;
    private DecimalFormat decimalFormat;
    private SimpleDateFormat dateFormater;
    private NumberFormat nfSmall, nfLarge;
    private List<M_egreso_cabecera> list;
    private final String[] colNames = {"Id.", "Nro Factura", "Proveedor", "Funcionario", "Tiempo", "Total", "Cond. compra"};
    private int formato;

    public EgresoCabeceraTableModel(int formato) {
        this.formato = formato;
        this.dateFormater = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
        this.decimalFormat = new DecimalFormat("#,##0.##");
        this.nfSmall = new DecimalFormat("000");
        this.nfLarge = new DecimalFormat("0000000");
        this.list = new ArrayList<>();
    }

    public void setList(List<M_egreso_cabecera> list) {
        this.list = list;
        updateTable();
    }

    public List<M_egreso_cabecera> getList() {
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
        M_egreso_cabecera ec = this.list.get(rowIndex);
        switch (colIndex) {
            case 0: {
                return decimalFormat.format(ec.getId_cabecera());
            }
            case 1: {
                switch (formato) {
                    case SIMPLE: {
                        return decimalFormat.format(ec.getNro_factura());
                    }
                    case MEDIANO: {
                        String sucursal = this.nfSmall.format(ec.getTimbrado().getNroSucursal());
                        String ptva = this.nfSmall.format(ec.getTimbrado().getNroPuntoVenta());
                        String nroFactura = decimalFormat.format(ec.getNro_factura());
                        String value = sucursal + "-" + ptva + "-" + nroFactura;
                        return value;
                    }
                    case COMPLETO: {
                        String timbrado = this.nfLarge.format(ec.getTimbrado().getNroTimbrado());
                        String sucursal = this.nfSmall.format(ec.getTimbrado().getNroSucursal());
                        String ptva = this.nfSmall.format(ec.getTimbrado().getNroPuntoVenta());
                        String nroFactura = decimalFormat.format(ec.getNro_factura());
                        String value = timbrado + "-" + sucursal + "-" + ptva + "-" + nroFactura;
                        return value;
                    }
                }
            }
            case 2: {
                return ec.getProveedor().getEntidad();
            }
            case 3: {
                return ec.getFuncionario().getAlias();
            }
            case 4: {
                return dateFormater.format(ec.getTiempo());
            }
            case 5: {
                return decimalFormat.format(ec.getTotal());
            }
            case 6: {
                return ec.getCondCompra().getDescripcion();
            }
            default: {
                return null;
            }
        }
    }

    public void agregarDatos(M_egreso_cabecera fc) {
        this.list.add(fc);
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
