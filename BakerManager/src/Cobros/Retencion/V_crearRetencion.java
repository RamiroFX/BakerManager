/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros.Retencion;

import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
public class V_crearRetencion extends JDialog {

    private JLabel jlNroFactura, jlCliente, jlMontoSinIVA, jlIVA, jlMontoConIVA, jlNroRetencion,
            jlFechaRetencion, jlPorcentajeRetencion, jlMontoConRetencion;
    public JTextField jtfNroFactura, jtfCliente, jtfMontoConIVA,
            jtfPorcentajeRetencion, jtfMontoConRetencion;
    public JDateChooser jdcFechaRetencion;
    public JTextField jtfMontoSinIVA, jtfNroRetencion, jtfIVA;
    public JSpinner jsPorcentaje;
    public JButton jbAceptar, jbCancelar;
    private JPanel jpPrincipal, jpBotones;
    public static final String CREATE_TITLE = "Crear Retención";

    public V_crearRetencion(JDialog vista) {
        super(vista);
        initializeComponents();
        constructLayout();
        constructAppWindow();
    }

    public V_crearRetencion(JFrame vista) {
        super(vista);
        initializeComponents();
        constructLayout();
        constructAppWindow();
    }

    private void initializeComponents() {
        jpPrincipal = new JPanel(new GridLayout(2, 1));

        //Labels
        jlNroFactura = new JLabel("Nro. Factura");
        jlNroFactura.setHorizontalAlignment(JLabel.CENTER);
        jlCliente = new JLabel("Cliente");
        jlCliente.setHorizontalAlignment(JLabel.CENTER);
        jlMontoSinIVA = new JLabel("Monto sin I.V.A.");
        jlMontoSinIVA.setHorizontalAlignment(JLabel.CENTER);
        jlIVA = new JLabel("I.V.A.");
        jlIVA.setHorizontalAlignment(JLabel.CENTER);
        jlMontoConIVA = new JLabel("Monto con I.V.A.");
        jlMontoConIVA.setHorizontalAlignment(JLabel.CENTER);
        jlNroRetencion = new JLabel("Nro. Retención");
        jlNroRetencion.setHorizontalAlignment(JLabel.CENTER);
        jlFechaRetencion = new JLabel("Fecha de retención");
        jlFechaRetencion.setHorizontalAlignment(JLabel.CENTER);
        jlPorcentajeRetencion = new JLabel("Porcentaje de retención");
        jlPorcentajeRetencion.setHorizontalAlignment(JLabel.CENTER);
        jlMontoConRetencion = new JLabel("Monto de retención");
        jlMontoConRetencion.setHorizontalAlignment(JLabel.CENTER);
        //TextFields
        jtfNroFactura = new JTextField();
        jtfCliente = new JTextField();
        jtfCliente.setEditable(false);
        jdcFechaRetencion = new JDateChooser();
        jtfPorcentajeRetencion = new JTextField();
        jtfMontoConIVA = new JTextField();
        jtfMontoConIVA.setEditable(false);
        jtfMontoSinIVA = new JTextField();
        jtfMontoSinIVA.setEditable(false);
        jtfIVA = new JTextField();
        jtfIVA.setEditable(false);
        jtfNroRetencion = new JTextField();
        jtfMontoConRetencion = new JTextField();
        //Spinner
        jsPorcentaje = new JSpinner();
        JComponent editor = jsPorcentaje.getEditor();
        JFormattedTextField tf = ((JSpinner.DefaultEditor) editor).getTextField();
        tf.setColumns(4);
        JPanel jpPorcentaje = new JPanel(new MigLayout());
        jpPorcentaje.add(jtfPorcentajeRetencion, "pushx, growx");
        jpPorcentaje.add(jsPorcentaje);

        JPanel jpDatosFactura = new JPanel(new MigLayout());
        jpDatosFactura.setBorder(new EtchedBorder());
        JPanel jpDatosRetencion = new JPanel(new MigLayout());
        jpDatosRetencion.setBorder(new EtchedBorder());
        jpDatosFactura.add(jlNroFactura);
        jpDatosFactura.add(jtfNroFactura, "pushx, growx, wrap");
        jpDatosFactura.add(jlCliente);
        jpDatosFactura.add(jtfCliente, "pushx, growx, wrap");
        jpDatosFactura.add(jlMontoSinIVA);
        jpDatosFactura.add(jtfMontoSinIVA, "pushx, growx, wrap");
        jpDatosFactura.add(jlIVA);
        jpDatosFactura.add(jtfIVA, "pushx, growx, wrap");
        jpDatosFactura.add(jlMontoConIVA);
        jpDatosFactura.add(jtfMontoConIVA, "pushx, growx, wrap");
        jpDatosRetencion.add(jlNroRetencion);
        jpDatosRetencion.add(jtfNroRetencion, "pushx, growx, wrap");
        jpDatosRetencion.add(jlFechaRetencion);
        jpDatosRetencion.add(jdcFechaRetencion, "pushx, growx, wrap");
        jpDatosRetencion.add(jlPorcentajeRetencion);
        jpDatosRetencion.add(jpPorcentaje, "pushx, growx, wrap");
        jpDatosRetencion.add(jlMontoConRetencion);
        jpDatosRetencion.add(jtfMontoConRetencion, "pushx, growx");
        jpPrincipal.add(jpDatosFactura);
        jpPrincipal.add(jpDatosRetencion);

        jpBotones = new JPanel();
        jbAceptar = new JButton("Aceptar");
        jbCancelar = new JButton("Cancelar");
        jpBotones.add(jbAceptar);
        jpBotones.add(jbCancelar);
    }

    private void constructLayout() {
        getContentPane().setLayout(new java.awt.BorderLayout());
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jpPrincipal, BorderLayout.CENTER);
        getContentPane().add(jpBotones, BorderLayout.SOUTH);
    }

    private void constructAppWindow() {
        setTitle(CREATE_TITLE);
        setName("jdCrearRetencion");
        setPreferredSize(new Dimension(400, 380));
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setModal(true);
    }
}
