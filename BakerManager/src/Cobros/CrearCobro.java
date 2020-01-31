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
public class CrearCobro {

    private V_crearCobro vista;
    private M_crearCobro modelo;
    private C_crearCobro controlador;

    public CrearCobro(C_inicio inicio) {
        this.modelo = new M_crearCobro();
        this.vista = new V_crearCobro(inicio.vista);
        this.controlador = new C_crearCobro(modelo, vista);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }

}
