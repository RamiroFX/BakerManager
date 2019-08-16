/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuracion;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 *
 * @author Ramiro Ferreira
 */
public class V_configuracion extends javax.swing.JDialog {

    public javax.swing.JButton jbCancelar, jbAgregarCampo, jbModificarCampo,
            jbHabilitarDeshabilitarCampo, jbImprimirPaginaPrueba, jbOcultarMostrarCampo;
    public javax.swing.JPanel jpSouth, jpTicket, jpFactura, jpFacturaPreferences;
    public javax.swing.JTabbedPane jtpCenter;
    public JTable jtTicket, jtFactura;
    //Variables de preferencia
    public JComboBox jcbTamañoLetra, jcbCantProd, jcbMoneda;
    public JComboBox<String> jcbFormatoFecha;
    public JTextField jtfTipoLetra, jtfDistanciaEntreCopias;
    public JCheckBox jchkDuplicado, jchkTriplicado, jchkMoneda;
    public javax.swing.JButton jbGuardar;

    public V_configuracion(JFrame frame) {
        super(frame, true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Configuración de impresiones");
        setAlwaysOnTop(false);
        setName("configuracion_impresiones");
        setPreferredSize(new java.awt.Dimension(1024, 768));
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        initPanelNorth();
        initPanelSouth();
        initPreferencePanel();
        jtpCenter = new JTabbedPane();
        //jtpCenter.add("Ticket", jpTicket);
        jtpCenter.add("Factura", jpFactura);
        jtpCenter.add("Preferencias", jpFacturaPreferences);
        getContentPane().add(jtpCenter, java.awt.BorderLayout.CENTER);
        getContentPane().add(jpSouth, java.awt.BorderLayout.SOUTH);
        pack();
    }

    private void initPanelNorth() {
        //Panel
        jpTicket = new javax.swing.JPanel(new java.awt.BorderLayout());
        jpFactura = new javax.swing.JPanel(new java.awt.BorderLayout());
        jpFacturaPreferences = new javax.swing.JPanel(new java.awt.BorderLayout());

        jtTicket = new JTable();
        jtTicket.getTableHeader().setReorderingAllowed(false);
        jtFactura = new JTable();
        jtFactura.getTableHeader().setReorderingAllowed(false);
        JScrollPane jspTicket = new JScrollPane(jtTicket);
        JScrollPane jspFactura = new JScrollPane(jtFactura);
        jpTicket.add(jspTicket, BorderLayout.CENTER);
        jpFactura.add(jspFactura, BorderLayout.CENTER);
    }

    private void initPreferencePanel() {
        jcbFormatoFecha = new JComboBox();
        jcbTamañoLetra = new JComboBox();
        jcbCantProd = new JComboBox();
        jcbMoneda = new JComboBox();
        jtfTipoLetra = new JTextField();
        jtfDistanciaEntreCopias = new JTextField();
        jchkDuplicado = new JCheckBox("Duplicado");
        jchkTriplicado = new JCheckBox("Triplicado");
        jchkMoneda = new JCheckBox("Impimir moneda");
        jbGuardar = new JButton("Guardar");
        javax.swing.JPanel jpPrefAux = new javax.swing.JPanel(new java.awt.GridLayout(9, 2));
        jpPrefAux.add(new JLabel("Formato de fecha", JLabel.CENTER));
        jpPrefAux.add(jcbFormatoFecha);
        jpPrefAux.add(new JLabel("Tamaño de letra", JLabel.CENTER));
        jpPrefAux.add(jcbTamañoLetra);
        jpPrefAux.add(new JLabel("Tipo de letra", JLabel.CENTER));
        jpPrefAux.add(jtfTipoLetra);
        jpPrefAux.add(new JLabel("Cantidad de productos por factura", JLabel.CENTER));
        jpPrefAux.add(jcbCantProd);
        jpPrefAux.add(new JLabel("Imprimir duplicado", JLabel.CENTER));
        jpPrefAux.add(jchkDuplicado);
        jpPrefAux.add(new JLabel("Imprimir triplicado", JLabel.CENTER));
        jpPrefAux.add(jchkTriplicado);
        jpPrefAux.add(new JLabel("Distancia entre copias", JLabel.CENTER));
        jpPrefAux.add(jtfDistanciaEntreCopias);
        jpPrefAux.add(new JLabel("Imprimir moneda", JLabel.CENTER));
        jpPrefAux.add(jchkMoneda);
        jpPrefAux.add(new JLabel("Monedas", JLabel.CENTER));
        jpPrefAux.add(jcbMoneda);
        javax.swing.JPanel jpPrefAux2 = new javax.swing.JPanel();
        jpPrefAux2.add(jbGuardar);
        jpFacturaPreferences.add(jpPrefAux, BorderLayout.CENTER);
        jpFacturaPreferences.add(jpPrefAux2, BorderLayout.SOUTH);
    }

    private void initPanelSouth() {
        jpSouth = new javax.swing.JPanel(new java.awt.BorderLayout());
        //jpSouth.setBorder(new javax.swing.border.EtchedBorder());
        jbCancelar = new javax.swing.JButton("Salir");
        jbAgregarCampo = new javax.swing.JButton("Agregar");
        jbModificarCampo = new javax.swing.JButton("Modificar");
        jbHabilitarDeshabilitarCampo = new javax.swing.JButton("Habilitar/Deshabilitar");
        jbImprimirPaginaPrueba = new javax.swing.JButton("Imprimir página de prueba");
        jbOcultarMostrarCampo = new javax.swing.JButton("Ocultar/Mostrar campos");
        javax.swing.JPanel jpSouthButtons = new javax.swing.JPanel();
        //jpSouthButtons.add(jbAgregarCampo);
        jpSouthButtons.add(jbModificarCampo);
        jpSouthButtons.add(jbHabilitarDeshabilitarCampo);
        jpSouthButtons.add(jbImprimirPaginaPrueba);
        jpSouthButtons.add(jbOcultarMostrarCampo);
        javax.swing.JPanel jpBotones = new javax.swing.JPanel();
        jpBotones.setBorder(new javax.swing.border.EtchedBorder());
        jpBotones.add(jbCancelar);
        jpFactura.add(jpSouthButtons, BorderLayout.SOUTH);
        jpSouth.add(jpBotones);
    }
}
