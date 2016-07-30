/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto;

/**
 *
 * @author Ramiro
 */
public class Crear_producto {

    M_crear_producto modelo;
    V_crear_producto vista;
    C_crear_producto controlador;

    public Crear_producto(C_gestion_producto gestionProducto) {
        this.modelo = new M_crear_producto();
        this.vista = new V_crear_producto(gestionProducto.c_inicio.vista);
        this.controlador = new C_crear_producto(modelo, vista, gestionProducto);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
