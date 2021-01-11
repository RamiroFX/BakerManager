/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas.VentaPorFecha;

import Ventas.V_crearVentaRapida;
import com.toedter.calendar.JDateChooser;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Ramiro Ferreira
 */
public class V_crearVentaPorFecha extends V_crearVentaRapida {

    JDateChooser jdcFecha;

    public V_crearVentaPorFecha(JFrame frame, boolean esModoCreacion_) {
        super(frame, esModoCreacion_);
    }

    @Override
    public void initNorth() {
        jdcFecha = new JDateChooser();
        jpNorth = new JPanel(new MigLayout());
        jbCliente = new JButton("Agregar cliente [F3]");
        jbNroFactura = new JButton("Nro. Factura [F5]");
        jtfCliente = new JTextField(30);
        jtfCliente.setEditable(false);
        jtfNroFactura = new JTextField(30);
        jtfNroFactura.setEditable(false);
        jcbCondVenta = new JComboBox();
        jcbTipoVenta = new JComboBox();
        jtfClieRuc = new JTextField(30);
        jtfClieRuc.setEditable(false);
        jtfClieDireccion = new JTextField(30);
        jtfClieDireccion.setEditable(false);
        jtfClieTelefono = new JTextField(30);
        jtfClieTelefono.setEditable(false);
        jpNorth.add(jbCliente);
        jpNorth.add(jtfCliente);
        jpNorth.add(jbNroFactura);
        jpNorth.add(jtfNroFactura);
        jpNorth.add(jcbCondVenta);
        jpNorth.add(jcbTipoVenta, "wrap");
        jpNorth.add(new JLabel("R.U.C.:"));
        jpNorth.add(jtfClieRuc);
        jpNorth.add(new JLabel("Direccion:"));
        jpNorth.add(jtfClieDireccion);
        jpNorth.add(new JLabel("Telefono:"));
        jpNorth.add(jtfClieTelefono,"wrap");
        jpNorth.add(new JLabel("Fecha:"));
        jpNorth.add(jdcFecha, "spanx 2, growx 2");
    }

}
