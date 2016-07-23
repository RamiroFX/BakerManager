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
 * @author Ramiro
 */
class Crear_rol {

    private M_crear_rol modelo;
    private V_crear_rol vista;
    private C_crear_rol controlador;

    public Crear_rol(JDialog inicio) {
        modelo = new M_crear_rol();
        vista = new V_crear_rol(inicio);
        controlador = new C_crear_rol(modelo, vista);
    }

    public Crear_rol(JFrame inicio) {
        modelo = new M_crear_rol();
        vista = new V_crear_rol(inicio);
        controlador = new C_crear_rol(modelo, vista);
    }

    public void mostrarVista() {
        controlador.mostrarVista();
    }
}
