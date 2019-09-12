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
public class ConfiguracionBoleta {

    private V_configuracionFactura vista;
    private C_configuracionBoleta controlador;
    private M_configuracionBoleta modelo;

    public ConfiguracionBoleta(JFrame frame) {
        this.modelo = new M_configuracionBoleta();
        this.vista = new V_configuracionFactura(frame);
        this.controlador = new C_configuracionBoleta(vista, modelo);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
    
}
