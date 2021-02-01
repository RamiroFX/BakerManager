/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Egresos.SeleccionarTimbrado;

import Interface.RecibirTimbradoVentaCallback;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_seleccionarNroFacturaCompra implements ActionListener, KeyListener {

    private static final String VALIDAR_NRO_FACTURA_1 = "Ingrese un número de factura";
    private static final String VALIDAR_NRO_FACTURA_2 = "Asegurese de colocar un numero positivo en el número de factura";
    private static final String VALIDAR_NRO_FACTURA_3 = "El número de factura ingresado se encuentra en uso";
    private static final String VALIDAR_NRO_TIMBRADO_1 = "Ingrese un número de timbrado";
    private static final String VALIDAR_NRO_TIMBRADO_2 = "Asegurese de colocar un numero positivo en el número de timbrado";
    private static final String VALIDAR_NRO_SUCURSAL_1 = "Ingrese un número de sucursal";
    private static final String VALIDAR_NRO_SUCURSAL_2 = "Asegurese de colocar un numero positivo en el número de sucursal";
    private static final String VALIDAR_NRO_PUNTO_VENTA_1 = "Ingrese un número de punto de venta";
    private static final String VALIDAR_NRO_PUNTO_VENTA_2 = "Asegurese de colocar un numero positivo en el número de punto de venta";
    private M_seleccionarNroFacturaCompra modelo;
    private V_selecionarNroFacturaCompra vista;

    public C_seleccionarNroFacturaCompra(M_seleccionarNroFacturaCompra modelo, V_selecionarNroFacturaCompra vista, RecibirTimbradoVentaCallback callback) {
        this.modelo = modelo;
        this.modelo.setCallback(callback);
        this.vista = vista;
        agregarListeners();
    }

    public void mostrarVista() {
        this.vista.setVisible(true);
    }

    private void cerrar() {
        this.vista.dispose();
        System.runFinalization();
    }

    private void agregarListeners() {
        this.vista.jbOK.addActionListener(this);
        this.vista.jbCancel.addActionListener(this);
        this.vista.jtfNroTimbrado.addActionListener(this);
        this.vista.jtfNroSucursal.addActionListener(this);
        this.vista.jtfNroPuntoVenta.addActionListener(this);
        this.vista.jtfNroFactura.addActionListener(this);
        this.vista.jtfNroFactura.addKeyListener(this);
        this.vista.jtfNroTimbrado.addKeyListener(this);
        this.vista.jtfNroSucursal.addKeyListener(this);
        this.vista.jtfNroPuntoVenta.addKeyListener(this);
        this.vista.jbOK.addKeyListener(this);
        this.vista.jbCancel.addKeyListener(this);
    }

    public void enviarNroFactura() {
        if (!validarNroFactura()) {
            return;
        }
        int nroFactura = Integer.valueOf(this.vista.jtfNroFactura.getText().trim());
        modelo.getCallback().recibirTimbradoNroFactura(modelo.getTimbrado(), nroFactura);
        cerrar();
    }

    private boolean validarNroTimbrado() {
        int nroTimbrado = -1;
        if (this.vista.jtfNroTimbrado.getText().trim().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, VALIDAR_NRO_TIMBRADO_1, "Atención",
                    javax.swing.JOptionPane.OK_OPTION);
            return false;
        }
        try {
            String nroFacturaString = this.vista.jtfNroTimbrado.getText().trim();
            nroTimbrado = Integer.valueOf(nroFacturaString);
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, VALIDAR_NRO_TIMBRADO_2, "Atención",
                    javax.swing.JOptionPane.OK_OPTION);
            return false;
        }
        if (nroTimbrado < 1) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, VALIDAR_NRO_TIMBRADO_2, "Atención",
                    javax.swing.JOptionPane.OK_OPTION);
            return false;
        }
        modelo.getTimbrado().setNroTimbrado(nroTimbrado);
        this.vista.jtfNroSucursal.requestFocusInWindow();
        return true;
    }

    private boolean validarNroSucursal() {
        int nroSucursal = -1;
        if (this.vista.jtfNroSucursal.getText().trim().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, VALIDAR_NRO_SUCURSAL_1, "Atención",
                    javax.swing.JOptionPane.OK_OPTION);
            return false;
        }
        try {
            String nroSucursalString = this.vista.jtfNroSucursal.getText().trim();
            nroSucursal = Integer.valueOf(nroSucursalString);
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, VALIDAR_NRO_SUCURSAL_2, "Atención",
                    javax.swing.JOptionPane.OK_OPTION);
            return false;
        }
        if (nroSucursal < 1) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, VALIDAR_NRO_SUCURSAL_2, "Atención",
                    javax.swing.JOptionPane.OK_OPTION);
            return false;
        }
        this.modelo.getTimbrado().setNroSucursal(nroSucursal);
        this.vista.jtfNroPuntoVenta.requestFocusInWindow();
        return true;
    }

    private boolean validarNroPuntoVenta() {
        int nroPuntoVenta = -1;
        if (this.vista.jtfNroPuntoVenta.getText().trim().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, VALIDAR_NRO_PUNTO_VENTA_1, "Atención",
                    javax.swing.JOptionPane.OK_OPTION);
            return false;
        }
        try {
            String nroPuntoVentaString = this.vista.jtfNroPuntoVenta.getText().trim();
            nroPuntoVenta = Integer.valueOf(nroPuntoVentaString);
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, VALIDAR_NRO_PUNTO_VENTA_2, "Atención",
                    javax.swing.JOptionPane.OK_OPTION);
            return false;
        }
        if (nroPuntoVenta < 1) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, VALIDAR_NRO_PUNTO_VENTA_2, "Atención",
                    javax.swing.JOptionPane.OK_OPTION);
            return false;
        }
        this.modelo.getTimbrado().setNroPuntoVenta(nroPuntoVenta);
        this.vista.jtfNroFactura.requestFocusInWindow();
        return true;
    }

    private boolean validarNroFactura() {
        int nroFactura = -1;
        if (this.vista.jtfNroFactura.getText().trim().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, VALIDAR_NRO_FACTURA_1, "Atención",
                    javax.swing.JOptionPane.OK_OPTION);
            return false;
        }
        try {
            String nroFacturaString = this.vista.jtfNroFactura.getText().trim();
            nroFactura = Integer.valueOf(nroFacturaString);
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, VALIDAR_NRO_FACTURA_2, "Atención",
                    javax.swing.JOptionPane.OK_OPTION);
            return false;
        }
        if (nroFactura < 1) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, VALIDAR_NRO_FACTURA_2, "Atención",
                    javax.swing.JOptionPane.OK_OPTION);
            return false;
        }
        if (modelo.nroFacturaEnUso(nroFactura)) {
            javax.swing.JOptionPane.showMessageDialog(this.vista, VALIDAR_NRO_FACTURA_3, "Atención",
                    javax.swing.JOptionPane.OK_OPTION);
            return false;
        }
        modelo.setNroFactura(nroFactura);
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(this.vista.jbOK)) {
            enviarNroFactura();
        } else if (e.getSource().equals(this.vista.jbCancel)) {
            cerrar();
        } else if (e.getSource().equals(this.vista.jtfNroTimbrado)) {
            validarNroTimbrado();
        } else if (e.getSource().equals(this.vista.jtfNroSucursal)) {
            validarNroSucursal();
        } else if (e.getSource().equals(this.vista.jtfNroPuntoVenta)) {
            validarNroPuntoVenta();
        } else if (e.getSource().equals(this.vista.jtfNroFactura)) {
            enviarNroFactura();
        }
    }

    @Override
    public void keyTyped(KeyEvent ke) {
        if (vista.jtfNroFactura.hasFocus()) {
            if (ke.getKeyChar() == '\n') {
                enviarNroFactura();
            }
            if (ke.getKeyChar() == KeyEvent.VK_ESCAPE) {
                cerrar();
            }
        }
        if (ke.getKeyChar() == KeyEvent.VK_ESCAPE) {
            cerrar();
        }
    }

    @Override
    public void keyPressed(KeyEvent ke) {
    }

    @Override
    public void keyReleased(KeyEvent ke) {

        if (ke.getKeyChar() == KeyEvent.VK_ESCAPE) {
            cerrar();
        }
    }

}
