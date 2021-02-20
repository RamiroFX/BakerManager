/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Egresos;

import bakermanager.C_inicio;

/**
 *
 * @author Ramiro Ferreira
 */
public class GestionCompras {

    M_gestionCompras modelo;
    V_gestionCompras vista;
    C_gestionCompras controlador;

    public GestionCompras(C_inicio c_inicio) {
        this.modelo = new M_gestionCompras();
        this.vista = new V_gestionCompras();
        this.controlador = new C_gestionCompras(this.modelo, this.vista, c_inicio);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
