/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros;

import javax.swing.JFrame;

/**
 *
 * @author Ramiro Ferreira
 */
public class CobroPendiente {

    M_cobroPendiente modelo;
    V_cobroPendiente vista;
    C_cobroPendiente controlador;

    public CobroPendiente(JFrame frame) {
        this.modelo = new M_cobroPendiente();
        this.vista = new V_cobroPendiente(frame);
        this.controlador = new C_cobroPendiente(this.modelo, this.vista);
    }

    public void mostrarVista() {
        this.controlador.mostraVista();
    }
}
