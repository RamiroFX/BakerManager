/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Proveedor;

/**
 *
 * @author Ramiro
 */
public class Crear_proveedor {

    private M_crear_proveedor modelo;
    private V_crear_proveedor vista;
    private C_crear_proveedor controlador;

    public Crear_proveedor(C_gestion_proveedores gestionProveedores) {
        this.modelo = new M_crear_proveedor();
        this.vista = new V_crear_proveedor(gestionProveedores.c_inicio.vista, true);
        this.controlador = new C_crear_proveedor(this.modelo, this.vista, gestionProveedores);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }

}
