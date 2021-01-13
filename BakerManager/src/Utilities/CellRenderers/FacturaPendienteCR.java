/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities.CellRenderers;

import Entities.E_NotaCreditoCabecera;
import Entities.E_movimientoContable;
import Entities.Estado;
import java.awt.Color;
import java.awt.Component;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Ramiro Ferreira
 */
public class FacturaPendienteCR extends DefaultTableCellRenderer {

    private List<E_movimientoContable> list;

    public FacturaPendienteCR(List<E_movimientoContable> list) {
        this.list = list;
    }

    public void setList(List<E_movimientoContable> list) {
        this.list = list;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused, int row, int column) {        
        setBackground(Color.white);//color de fondo
        table.setForeground(Color.black);//color de texto
        JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, selected, focused, row, column);
        if (list.isEmpty()) {
            return l;
        }
        E_movimientoContable fsp = list.get(row);
        if (fsp.getVenta().estaVencida()) {
            l.setBackground(new Color(255, 102, 102));
        }
        return l;

    }

}
