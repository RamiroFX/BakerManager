/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros.Retencion;

import bakermanager.C_inicio;

/**
 *
 * @author Ramiro Ferreira
 */
public class GestionRetencion {

    private M_gestionRetencion modelo;
    private V_gestionRetencion vista;
    private C_gestionRetencion controlador;

    public GestionRetencion(C_inicio inicio) {
        this.modelo = new M_gestionRetencion();
        this.vista = new V_gestionRetencion(inicio.vista);
        this.controlador = new C_gestionRetencion(this.modelo, this.vista, inicio);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
