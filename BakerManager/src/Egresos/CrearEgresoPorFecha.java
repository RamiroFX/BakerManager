/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Egresos;

import javax.swing.JFrame;

/**
 *
 * @author Ramiro Ferreira
 */
public class CrearEgresoPorFecha {

    private M_crearEgresoPorFecha modelo;
    private V_crearEgresoPorFecha vista;
    private C_crearEgresoPorFecha controlador;

    public CrearEgresoPorFecha(JFrame frame) {
        this.modelo = new M_crearEgresoPorFecha();
        this.vista = new V_crearEgresoPorFecha(frame);
        this.controlador = new C_crearEgresoPorFecha(modelo, vista);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}