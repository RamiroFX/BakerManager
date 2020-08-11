/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros.Retencion;

import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import static java.awt.Dialog.DEFAULT_MODALITY_TYPE;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/**
 *
 * @author Ramiro Ferreira
 */
public class V_crearRetencion extends JDialog {

    private JLabel jlNroFactura, jlCliente, jlMontoSinIVA, jlIVA, jlMontoConIVA, jlNroRetencion,
            jlFechaRetencion, jlPorcentajeRetencion, jlMontoConRetencion;
    public JTextField jtfNroFactura, jtfCliente, jtfMontoConIVA,
            jtfPorcentajeRetencion;
    public JDateChooser jdcFechaRetencion;
    public JTextField jtfMontoSinIVA, jtfNroRetencion, jtfIVA;
    public JSpinner spPorcentaje;
    public JButton jbAceptar, jbCancelar;
    private JPanel jpPrincipal, jpBotones;

    public V_crearRetencion(JDialog vista) {
        super(vista, "Crear Retención", DEFAULT_MODALITY_TYPE);
        setName("jdCrearRetencion");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initializeComponents();
        constructLayout();
    }

    private void initializeComponents() {
        jpPrincipal = new JPanel(new GridLayout(10, 2));

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
        //TextFields
        jtfNroFactura = new JTextField();
        jtfCliente = new JTextField();
        jdcFechaRetencion = new JDateChooser();
        jtfPorcentajeRetencion = new JTextField();
        jtfMontoConIVA = new JTextField();
        jtfMontoSinIVA = new JTextField();
        jtfIVA = new JTextField();
        jtfNroRetencion = new JTextField();
        //Spinner
        spPorcentaje = new JSpinner();
        JPanel jpPorcentaje = new JPanel();
        jpPorcentaje.add(jtfPorcentajeRetencion);
        jpPorcentaje.add(spPorcentaje);

        jpPrincipal.add(jlNroFactura);
        jpPrincipal.add(jtfNroFactura);
        jpPrincipal.add(jlCliente);
        jpPrincipal.add(jtfCliente);
        jpPrincipal.add(jlMontoSinIVA);
        jpPrincipal.add(jtfMontoSinIVA);
        jpPrincipal.add(jlIVA);
        jpPrincipal.add(jtfIVA);
        jpPrincipal.add(jlNroRetencion);
        jpPrincipal.add(jtfNroRetencion);
        jpPrincipal.add(jlMontoConIVA);
        jpPrincipal.add(jtfMontoConIVA);
        jpPrincipal.add(jlFechaRetencion);
        jpPrincipal.add(jdcFechaRetencion);
        jpPrincipal.add(jlPorcentajeRetencion);
        jpPrincipal.add(jpPorcentaje);

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
}
