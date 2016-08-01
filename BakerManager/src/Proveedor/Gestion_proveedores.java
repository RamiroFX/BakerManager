/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Proveedor;

import bakermanager.C_inicio;

/**
 *
 * @author Usuario
 */
public class Gestion_proveedores {

    V_gestion_proveedores vista;
    C_gestion_proveedores controlador;

    public Gestion_proveedores(C_inicio c_inicio) {
        this.vista = new V_gestion_proveedores();
        this.controlador = new C_gestion_proveedores(c_inicio);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }

}
