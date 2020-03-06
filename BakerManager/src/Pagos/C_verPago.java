/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pagos;

import Utilities.c_packColumn;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_verPago implements ActionListener, KeyListener {

    private M_verPago modelo;
    private V_crearPago vista;

    public C_verPago(M_verPago modelo, V_crearPago vista) {
        this.modelo = modelo;
        this.vista = vista;
        agregarListeners();
    }

    public void mostrarVista() {
        inicializarVista();
        vista.setVisible(true);
    }

    private void cerrar() {
        vista.dispose();
    }

    private void inicializarVista() {
        String registrador = modelo.getCabecera().getFuncionario().getNombre();
        this.vista.setTitle("Detalle de pago (Fecha registro: " + modelo.getCabecera().getFechaOperacion() + " - Registrado por: " + registrador + ")");
        this.vista.jtReciboDetalle.setModel(modelo.getTmDetalle());
        this.vista.jbModificarDetalle.setEnabled(false);
        this.vista.jbEliminarDetalle.setEnabled(false);
        this.vista.jbAgregarFactura.setEnabled(false);
        this.vista.jdcFechaCobro.setEnabled(false);
        this.vista.jtfNroFactura.setEditable(false);
        this.vista.jbProveedor.setEnabled(false);
        this.vista.jtfNroRecibo.setText(modelo.getCabecera().getNroRecibo() + "");
        this.vista.jtfNroRecibo.setEditable(false);
        this.vista.jtfProveedor.setText(modelo.getCabecera().getProveedor().getEntidad());
        this.vista.jtfProveedor.setEditable(false);
        this.vista.jdcFechaCobro.setDate(modelo.getCabecera().getFechaPago());
        this.vista.jftTotal.setValue(modelo.getTotal());
        this.vista.jbAceptar.setVisible(false);
        c_packColumn.packColumns(this.vista.jtReciboDetalle, 1);
    }

    private void agregarListeners() {
        this.vista.jbSalir.addActionListener(this);
        this.vista.jbSalir.addKeyListener(this);
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
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
