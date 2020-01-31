/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros;

import bakermanager.C_inicio;

/**
 *
 * @author Ramiro Ferreira
 */
public class GestionCobroPago {

    C_gestionCobroPago controlador;
    M_gestionCobroPago modelo;
    V_gestionCobroPago vista;

    public GestionCobroPago(C_inicio inicio) {
        this.modelo = new M_gestionCobroPago();
        this.vista = new V_gestionCobroPago();
        this.controlador = new C_gestionCobroPago(vista, modelo, inicio);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
    
}
