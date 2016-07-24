/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Proveedor;

import MenuPrincipal.C_MenuPrincipal;

/**
 *
 * @author Usuario
 */
public class Gestion_proveedores {

    V_gestion_proveedores vista;
    C_gestion_proveedores controlador;

    public Gestion_proveedores(C_MenuPrincipal c_menu_principal) {
        this.vista = new V_gestion_proveedores();
        this.controlador = new C_gestion_proveedores(c_menu_principal.c_inicio);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }

}
