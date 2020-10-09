/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast.desperdicio.buscarDesperdicio;

import javax.swing.JFrame;

/**
 *
 * @author Ramiro Ferreira
 */
public class BuscarDesperdicioDetalle {

    private M_buscarDesperdicioDetalle modelo;
    private V_buscarDesperdicioDetalle vista;
    private C_buscarDesperdicioDetalle controlador;

    public BuscarDesperdicioDetalle(JFrame frame) {
        this.modelo = new M_buscarDesperdicioDetalle();
        this.vista = new V_buscarDesperdicioDetalle(frame);
        this.controlador = new C_buscarDesperdicioDetalle(this.modelo, this.vista);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }

}
