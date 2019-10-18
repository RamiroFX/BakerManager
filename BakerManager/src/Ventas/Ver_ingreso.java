/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas;

import bakermanager.C_inicio;

/**
 *
 * @author Ramiro Ferreira
 */
public class Ver_ingreso {

    public V_crearVentaRapida vista;
    public C_ver_ingreso controlador;

    public Ver_ingreso(C_inicio c_inicio, Integer idIngresoCabecera) {
        this.vista = new V_crearVentaRapida(c_inicio.vista);
        this.controlador = new C_ver_ingreso(idIngresoCabecera, this.vista);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
