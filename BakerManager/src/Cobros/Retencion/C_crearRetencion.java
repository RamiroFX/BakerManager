/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros.Retencion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_crearRetencion implements ActionListener, KeyListener {

    private static final String VALIDAR_NRO_FACTURA_1 = "Ingrese solo números enteros en número de factura",
            VALIDAR_NRO_FACTURA_2 = "Ingrese solo números enteros y positivos en número de factura",
            VALIDAR_NRO_FACTURA_3 = "Ingrese un en número de factura";

    private M_crearRetencion modelo;
    private V_crearRetencion vista;

    public C_crearRetencion(M_crearRetencion modelo, V_crearRetencion vista) {
        this.modelo = modelo;
        this.vista = vista;
        agregarListeners();
    }

    public void mostrarVista() {
        this.vista.pack();
        this.vista.setLocationRelativeTo(null);
        this.vista.setVisible(true);
    }

    public void cerrar() {
        this.vista.dispose();
    }

    private void agregarListeners() {
        this.vista.jtfNroFactura.addActionListener(this);
        this.vista.jtfNroFactura.addKeyListener(this);
        this.vista.jtfNroRetencion.addActionListener(this);
        this.vista.jtfNroRetencion.addKeyListener(this);
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbCancelar.addActionListener(this);
    }

    private boolean validarNroFactura() {
        int nroFactura = -1;
        if (this.vista.jtfNroFactura.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, VALIDAR_NRO_FACTURA_3, "Atención", JOptionPane.WARNING_MESSAGE);
            return false;
        } else {
            try {
                nroFactura = Integer.valueOf(this.vista.jtfNroFactura.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(vista, VALIDAR_NRO_FACTURA_1, "Atención", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            if (nroFactura < 0) {
                JOptionPane.showMessageDialog(vista, VALIDAR_NRO_FACTURA_2, "Atención", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        return true;
    }

    private void consultarNroFactura() {
        if (!validarNroFactura()) {
            return;
        }
        int nroFactura = Integer.valueOf(this.vista.jtfNroFactura.getText().trim());
        modelo.consultarNroFactura(nroFactura);
        this.vista.jtfCliente.setText(modelo.getFacturaCabecera().getCliente().getEntidad());
        this.vista.jtfMontoConIVA.setText(modelo.obtenerMontoConIva() + "");
        this.vista.jtfMontoSinIVA.setText(modelo.obtenerMontoSinIva() + "");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(vista.jtfNroFactura)) {
            consultarNroFactura();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE: {
                cerrar();
                break;
            }
        }
    }

}
