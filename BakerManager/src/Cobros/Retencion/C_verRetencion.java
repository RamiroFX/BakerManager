/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros.Retencion;

import Entities.E_retencionVenta;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;

/**
 *
 * @author Ramiro Ferreira
 */
public class C_verRetencion implements ActionListener, KeyListener {

    public M_verRetencion modelo;
    public V_crearRetencion vista;
    private DecimalFormat integerFormat;

    public C_verRetencion(M_verRetencion modelo, V_crearRetencion vista) {
        this.modelo = modelo;
        this.vista = vista;
        inicializarVariables();
        agregarListeners();
    }

    private void inicializarVariables() {
        integerFormat = new DecimalFormat("###,###");
    }

    public void cargarDatos(E_retencionVenta retencion) {
        this.modelo.cargarDatos(retencion);
        this.vista.setTitle(V_crearRetencion.READ_TITLE);
        inicializarVista();
    }

    public void mostrarVista() {
        this.vista.setVisible(true);
    }

    private void cerrar() {
        this.vista.dispose();
    }

    private void inicializarVista() {
        this.vista.jdcFechaRetencion.setEnabled(false);
        this.vista.jtfNroFactura.setEditable(false);
        this.vista.jtfNroRetencion.setEditable(false);
        this.vista.jftMontoRetencion.setEditable(false);
        this.vista.jftPorcentajeRetencion.setEditable(false);
        this.vista.jsPorcentaje.setEnabled(false);
        this.vista.jbAceptar.setVisible(false);
        this.vista.jdcFechaRetencion.setDate(modelo.getRetencion().getTiempo());
        this.vista.jtfCliente.setText(modelo.getRetencion().getVenta().getCliente().getEntidad() + " (" + modelo.getRetencion().getVenta().getCliente().getRucCompleto() + ")");
        this.vista.jtfNroFactura.setText(integerFormat.format(modelo.getRetencion().getVenta().getNroFactura()));
        this.vista.jtfNroRetencion.setText(integerFormat.format(modelo.getRetencion().getNroRetencion()));
        this.vista.jftMontoRetencion.setValue(modelo.getRetencion().getMonto());
        this.vista.jftPorcentajeRetencion.setValue(modelo.getRetencion().getPorcentaje());
        this.vista.jsPorcentaje.setValue(modelo.getRetencion().getPorcentaje());
        this.vista.jtfIVA.setText(integerFormat.format(modelo.obtenerMontoConIva() - modelo.obtenerMontoSinIva()));
        this.vista.jtfMontoConIVA.setText(integerFormat.format(modelo.obtenerMontoConIva()));
        this.vista.jtfMontoSinIVA.setText(integerFormat.format(modelo.obtenerMontoSinIva()));
    }

    private void agregarListeners() {
        this.vista.jbCancelar.addActionListener(this);
        this.vista.jtfNroFactura.addKeyListener(this);
        this.vista.jbAceptar.addKeyListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(this.vista.jbCancelar)) {
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
