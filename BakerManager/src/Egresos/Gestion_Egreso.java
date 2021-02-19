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
public class Gestion_Egreso {

    M_gestionEgresos modelo;
    V_gestion_egresos vista;
    C_gestionEgresos controlador;

    public Gestion_Egreso(C_inicio c_inicio) {
        this.modelo = new M_gestionEgresos();
        this.vista = new V_gestion_egresos();
        this.controlador = new C_gestionEgresos(this.modelo, this.vista, c_inicio);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
