/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Ramiro Ferreira
 */
public class MyColorCellRenderer extends DefaultTableCellRenderer {
    private final int color_column;

    public MyColorCellRenderer(int column) {
        this.color_column = column;
    }    
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused, int row, int column) {
        setBackground(Color.white);//color de fondo
        table.setForeground(Color.black);//color de texto
        JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, selected, focused, row, color_column);
        if (table.getValueAt(row, column).equals("Cancelado")) {
            l.setBackground(Color.RED.brighter());
        } else if (table.getValueAt(row, column).equals("Pendiente")) {
            l.setBackground(Color.YELLOW.brighter());
        } else if (table.getValueAt(row, column).equals("Entregado")) {
            l.setBackground(Color.GREEN.brighter());
        }
        return l;

    }

}
