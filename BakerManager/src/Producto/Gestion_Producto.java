/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto;

import bakermanager.C_inicio;

/**
 *
 * @author Ramiro Ferreira
 */
public class Gestion_Producto {

    M_gestionProducto modelo;
    C_gestion_producto controlador;
    V_gestion_producto vista;

    public Gestion_Producto(C_inicio c_inicio) {
        this.modelo = new M_gestionProducto();
        this.vista = new V_gestion_producto();
        this.controlador = new C_gestion_producto(this.modelo, this.vista, c_inicio);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
