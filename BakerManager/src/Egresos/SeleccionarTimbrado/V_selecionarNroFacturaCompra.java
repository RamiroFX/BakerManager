/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Egresos.SeleccionarTimbrado;

import Configuracion.Timbrado.*;
import javax.swing.JDialog;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
public class V_selecionarNroFacturaCompra extends javax.swing.JDialog {

    public javax.swing.JButton jbCancel, jbOK;
    private javax.swing.JLabel jlNroTimbrado, jlNroSucursal, jlNroPuntoVenta, jlNroFactura;
    public javax.swing.JTextField jtfNroTimbrado, jtfNroSucursal, jtfNroPuntoVenta, jtfNroFactura;

    public V_selecionarNroFacturaCompra(JDialog vista) {
        super(vista, true);
        setTitle("Seleccione un Número de factura");
        setSize(new java.awt.Dimension(300, 280));
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
        jlNroFactura = new javax.swing.JLabel("Nro. Factura");
        jtfNroTimbrado = new javax.swing.JTextField();
        jtfNroSucursal = new javax.swing.JTextField();
        jtfNroPuntoVenta = new javax.swing.JTextField();
        jtfNroFactura = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jbOK.setText("OK");
        jbCancel.setText("Cancel");
        getContentPane().add(jlNroTimbrado);
        getContentPane().add(jtfNroTimbrado, "width :200:,grow,wrap");
        getContentPane().add(jlNroSucursal);
        getContentPane().add(jtfNroSucursal, "width :200:,grow,wrap");
        getContentPane().add(jlNroPuntoVenta);
        getContentPane().add(jtfNroPuntoVenta, "width :200:,grow,wrap");
        getContentPane().add(jlNroFactura);
        getContentPane().add(jtfNroFactura, "width :200:,grow,wrap");
        getContentPane().add(jbOK);
        getContentPane().add(jbCancel,"wrap");
        jtfNroTimbrado.selectAll();
    }

}
