/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Produccion;

import bakermanager.C_inicio;

/**
 *
 * @author Ramiro Ferreira
 */
public class VerProduccion {
    private V_crearProduccion vista;
    private M_verProduccion modelo;
    private C_verProduccion controlador;
    
    

    public VerProduccion(C_inicio c_inicio) {
        this.modelo = new M_verProduccion();
        this.vista = new V_crearProduccion(c_inicio.vista);
        this.controlador = new C_verProduccion(modelo, vista);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }

    public void verPedidoRegistrado(int idProduccion) {
        this.controlador.verPedidoRegistrado(idProduccion);
    }
}
