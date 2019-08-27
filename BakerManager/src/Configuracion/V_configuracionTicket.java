/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuracion;

import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author Ramiro Ferreira
 */
public class V_configuracionTicket extends javax.swing.JDialog {

    public javax.swing.JButton jbCancelar, jbImprimirPaginaPrueba;
    public javax.swing.JPanel jpSouth, jpTicket, jpImpresora;
    public javax.swing.JTabbedPane jtpCenter;
    //Variables de impresora
    public JTextField jtfNombreImpresora;
    public JTextArea jtaCabecera, jtaPie;
    public javax.swing.JButton jbGuardar;

    public V_configuracionTicket(JFrame frame) {
        super(frame, true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Configuración de ticket");
        setAlwaysOnTop(false);
        setName("configuracion_ticket");
        setPreferredSize(new java.awt.Dimension(1024, 768));
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        initPanelNorth();
        initPanelSouth();
        initPrinterPanel();
        jtpCenter = new JTabbedPane();
        jtpCenter.add("Parametros", jpTicket);
        jtpCenter.add("Impresora", jpImpresora);
        getContentPane().add(jtpCenter, java.awt.BorderLayout.CENTER);
        getContentPane().add(jpSouth, java.awt.BorderLayout.SOUTH);
        pack();
    }

    private void initPanelNorth() {
        //Panel
        jpTicket = new javax.swing.JPanel(new java.awt.GridLayout(1, 2));
        jtaCabecera = new JTextArea();
        jtaPie = new JTextArea();
        JScrollPane jspCabecera = new JScrollPane(jtaCabecera);
        jspCabecera.setBorder(javax.swing.BorderFactory.createTitledBorder("Cabecera"));
        JScrollPane jspPie = new JScrollPane(jtaPie);
        jspPie.setBorder(javax.swing.BorderFactory.createTitledBorder("Pie"));
        jpTicket.add(jspCabecera);
        jpTicket.add(jspPie);
    }

    private void initPanelSouth() {
        jpSouth = new javax.swing.JPanel(new java.awt.BorderLayout());
        jbCancelar = new javax.swing.JButton("Salir");
        jbGuardar = new javax.swing.JButton("Guardar");
        jbImprimirPaginaPrueba = new javax.swing.JButton("Imprimir ticket de prueba");
        javax.swing.JPanel jpSouthButtons = new javax.swing.JPanel();
        jpSouthButtons.add(jbImprimirPaginaPrueba);
        javax.swing.JPanel jpBotones = new javax.swing.JPanel();
        jpBotones.setBorder(new javax.swing.border.EtchedBorder());
        jpBotones.add(jbGuardar);
        jpBotones.add(jbImprimirPaginaPrueba);
        jpBotones.add(jbCancelar);
        jpSouth.add(jpBotones);
    }

    private void initPrinterPanel() {
        jtfNombreImpresora = new JTextField();
        jpImpresora = new javax.swing.JPanel(new java.awt.GridLayout(5, 2));
        jpImpresora.add(new JLabel("Nombre de impresora", JLabel.CENTER));
        jpImpresora.add(jtfNombreImpresora);
        jpImpresora.add(new JComponent() {
        });
        jpImpresora.add(new JComponent() {
        });
        jpImpresora.add(new JComponent() {
        });
        jpImpresora.add(new JComponent() {
        });
        jpImpresora.add(new JComponent() {
        });
        jpImpresora.add(new JComponent() {
        });
        jpImpresora.add(new JComponent() {
        });
        jpImpresora.add(new JComponent() {
        });
    }
}
