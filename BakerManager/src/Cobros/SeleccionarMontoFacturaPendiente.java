/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros;

import Entities.E_facturaSinPago;
import Interface.RecibirCtaCteDetalleCallback;
import javax.swing.JDialog;

/**
 *
 * @author Ramiro Ferreira
 */
public class SeleccionarMontoFacturaPendiente {

    M_seleccionarMontoFacturaPendiente modelo;
    V_seleccionarMontoFacturaPendiente vista;
    C_seleccionarMontoFacturaPendiente controlador;

    public SeleccionarMontoFacturaPendiente(JDialog vista) {
        this.modelo = new M_seleccionarMontoFacturaPendiente();
        this.vista = new V_seleccionarMontoFacturaPendiente(vista);
        this.controlador = new C_seleccionarMontoFacturaPendiente(this.modelo, this.vista);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }

    public void inicializarVista(E_facturaSinPago fsp) {
        this.controlador.inicializarVista(fsp);
    }

    public void setCallback(RecibirCtaCteDetalleCallback callback) {
        this.controlador.setCallback(callback);
    }

}
