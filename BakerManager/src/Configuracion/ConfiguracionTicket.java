/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuracion;

import javax.swing.JFrame;

/**
 *
 * @author Ramiro Ferreira
 */
public class ConfiguracionTicket {

    V_configuracionTicket vista;
    M_configuracionTicket modelo;
    C_configuracionTicket controlador;

    public ConfiguracionTicket(JFrame frame) {
        this.vista = new V_configuracionTicket(frame);
        this.modelo = new M_configuracionTicket();
        this.controlador = new C_configuracionTicket(vista, modelo);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
