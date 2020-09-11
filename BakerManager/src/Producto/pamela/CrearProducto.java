/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto.pamela;

import javax.swing.JFrame;

/**
 *
 * @author Ramiro Ferreira
 */
public class CrearProducto {

    M_crearProducto modelo;
    V_crearProducto vista;
    C_crearProducto controlador;

    public CrearProducto(JFrame frame) {
        this.modelo = new M_crearProducto();
        this.vista = new V_crearProducto(frame);
        this.controlador = new C_crearProducto(this.modelo, this.vista);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
