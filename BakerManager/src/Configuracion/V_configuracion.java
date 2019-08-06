/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuracion;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

/**
 *
 * @author Ramiro
 */
public class V_configuracion extends javax.swing.JDialog {

    public javax.swing.JButton jbAceptar, jbCancelar, jbAgregarCampo, jbModificarCampo, jbQuitarCampo;
    public javax.swing.JPanel jpSouth, jpTicket, jpFactura;
    public javax.swing.JTabbedPane jtpCenter;
    public JTable jtTicket, jtFactura;

    public V_configuracion(JFrame frame) {
        super(frame, true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Configuración de impresiones");
        setAlwaysOnTop(false);
        setName("configuracion_impresiones");
        setPreferredSize(new java.awt.Dimension(800, 600));
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        initPaneRol();
        initPanelSouth();
        jtpCenter = new JTabbedPane();
        jtpCenter.add("Ticket", jpTicket);
        jtpCenter.add("Factura", jpFactura);
        getContentPane().add(jtpCenter, java.awt.BorderLayout.CENTER);
        getContentPane().add(jpSouth, java.awt.BorderLayout.SOUTH);
        pack();
    }

    private void initPaneRol() {
        //Panel
        jpTicket = new javax.swing.JPanel(new java.awt.BorderLayout());
        jpFactura = new javax.swing.JPanel(new java.awt.BorderLayout());

        jtTicket = new JTable();
        jtTicket.getTableHeader().setReorderingAllowed(false);
        jtFactura = new JTable();
        jtFactura.getTableHeader().setReorderingAllowed(false);
        JScrollPane jspTicket = new JScrollPane(jtTicket);
        JScrollPane jspFactura = new JScrollPane(jtFactura);
        jpTicket.add(jspTicket, BorderLayout.CENTER);
        jpFactura.add(jspFactura, BorderLayout.CENTER);
    }

    private void initPanelSouth() {
        jpSouth = new javax.swing.JPanel(new java.awt.GridLayout(2, 1));
        //jpSouth.setBorder(new javax.swing.border.EtchedBorder());
        jbAceptar = new javax.swing.JButton("Aceptar");
        jbCancelar = new javax.swing.JButton("Cancelar");
        jbAgregarCampo = new javax.swing.JButton("Agregar");
        jbModificarCampo = new javax.swing.JButton("Modificar");
        jbQuitarCampo = new javax.swing.JButton("Quitar");
        javax.swing.JPanel jpSouthButtons = new javax.swing.JPanel();
        jpSouthButtons.add(jbAgregarCampo);
        jpSouthButtons.add(jbModificarCampo);
        jpSouthButtons.add(jbQuitarCampo);
        javax.swing.JPanel jpBotones = new javax.swing.JPanel();
        jpBotones.setBorder(new javax.swing.border.EtchedBorder());
        jpBotones.add(jbAceptar);
        jpBotones.add(jbCancelar);
        jpSouth.add(jpSouthButtons);
        jpSouth.add(jpBotones);
    }
}