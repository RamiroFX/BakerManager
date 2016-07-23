/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GestionRol;

import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 *
 * @author Ramiro Ferreira
 */
public class Modificar_rol {

    M_modificar_rol modelo;
    V_modificar_rol vista;
    C_modificar_rol controlador;

    public Modificar_rol(JDialog inicio, int idRol) {
        this.modelo = new M_modificar_rol(idRol);
        this.vista = new V_modificar_rol(inicio);
        this.controlador = new C_modificar_rol(this.modelo, this.vista);
    }

    public Modificar_rol(JFrame inicio, int idRol) {
        this.modelo = new M_modificar_rol(idRol);
        this.vista = new V_modificar_rol(inicio);
        this.controlador = new C_modificar_rol(this.modelo, this.vista);
    }

    public void mostrarVista() {
        controlador.mostrarVista();
    }
}
