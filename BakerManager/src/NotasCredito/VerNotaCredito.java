/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NotasCredito;

import javax.swing.JFrame;

/**
 *
 * @author Ramiro Ferreira
 */
public class VerNotaCredito {

    private V_crearNotaCredito vista;
    private M_verNotaCredito modelo;
    private C_verNotaCredito controlador;

    public VerNotaCredito(JFrame frame) {
        this.vista = new V_crearNotaCredito(frame);
        this.modelo = new M_verNotaCredito();
        this.controlador = new C_verNotaCredito(this.modelo, this.vista);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }

    public void establecerIdNotaCredito(int idNotacredito) {
        //this.controlador.setId();
    }

}
