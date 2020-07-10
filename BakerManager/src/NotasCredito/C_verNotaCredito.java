/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NotasCredito;

import Entities.E_NotaCreditoCabecera;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_verNotaCredito implements ActionListener, KeyListener {

    public M_verNotaCredito modelo;
    public V_crearNotaCredito vista;

    public C_verNotaCredito(M_verNotaCredito modelo, V_crearNotaCredito vista) {
        this.modelo = modelo;
        this.vista = vista;
        agregarListeners();
    }

    public void inicializarDatos(E_NotaCreditoCabecera notaCreditoCabecera) {
        this.modelo.inicializarDatos(notaCreditoCabecera);
        inicializarVista();
    }

    public void mostrarVista() {
        vista.setVisible(true);
    }

    private void cerrar() {
        this.vista.dispose();
    }

    private void inicializarVista() {
        this.vista.jtNotaCreditoDetalle.setModel(modelo.getNotaCreditoDetalleTm());
        Utilities.c_packColumn.packColumns(this.vista.jtNotaCreditoDetalle, 1);
        this.vista.jdcFechaotaCredito.setEnabled(false);
        this.vista.jtfNroNotaCredito.setEditable(false);
        this.vista.jtfNroFactura.setEditable(false);
        this.vista.jbAceptar.setVisible(false);
        this.vista.jbAgregarFactura.setEnabled(false);
        this.vista.jbModificarDetalle.setEnabled(false);
        this.vista.jbEliminarDetalle.setEnabled(false);
        this.vista.jdcFechaotaCredito.setDate(modelo.getCabecera().getTiempo());
        this.vista.jtfCliente.setText(modelo.getCabecera().getCliente().getEntidad() + " (" + modelo.getCabecera().getCliente().getRucCompleto() + ")");
        this.vista.jtfNroNotaCredito.setText(modelo.getCabecera().getNroNotaCredito() + "");
        sumarTotal();
    }

    private void agregarListeners() {
        this.vista.jbAceptar.addActionListener(this);
        this.vista.jbAgregarFactura.addActionListener(this);
        this.vista.jbCliente.addActionListener(this);
        this.vista.jbEliminarDetalle.addActionListener(this);
        this.vista.jbModificarDetalle.addActionListener(this);
        this.vista.jbSalir.addActionListener(this);
        this.vista.jbAgregarFactura.addKeyListener(this);
        this.vista.jtfNroFactura.addKeyListener(this);
        this.vista.jtfNroNotaCredito.addKeyListener(this);
        this.vista.jbCliente.addKeyListener(this);
        this.vista.jbAceptar.addKeyListener(this);
        this.vista.jbSalir.addKeyListener(this);
    }

    private void sumarTotal() {
        this.vista.jftTotal.setValue(modelo.getTotal());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(this.vista.jbSalir)) {
            cerrar();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE: {
                cerrar();
                break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
