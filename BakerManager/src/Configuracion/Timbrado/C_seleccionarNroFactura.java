/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuracion.Timbrado;

import Entities.E_Timbrado;
import Interface.RecibirTimbradoVentaCallback;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_seleccionarNroFactura implements ActionListener, KeyListener, RecibirTimbradoVentaCallback {

    private static final String VALIDAR_NRO_FACTURA_1 = "Ingrese un número de factura";
    private static final String VALIDAR_NRO_FACTURA_2 = "Asegurese de colocar un numero positivo en el número de factura";
    private static final String VALIDAR_NRO_FACTURA_3 = "El número de factura ingresado se encuentra en uso";
    private M_seleccionarNroFactura modelo;
    private V_selecionarNroFactura vista;

    public C_seleccionarNroFactura(M_seleccionarNroFactura modelo, V_selecionarNroFactura vista, RecibirTimbradoVentaCallback callback, E_Timbrado timbrado) {
        this.modelo = modelo;
        this.modelo.setCallback(callback);
        this.modelo.setTimbrado(timbrado);
        this.vista = vista;
        agregarListeners();
        cargarDatos();
    }

    private void cargarDatos() {
        this.vista.jtfNroTimbrado.setText(modelo.getTimbrado().getNroTimbrado() + "");
        this.vista.jtfNroSucursal.setText(modelo.getTimbrado().getNroSucursal() + "");
        this.vista.jtfNroPuntoVenta.setText(modelo.getTimbrado().getNroPuntoVenta() + "");
        this.vista.jtfRangoFacturas.setText(modelo.getTimbrado().getNroBoletaInicial() + "-" + modelo.getTimbrado().getNroBoletaFinal());
        this.vista.jtfNroTimbrado.setEditable(false);
        this.vista.jtfNroSucursal.setEditable(false);
        this.vista.jtfNroPuntoVenta.setEditable(false);
        this.vista.jtfRangoFacturas.setEditable(false);
        this.vista.jtfNroFactura.setText(modelo.obtenerUltimoNroFactura() + "");
        this.vista.jtfNroFactura.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorRemoved(AncestorEvent pEvent) {
            }

            @Override
            public void ancestorMoved(AncestorEvent pEvent) {
            }

            @Override
            public void ancestorAdded(AncestorEvent pEvent) {
                // TextField is added to its parent => request focus in Event Dispatch Thread
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        vista.jtfNroFactura.requestFocusInWindow();
                    }
                });
            }
        });
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
        this.vista.jtfNroTimbrado.addKeyListener(this);
        this.vista.jtfNroSucursal.addKeyListener(this);
        this.vista.jtfNroPuntoVenta.addKeyListener(this);
        this.vista.jtfRangoFacturas.addKeyListener(this);
        this.vista.jtfNroFactura.addActionListener(this);
        this.vista.jbCambiarTimbrado.addActionListener(this);
        this.vista.jbOK.addKeyListener(this);
        this.vista.jbCancel.addKeyListener(this);
        this.vista.jbCambiarTimbrado.addKeyListener(this);
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

    public void enviarNroFactura() {
        if (!validarNroFactura()) {
            return;
        }
        int nroFactura = Integer.valueOf(this.vista.jtfNroFactura.getText().trim());
        modelo.getCallback().recibirTimbradoNroFactura(modelo.getTimbrado(), nroFactura);
        cerrar();
    }

    private void invocarSeleccionTimbrado() {
        SeleccionarTimbrado st = new SeleccionarTimbrado(vista, this);
        st.mostrarVista();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(this.vista.jbOK)) {
            enviarNroFactura();
        } else if (e.getSource().equals(this.vista.jbCancel)) {
            cerrar();
        } else if (e.getSource().equals(this.vista.jtfNroFactura)) {
            enviarNroFactura();
        } else if (e.getSource().equals(this.vista.jbCambiarTimbrado)) {
            invocarSeleccionTimbrado();
        }
    }

    @Override
    public void keyTyped(KeyEvent ke) {
        if (vista.jtfRangoFacturas.hasFocus()) {
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
    }

    @Override
    public void recibirTimbrado(E_Timbrado timbrado) {
        this.modelo.setTimbrado(timbrado);
        cargarDatos();
    }

    @Override
    public void recibirTimbradoNroFactura(E_Timbrado timbrado, int nroFactura) {
        this.modelo.setTimbrado(timbrado);
        cargarDatos();
    }

}
