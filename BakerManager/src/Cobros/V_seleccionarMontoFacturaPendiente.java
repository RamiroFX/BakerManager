/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros;

import javax.swing.JDialog;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
public class V_seleccionarMontoFacturaPendiente extends javax.swing.JDialog {

    public javax.swing.JButton jbOK, jbCancel;
    private javax.swing.JLabel jlIdFactura, jlNroFactura, jlCliente, jlMontoTotal, jlSaldo,
            jlPago;
    public javax.swing.JTextField jtfIdFactura, jtfNroFactura,
            jtfMontoTotal, jtfSaldo, jtfPago, jtfCliente;

    public V_seleccionarMontoFacturaPendiente(JDialog vista) {
        super(vista, true);
        setTitle("Seleccione una cantidad");
        setSize(new java.awt.Dimension(300, 270));
        setLocationRelativeTo(vista);
        initComponents();
    }

    private void initComponents() {
        getContentPane().setLayout(new MigLayout());
        jbOK = new javax.swing.JButton("Ok");
        jbCancel = new javax.swing.JButton("Cancel");
        jlIdFactura = new javax.swing.JLabel("Id. factura");
        jlNroFactura = new javax.swing.JLabel("Nro. factura");
        jlCliente = new javax.swing.JLabel("Cliente");
        jlMontoTotal = new javax.swing.JLabel("Total");
        jlSaldo = new javax.swing.JLabel("Saldo");
        jlPago = new javax.swing.JLabel("Pago");
        jtfIdFactura = new javax.swing.JTextField();
        jtfIdFactura.setEditable(false);
        jtfNroFactura = new javax.swing.JTextField();
        jtfNroFactura.setEditable(false);
        jtfCliente = new javax.swing.JTextField();
        jtfCliente.setEditable(false);
        jtfMontoTotal = new javax.swing.JTextField();
        jtfMontoTotal.setEditable(false);
        jtfSaldo = new javax.swing.JTextField();
        jtfSaldo.setEditable(false);
        jtfPago = new javax.swing.JTextField("0");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().add(jlIdFactura);
        getContentPane().add(jtfIdFactura, "width :200:,grow,wrap");
        getContentPane().add(jlNroFactura);
        getContentPane().add(jtfNroFactura, "width :200:,grow,wrap");
        getContentPane().add(jlCliente);
        getContentPane().add(jtfCliente, "width :200:,grow,wrap");
        getContentPane().add(jlMontoTotal);
        getContentPane().add(jtfMontoTotal, "width :200:,grow,wrap");
        getContentPane().add(jlSaldo);
        getContentPane().add(jtfSaldo, "width :200:,grow,wrap");
        getContentPane().add(jlPago);
        getContentPane().add(jtfPago, "width :200:,grow,wrap");

        getContentPane().add(jbOK);
        getContentPane().add(jbCancel);

        jtfPago.selectAll();
    }
}
