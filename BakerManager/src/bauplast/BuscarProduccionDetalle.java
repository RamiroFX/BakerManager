/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast;

import javax.swing.JFrame;

/**
 *
 * @author Ramiro Ferreira
 */
public class BuscarProduccionDetalle {

    M_buscarProduccionDetalle modelo;
    V_buscarProduccionDetalle vista;
    C_buscarProduccionDetalle controlador;

    public BuscarProduccionDetalle(JFrame frame) {
        this.modelo = new M_buscarProduccionDetalle();
        this.vista = new V_buscarProduccionDetalle(frame);
        this.controlador = new C_buscarProduccionDetalle(modelo, vista);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
