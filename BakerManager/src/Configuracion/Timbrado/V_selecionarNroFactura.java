/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuracion.Timbrado;

import javax.swing.JDialog;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
public class V_selecionarNroFactura extends javax.swing.JDialog {

    public javax.swing.JButton jbCancel, jbOK;
    private javax.swing.JLabel jlNroTimbrado, jlNroSucursal, jlNroPuntoVenta, jlRangoFacturas, jlNroFactura;
    public javax.swing.JTextField jtfNroTimbrado, jtfNroSucursal, jtfNroPuntoVenta, jtfRangoFacturas, jtfNroFactura;

    public V_selecionarNroFactura(JDialog vista) {
        super(vista, true);
        setTitle("Seleccione un Número de factura");
        setSize(new java.awt.Dimension(300, 250));
        setLocationRelativeTo(vista);
        initComponents();
    }

    private void initComponents() {
        getContentPane().setLayout(new MigLayout());
        jbOK = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();
        jlNroTimbrado = new javax.swing.JLabel("Nro. timbrado");
        jlNroSucursal = new javax.swing.JLabel("Nro. sucursal");
        jlNroPuntoVenta = new javax.swing.JLabel("Nro. Punto de venta");
        jlRangoFacturas = new javax.swing.JLabel("Rango de nro de facturas");
        jlNroFactura = new javax.swing.JLabel("Nro. Factura");
        jtfNroTimbrado = new javax.swing.JTextField();
        jtfNroSucursal = new javax.swing.JTextField();
        jtfNroPuntoVenta = new javax.swing.JTextField();
        jtfRangoFacturas = new javax.swing.JTextField();
        jtfNroFactura = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jbOK.setText("OK");
        jbCancel.setText("Cancel");
        getContentPane().add(jlNroTimbrado);
        getContentPane().add(jtfNroTimbrado, "width :200:,grow,wrap");
        getContentPane().add(jlNroPuntoVenta);
        getContentPane().add(jtfNroPuntoVenta, "width :200:,grow,wrap");
        getContentPane().add(jlNroSucursal);
        getContentPane().add(jtfNroSucursal, "width :200:,grow,wrap");
        getContentPane().add(jlRangoFacturas);
        getContentPane().add(jtfRangoFacturas, "width :200:,grow,wrap");
        getContentPane().add(jlNroFactura);
        getContentPane().add(jtfNroFactura, "width :200:,grow,wrap");
        getContentPane().add(jbOK);
        getContentPane().add(jbCancel);
        jtfNroTimbrado.selectAll();
    }

}
