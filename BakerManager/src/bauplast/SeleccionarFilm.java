/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast;

import Entities.E_productoClasificacion;
import Interface.InterfaceRecibirProduccionFilm;
import javax.swing.JDialog;

/**
 *
 * @author Ramiro Ferreira
 */
public class SeleccionarFilm {

    M_seleccionarFilm modelo;
    V_seleccionarFilm vista;
    C_seleccionarFilm controlador;

    public SeleccionarFilm(JDialog dialog) {
        this.modelo = new M_seleccionarFilm();
        this.vista = new V_seleccionarFilm(dialog);
        this.controlador = new C_seleccionarFilm(modelo, vista);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }

    public void setCallback(InterfaceRecibirProduccionFilm callback) {
        this.controlador.setCallback(callback);
    }
}
