/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto;

/**
 *
 * @author Ramiro Ferreira
 */
public class Modificar_producto {

    M_modificar_producto modelo;
    V_modificar_producto vista;
    C_modificar_producto controlador;

    public Modificar_producto(C_gestion_producto gestionProducto, int idProducto) {
        this.modelo = new M_modificar_producto(idProducto);
        this.vista = new V_modificar_producto(gestionProducto.c_inicio.vista);
        this.controlador = new C_modificar_producto(modelo, vista, gestionProducto);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }

}
