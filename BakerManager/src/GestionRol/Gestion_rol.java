/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GestionRol;

import bakermanager.C_inicio;

/**
 *
 * @author Ramiro
 */
public class Gestion_rol {

    private M_gestion_rol modelo;
    private V_gestion_rol vista;
    private C_gestion_rol controlador;

    public Gestion_rol(C_inicio inicio) {
        this.modelo = new M_gestion_rol();
        this.vista = new V_gestion_rol(inicio.vista);
        this.controlador = new C_gestion_rol(this.modelo, this.vista);
    }

    public void mostrarVista(){
        this.controlador.mostrarVista();
    }
}
