/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Reportes;

import bakermanager.C_inicio;

/**
 *
 * @author Ramiro Ferreira
 */
public class GestionReporte {

    C_gestion_reporte controlador;
    V_gestion_reporte vista;

    public GestionReporte(C_inicio c_inicio) {
        this.vista = new V_gestion_reporte();
        this.controlador = new C_gestion_reporte(vista, c_inicio);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
