/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuracion.Timbrado;

import bakermanager.C_inicio;

/**
 *
 * @author Ramiro Ferreira
 */
public class GestionTimbrado {

    M_gestionTimbrado modelo;
    V_gestionTimbrado vista;
    C_gestionTimbrado controlador;

    public GestionTimbrado(C_inicio inicio) {
        this.modelo = new M_gestionTimbrado();
        this.vista = new V_gestionTimbrado(inicio.vista);
        this.controlador = new C_gestionTimbrado(modelo, vista, inicio);
    }

    public void mostrarVista() {
        controlador.mostrarVista();
    }
}
