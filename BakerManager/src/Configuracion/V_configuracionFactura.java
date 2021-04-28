/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuracion;

import Entities.E_impresionOrientacion;
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
public class V_configuracionFactura extends javax.swing.JDialog {

    private final String paperHint = "A4(595 x 842)pixels";
    public javax.swing.JButton jbNuevo, jbCancelar, jbAgregarCampo, jbModificarCampo,
            jbHabilitarDeshabilitarCampo, jbImprimirPaginaPrueba, jbOcultarMostrarCampo;
    public javax.swing.JPanel jpNorth, jpSouth, jpFactura, jpFacturaPreferences,
            jpImpresoraYPapel;
    public javax.swing.JTabbedPane jtpCenter;
    public JTable jtFactura;
    //Variables de preferencia
    public JComboBox jcbTamañoLetra, jcbCantProd, jcbMoneda;
    public JComboBox<String> jcbFormatoFecha;
    public JTextField jtfTipoLetra, jtfDistanciaEntreCopias;
    public JCheckBox jchkDuplicado, jchkTriplicado, jchkMoneda;
    //Variables de impresora
    public JTextField jtfNombreImpresora, jtfAnchoPapel, jtfLargoPapel,
            jtfMargenX, jtfMargenY;
    public javax.swing.JButton jbGuardarPreferencias, jbGuardarImpresora;
    public javax.swing.JLabel jlOrientacion;
    public JComboBox<E_impresionOrientacion> jcbOrientacion;
    public JComboBox jcbPlantillas;

    public V_configuracionFactura(JFrame frame) {
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
        initPrinterPanel();
        jtpCenter = new JTabbedPane();
        jtpCenter.add("Coord. de parametros", jpFactura);
        jtpCenter.add("Preferencias", jpFacturaPreferences);
        jtpCenter.add("Impresora y papel", jpImpresoraYPapel);
        getContentPane().add(jpNorth, java.awt.BorderLayout.NORTH);
        getContentPane().add(jtpCenter, java.awt.BorderLayout.CENTER);
        getContentPane().add(jpSouth, java.awt.BorderLayout.SOUTH);
        pack();
    }

    private void initPanelNorth() {
        jpNorth = new javax.swing.JPanel();
        jcbPlantillas = new javax.swing.JComboBox();
        jcbPlantillas.addItem("Factura beracah");
        jcbPlantillas.addItem("Escencia de lo Alto");
        jpNorth.add(new JLabel("Plantilla"));
        jpNorth.add(jcbPlantillas);
        //Panel
        jpFactura = new javax.swing.JPanel(new java.awt.BorderLayout());
        jpFacturaPreferences = new javax.swing.JPanel(new java.awt.BorderLayout());
        jtFactura = new JTable();
        jtFactura.getTableHeader().setReorderingAllowed(false);
        JScrollPane jspFactura = new JScrollPane(jtFactura);
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
        jbGuardarPreferencias = new JButton("Guardar");
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
        jpPrefAux2.add(jbGuardarPreferencias);
        jpFacturaPreferences.add(jpPrefAux, BorderLayout.CENTER);
        jpFacturaPreferences.add(jpPrefAux2, BorderLayout.SOUTH);
    }

    private void initPanelSouth() {
        jpSouth = new javax.swing.JPanel(new java.awt.BorderLayout());
        //jpSouth.setBorder(new javax.swing.border.EtchedBorder());
        jbNuevo = new javax.swing.JButton("Nuevo");
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
        jpBotones.add(jbNuevo);
        jpBotones.add(jbCancelar);
        jpFactura.add(jpSouthButtons, BorderLayout.SOUTH);
        jpSouth.add(jpBotones);
    }

    private void initPrinterPanel() {
        jtfNombreImpresora = new JTextField();
        jtfAnchoPapel = new JTextField();
        jtfAnchoPapel.setToolTipText(paperHint);
        jtfLargoPapel = new JTextField();
        jtfLargoPapel.setToolTipText(paperHint);
        jtfMargenX = new JTextField();
        jtfMargenY = new JTextField();
        jlOrientacion = new JLabel("Orientación", JLabel.CENTER);
        jcbOrientacion = new JComboBox();
        jbGuardarImpresora = new javax.swing.JButton("Guardar");
        javax.swing.JPanel jpImpresoraYPapel1 = new javax.swing.JPanel(new java.awt.GridLayout(6, 2));
        jpImpresoraYPapel1.add(new JLabel("Nombre de impresora", JLabel.CENTER));
        jpImpresoraYPapel1.add(jtfNombreImpresora);
        jpImpresoraYPapel1.add(new JLabel("Ancho de papel", JLabel.CENTER));
        jpImpresoraYPapel1.add(jtfAnchoPapel);
        jpImpresoraYPapel1.add(new JLabel("Largo de papel", JLabel.CENTER));
        jpImpresoraYPapel1.add(jtfLargoPapel);
        jpImpresoraYPapel1.add(new JLabel("Margen X", JLabel.CENTER));
        jpImpresoraYPapel1.add(jtfMargenX);
        jpImpresoraYPapel1.add(new JLabel("Margen Y", JLabel.CENTER));
        jpImpresoraYPapel1.add(jtfMargenY);
        jpImpresoraYPapel1.add(jlOrientacion);
        jpImpresoraYPapel1.add(jcbOrientacion);
        javax.swing.JPanel jpImpresoraYPapel2 = new javax.swing.JPanel();
        jpImpresoraYPapel2.add(jbGuardarImpresora);
        jpImpresoraYPapel = new javax.swing.JPanel(new java.awt.BorderLayout());
        jpImpresoraYPapel.add(jpImpresoraYPapel1, BorderLayout.CENTER);
        jpImpresoraYPapel.add(jpImpresoraYPapel2, BorderLayout.SOUTH);
    }
}
