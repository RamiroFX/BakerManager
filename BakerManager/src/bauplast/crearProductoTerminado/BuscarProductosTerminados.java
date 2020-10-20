/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast.crearProductoTerminado;

import javax.swing.JFrame;

/**
 *
 * @author Ramiro Ferreira
 */
public class BuscarProductosTerminados {

    private M_buscarProductosTerminados modelo;
    private V_buscarProductosTerminados vista;
    private C_buscarProductosTerminados controlador;

    public BuscarProductosTerminados(JFrame frame) {
        this.modelo = new M_buscarProductosTerminados();
        this.vista = new V_buscarProductosTerminados(frame);
        this.controlador = new C_buscarProductosTerminados(this.modelo, this.vista);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }

}
