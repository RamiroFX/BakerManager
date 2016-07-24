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
public class Gestion_Ventas {

    private M_gestionVentas modelo;
    private C_gestionVentas controlador;
    private V_gestionVentas vista;

    public Gestion_Ventas(C_inicio c_inicio) {
        this.modelo = new M_gestionVentas();
        this.vista = new V_gestionVentas();
        this.controlador = new C_gestionVentas(modelo, vista, c_inicio);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
