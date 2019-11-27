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
public class Facturacion {

    M_facturacion modelo;
    V_facturacion vista;
    C_facturacion controlador;

    public Facturacion(C_inicio inicio, int idCliente, String fechaInicio, String fechaFin, String condVenta) {
        this.modelo = new M_facturacion(idCliente, fechaInicio, fechaFin, condVenta);
        this.vista = new V_facturacion(inicio);
        this.controlador = new C_facturacion(modelo, vista);
    }
    
    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
