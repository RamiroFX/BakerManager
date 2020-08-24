/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuracion.Timbrado;

import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 *
 * @author Ramiro Ferreira
 */
public class CrearTimbrado {

    M_crearTimbrado modelo;
    V_crearTimbrado vista;
    C_crearTimbrado controlador;

    public CrearTimbrado(JFrame frame) {
        this.modelo = new M_crearTimbrado();
        this.vista = new V_crearTimbrado(frame);
        this.controlador = new C_crearTimbrado(modelo, vista);
    }

    public CrearTimbrado(JDialog dialog) {
        this.modelo = new M_crearTimbrado();
        this.vista = new V_crearTimbrado(dialog);
        this.controlador = new C_crearTimbrado(modelo, vista);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
