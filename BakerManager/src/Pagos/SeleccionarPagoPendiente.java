/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pagos;

import Cobros.*;
import Interface.RecibirCtaCteDetalleCallback;
import Interface.RecibirReciboPagoDetalleCallback;
import javax.swing.JDialog;

/**
 *
 * @author Ramiro Ferreira
 */
public class SeleccionarPagoPendiente {

    private M_seleccionarPagoPendiente modelo;
    private V_seleccionarPagoPendiente vista;
    private C_seleccionarPagoPendiente controlador;

    public SeleccionarPagoPendiente(JDialog jdialog, int idCliente) {
        this.modelo = new M_seleccionarPagoPendiente(idCliente);
        this.vista = new V_seleccionarPagoPendiente(jdialog);
        this.controlador = new C_seleccionarPagoPendiente(modelo, vista);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }

    public void setCallback(RecibirReciboPagoDetalleCallback callback) {
        this.controlador.setCallback(callback);
    }
}
