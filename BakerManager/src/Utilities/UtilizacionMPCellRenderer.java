/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import Entities.Estado;
import ModeloTabla.UtilizacionMPCabeceraTableModel;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Ramiro Ferreira
 */
public class UtilizacionMPCellRenderer extends DefaultTableCellRenderer {

    private final int color_column;

    public UtilizacionMPCellRenderer(int column) {
        this.color_column = column;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused, int row, int column) {
        setBackground(Color.white);//color de fondo
        table.setForeground(Color.black);//color de texto
        JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, selected, focused, row, color_column);
        UtilizacionMPCabeceraTableModel tm = (UtilizacionMPCabeceraTableModel) table.getModel();
        if (tm.getList().get(row).getEstado().getId() == Estado.INACTIVO) {
            l.setBackground(new Color(255, 109, 109));
        }
        return l;

    }

}
