/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto.pamela;

import bakermanager.C_inicio;

/**
 *
 * @author Ramiro Ferreira
 */
public class GestionProducto {

    M_gestionProducto modelo;
    V_gestionProducto vista;
    C_gestionProducto controlador;

    public GestionProducto(C_inicio inicio) {
        this.modelo = new M_gestionProducto();
        this.vista = new V_gestionProducto();
        this.controlador = new C_gestionProducto(this.modelo, this.vista, inicio);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }

}
