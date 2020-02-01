/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros;

import bakermanager.C_inicio;
import javax.swing.JDialog;

/**
 *
 * @author Ramiro Ferreira
 */
public class SeleccionarFacturaPendiente {

    private M_seleccionarFacturaPendiente modelo;
    private V_seleccionarFacturaPendiente vista;
    private C_seleccionarFacturaPendiente controlador;

    public SeleccionarFacturaPendiente(JDialog jdialog) {
        this.modelo = new M_seleccionarFacturaPendiente();
        this.vista = new V_seleccionarFacturaPendiente(jdialog);
        this.controlador = new C_seleccionarFacturaPendiente(modelo, vista);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
