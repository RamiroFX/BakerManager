/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast;

import Interface.InterfaceRecibirProduccionFilm;
import javax.swing.JDialog;

/**
 *
 * @author Ramiro Ferreira
 */
public class SeleccionarRollo {

    M_seleccionarRollo modelo;
    V_seleccionarRollo vista;
    C_seleccionarRollo controlador;

    public SeleccionarRollo(JDialog dialog) {
        this.modelo = new M_seleccionarRollo();
        this.vista = new V_seleccionarRollo(dialog);
        this.controlador = new C_seleccionarRollo(modelo, vista);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }

    public void setCallback(InterfaceRecibirProduccionFilm callback) {
        this.controlador.setCallback(callback);
    }
}
