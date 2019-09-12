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
public class ConfiguracionFactura {

    private V_configuracionFactura vista;
    private C_configuracionFactura controlador;
    private M_configuracionFactura modelo;

    public ConfiguracionFactura(JFrame frame) {
        this.modelo = new M_configuracionFactura();
        this.vista = new V_configuracionFactura(frame);
        this.controlador = new C_configuracionFactura(vista, modelo);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
