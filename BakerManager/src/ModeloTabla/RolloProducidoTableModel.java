/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Entities.E_produccionFilm;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class RolloProducidoTableModel extends AbstractTableModel {

    List<E_produccionFilm> list;
    private String[] colNames = {"OT", "Fecha Producción", "Nro. Film", "Código", "Descripción", "Cono", "Medida", "Micron", "Peso producido", "Peso utilizado", "Peso disponible"};

    public RolloProducidoTableModel() {
        list = new ArrayList<>();
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
//"OT","Fecha Producción","Nro. Film", "Peso", "Descripción", "Cono", "Medida", "Micron", "Tipo MP"
        E_produccionFilm produccion = this.list.get(rowIndex);
        switch (colIndex) {
            case 0: {
                return produccion.getOrdenTrabajoCabecera();
            }
            case 1: {
                return produccion.getFechaCreacion();
            }
            case 2: {
                return produccion.getNroFilm();
            }
            case 3: {
                return produccion.getProducto().getCodigo();
            }
            case 4: {
                return produccion.getProducto().getDescripcion();
            }
            case 5: {
                return produccion.getCono();
            }
            case 6: {
                return produccion.getMedida();
            }
            case 7: {
                return produccion.getMicron();
            }
            case 8: {
                return produccion.getPeso();
            }
            case 9: {
                return produccion.getPesoUtilizado();
            }
            case 10: {
                return produccion.getPesoActual();
            }
            default: {
                return null;
            }
        }
    }

    public void setList(List<E_produccionFilm> productoList) {
        this.list = productoList;
        updateTable();
    }

    public List<E_produccionFilm> getList() {
        return list;
    }

    public void agregarDatos(E_produccionFilm film) {
        this.list.add(film);
        fireTableDataChanged();
    }

    public void modificarDatos(int index, E_produccionFilm pf) {
        this.list.get(index).setCono(pf.getCono());
        this.list.get(index).setEstado(pf.getEstado());
        this.list.get(index).setMedida(pf.getMedida());
        this.list.get(index).setMicron(pf.getMicron());
        this.list.get(index).setNroFilm(pf.getNroFilm());
        this.list.get(index).setPeso(pf.getPeso());
        this.list.get(index).setProducto(pf.getProducto());
        this.list.get(index).setProductoClasificacion(pf.getProductoClasificacion());
        fireTableCellUpdated(index, 0);
        fireTableCellUpdated(index, 1);
        fireTableCellUpdated(index, 2);
        fireTableCellUpdated(index, 3);
        fireTableCellUpdated(index, 4);
        fireTableCellUpdated(index, 5);
        fireTableCellUpdated(index, 6);
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
