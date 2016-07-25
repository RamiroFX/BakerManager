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

    private M0_gestionVentas modelo;
    private C0_gestionVentas controlador;
    private V0_gestionVentas vista;

    public Gestion_Ventas(C_inicio c_inicio) {
        this.modelo = new M0_gestionVentas();
        this.vista = new V0_gestionVentas();
        this.controlador = new C0_gestionVentas(modelo, vista, c_inicio);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
