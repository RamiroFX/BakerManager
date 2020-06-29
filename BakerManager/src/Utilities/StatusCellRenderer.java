/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import Entities.Caja;
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
public class StatusCellRenderer extends DefaultTableCellRenderer {

    private final int color_column;
    private List<Caja> list;

    public StatusCellRenderer(int column, List<Caja> list) {
        this.color_column = column;
        this.list = list;
    }

    public void setList(List<Caja> list) {
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
        if (list.get(row).getEstado().getId() == Estado.INACTIVO) {
            l.setBackground(new Color(255,102,102));
        }
        return l;

    }

}
