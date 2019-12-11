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
public class CrearProduccion {

    private V_crearProduccion vista;
    private M_crearProduccion modelo;
    private C_crearProduccion controlador;

    public CrearProduccion(C_inicio c_inicio) {
        this.modelo = new M_crearProduccion();
        this.vista = new V_crearProduccion(c_inicio.vista);
        this.controlador = new C_crearProduccion(modelo, vista);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
