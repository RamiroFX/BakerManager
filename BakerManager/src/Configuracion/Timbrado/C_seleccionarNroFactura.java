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

/**
 *
 * @author Ramiro Ferreira
 */
public class C_seleccionarNroFactura implements ActionListener, KeyListener {

    private M_seleccionarNroFactura modelo;
    private V_selecionarNroFactura vista;

    public C_seleccionarNroFactura(M_seleccionarNroFactura modelo, V_selecionarNroFactura vista) {
        this.modelo = modelo;
        this.vista = vista;
        agregarListeners();
        cargarDatos();
    }

    public void setCallback(RecibirTimbradoVentaCallback callback) {
        modelo.setCallback(callback);
    }

    public void setTimbrado(E_Timbrado timbrado) {
        modelo.setTimbrado(timbrado);
    }

    private void cargarDatos() {

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
    }

    private boolean validarNroFactura() {

        return true;
    }

    public void enviarNroFactura() {
        if (!validarNroFactura()) {
            return;
        }
        int nroFactura = 0;
        modelo.getCallback().recibirTimbradoNroFactura(modelo.getTimbrado(), nroFactura);
        cerrar();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(this.vista.jbOK)) {
            enviarNroFactura();
        } else if (e.getSource().equals(this.vista.jbCancel)) {
            cerrar();
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
    }

    @Override
    public void keyPressed(KeyEvent ke) {
    }

    @Override
    public void keyReleased(KeyEvent ke) {
    }

}
