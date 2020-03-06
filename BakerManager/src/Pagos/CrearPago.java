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
public class CrearPago {

    private V_crearPago vista;
    private M_crearPago modelo;
    private C_crearPago controlador;

    public CrearPago(C_inicio inicio) {
        this.modelo = new M_crearPago();
        this.vista = new V_crearPago(inicio.vista);
        this.controlador = new C_crearPago(modelo, vista, inicio);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }

}
