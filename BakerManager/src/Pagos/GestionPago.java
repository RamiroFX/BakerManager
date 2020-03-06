/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pagos;

import bakermanager.C_inicio;

/**
 *
 * @author Ramiro Ferreira
 */
public class GestionPago {

    C_gestionPago controlador;
    M_gestionPago modelo;
    V_gestionPago vista;

    public GestionPago(C_inicio inicio) {
        this.modelo = new M_gestionPago();
        this.vista = new V_gestionPago();
        this.controlador = new C_gestionPago(vista, modelo, inicio);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }

}
