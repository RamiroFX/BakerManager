/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuracion;

import javax.swing.JFrame;

/**
 *
 * @author Ramiro
 */
public class Configuracion {

    private V_configuracion vista;
    private C_configuracion controlador;

    public Configuracion(JFrame frame) {
        this.vista = new V_configuracion(frame);
        this.controlador = new C_configuracion(vista);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
