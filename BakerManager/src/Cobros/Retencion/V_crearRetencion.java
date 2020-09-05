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

    private JLabel jlMontoSinIVA, jlIVA, jlMontoConIVA, jlNroRetencion,
            jlFechaRetencion, jlPorcentajeRetencion, jlMontoConRetencion;
    public JTextField jtfNroFactura, jtfCliente, jtfMontoConIVA;
    public JFormattedTextField jftPorcentajeRetencion, jftMontoRetencion;
    public JDateChooser jdcFechaRetencion;
    public JTextField jtfMontoSinIVA, jtfNroRetencion, jtfIVA;
    public JSpinner jsPorcentaje;
    public JButton jbCliente, jbNroFactura, jbAceptar, jbCancelar;
    private JPanel jpPrincipal, jpBotones;
    public static final String CREATE_TITLE = "Crear Retención", READ_TITLE = "Ver Retención";

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
        jbCliente = new JButton("Cliente");
        jbCliente.setHorizontalAlignment(JLabel.CENTER);
        jbNroFactura = new JButton("Factura");
        jbNroFactura.setHorizontalAlignment(JLabel.CENTER);
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
        jftPorcentajeRetencion = new JFormattedTextField(0.0);
        jtfMontoConIVA = new JTextField();
        jtfMontoConIVA.setEditable(false);
        jtfMontoSinIVA = new JTextField();
        jtfMontoSinIVA.setEditable(false);
        jtfIVA = new JTextField();
        jtfIVA.setEditable(false);
        jtfNroRetencion = new JTextField();
        jftMontoRetencion = new JFormattedTextField();
        //Spinner
        jsPorcentaje = new JSpinner();
        JComponent editor = jsPorcentaje.getEditor();
        JFormattedTextField tf = ((JSpinner.DefaultEditor) editor).getTextField();
        tf.setColumns(4);
        JPanel jpPorcentaje = new JPanel(new MigLayout());
        jpPorcentaje.add(jftPorcentajeRetencion, "pushx, growx");
        jpPorcentaje.add(jsPorcentaje);

        JPanel jpDatosFactura = new JPanel(new MigLayout());
        jpDatosFactura.setBorder(new EtchedBorder());
        JPanel jpDatosRetencion = new JPanel(new MigLayout());
        jpDatosRetencion.setBorder(new EtchedBorder());
        jpDatosFactura.add(jbCliente);
        jpDatosFactura.add(jtfCliente, "pushx, growx, wrap");
        jpDatosFactura.add(jbNroFactura);
        jpDatosFactura.add(jtfNroFactura, "pushx, growx, wrap");
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
        jpDatosRetencion.add(jftMontoRetencion, "pushx, growx");
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
        setSize(new Dimension(430, 430));
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setModal(true);
    }
}
