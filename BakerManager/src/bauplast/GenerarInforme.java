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
public class GenerarInforme {

    private M_generarInforme modelo;
    private V_generarInforme vista;
    private C_generarInforme controlador;

    public GenerarInforme(JFrame vista) {
        this.modelo = new M_generarInforme();
        this.vista = new V_generarInforme(vista);
        this.controlador = new C_generarInforme(this.modelo, this.vista);
    }

    public void mostarVista() {
        this.controlador.mostrarVista();
    }
}
