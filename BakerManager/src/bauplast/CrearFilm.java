/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast;

import Entities.E_produccionFilm;
import Entities.M_producto;
import Interface.InterfaceRecibirProduccionFilm;
import javax.swing.JDialog;

/**
 *
 * @author Ramiro Ferreira
 */
public class CrearFilm {

    private M_crearFilm modelo;
    private V_crearFilm vista;
    private C_crearFilm controlador;

    public CrearFilm(JDialog dialog) {
        this.modelo = new M_crearFilm();
        this.vista = new V_crearFilm(dialog);
        this.controlador = new C_crearFilm(modelo, vista);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }

    public void setCallback(InterfaceRecibirProduccionFilm callback) {
        this.controlador.setInterface(callback);
    }

    public void rellenarVista(M_producto rollo) {
        this.controlador.rellenarVista(rollo);
    }

    public void modificarRollo(int index, E_produccionFilm rollo) {
        this.controlador.modificarRollo(index, rollo);
    }

}
