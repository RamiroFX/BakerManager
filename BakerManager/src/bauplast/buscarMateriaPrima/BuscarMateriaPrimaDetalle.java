/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast.buscarMateriaPrima;

import javax.swing.JFrame;

/**
 *
 * @author Ramiro Ferreira
 */
public class BuscarMateriaPrimaDetalle {

    private M_buscarMateriaPrimaDetalle modelo;
    private V_buscarMateriaPrimaDetalle vista;
    private C_buscarMateriaPrimaDetalle controlador;

    public BuscarMateriaPrimaDetalle(JFrame frame) {
        this.modelo = new M_buscarMateriaPrimaDetalle();
        this.vista = new V_buscarMateriaPrimaDetalle(frame);
        this.controlador = new C_buscarMateriaPrimaDetalle(this.modelo, this.vista);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }

}
