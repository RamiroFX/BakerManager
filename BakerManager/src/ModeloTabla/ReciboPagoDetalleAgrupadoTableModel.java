/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Entities.E_formaPago;
import Entities.E_reciboPagoDetalle;
import Entities.E_tipoCheque;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class ReciboPagoDetalleAgrupadoTableModel extends AbstractTableModel {

    private List<E_reciboPagoDetalle> list;
    private final String[] colNames = {"Monto a pagar", "Banco"};

    public ReciboPagoDetalleAgrupadoTableModel() {
        this.list = new ArrayList<>();
    }

    public void setList(List<E_reciboPagoDetalle> list) {
        this.list = list;
        updateTable();
    }

    public List<E_reciboPagoDetalle> getList() {
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
        E_reciboPagoDetalle row = this.list.get(rowIndex);
        switch (colIndex) {
            case 0: {
                return (int) row.getMonto();
            }
            case 1: {
                if (row.getBanco() != null) {
                    if (row.getBanco().getDescripcion() != null) {
                        return row.getBanco().getDescripcion();
                    } else {
                        return "";
                    }
                } else {
                    return "";
                }
            }
            default: {
                return null;
            }
        }
    }

    public void agregarDatos(E_reciboPagoDetalle data) {
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

    public void modificarDatos(int index, E_reciboPagoDetalle data) {
        this.list.get(index).setMonto(data.getMonto());
        switch (data.getFormaPago().getId()) {
            case E_formaPago.CHEQUE: {
                this.list.get(index).setBanco(data.getBanco());
                this.list.get(index).setFechaCheque(data.getFechaCheque());
                if (data.getTipoCheque().getId() == E_tipoCheque.DIFERIDO) {
                    this.list.get(index).setFechaDiferidaCheque(data.getFechaDiferidaCheque());
                } else {
                    this.list.get(index).setFechaDiferidaCheque(null);
                }
                this.list.get(index).setNroCheque(data.getNroCheque());
                break;
            }
        }
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
