/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Entities.E_cuentaCorrienteDetalle;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class CtaCteDetalleTableModel extends AbstractTableModel {

    private List<E_cuentaCorrienteDetalle> list;
    private final String[] colNames2 = {"Monto a pagar", "Id venta", "Nro Factura"};
    private final String[] colNames = {"Monto a pagar", "Id venta", "Nro Factura", "Nro Cheque", "Banco", "Fecha cheque", "Fecha diferida"};

    public CtaCteDetalleTableModel() {
        this.list = new ArrayList<>();
    }

    public void setList(List<E_cuentaCorrienteDetalle> facturaCabeceraList) {
        this.list = facturaCabeceraList;
        updateTable();
    }

    public List<E_cuentaCorrienteDetalle> getList() {
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
        E_cuentaCorrienteDetalle row = this.list.get(rowIndex);
        //{"Nro Cheque", "Banco", "Fecha cheque", "Fecha diferida"};
        switch (colIndex) {
            case 0: {
                return row.getMonto();
            }
            case 1: {
                return row.getIdFacturaCabecera();
            }
            case 2: {
                return row.getNroFactura();
            }
            case 3: {
                if (row.getNroCheque() > 0) {
                    return row.getNroCheque();
                } else {
                    return "";
                }
            }
            case 4: {
                if (row.getBanco() != null) {
                    return row.getBanco().getDescripcion();
                } else {
                    return "";
                }
            }
            case 5: {
                if (row.getFechaCheque() != null) {
                    return row.getFechaCheque();
                } else {
                    return "";
                }
            }
            case 6: {
                if (row.getFechaDiferidaCheque() != null) {
                    return row.getFechaCheque();
                } else {
                    return "";
                }
            }
            default: {
                return null;
            }
        }
    }

    public void agregarDatos(E_cuentaCorrienteDetalle data) {
        this.list.add(data);
        fireTableDataChanged();
    }

    public void modificarMontoPagar(int pago, int row) {
        try {
            list.get(row).setMonto(Double.valueOf(pago + ""));
            fireTableDataChanged();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al modificar monto a pagar", "Atención", JOptionPane.WARNING_MESSAGE);
        }
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
