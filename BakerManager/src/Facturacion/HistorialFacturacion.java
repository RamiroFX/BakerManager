/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Facturacion;

import bakermanager.C_inicio;

/**
 *
 * @author Ramiro
 */
public class HistorialFacturacion {

    private M_historialFacturacion modelo;
    private V_historialFacturacion vista;
    private C_historialFacturacion controlador;

    public HistorialFacturacion(C_inicio c_inicio) {
        this.modelo = new M_historialFacturacion();
        this.vista = new V_historialFacturacion(c_inicio.vista);
        this.controlador = new C_historialFacturacion(modelo, vista, c_inicio);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }

}
