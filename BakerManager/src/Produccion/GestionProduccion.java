/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Produccion;

import Pedido.*;
import bakermanager.C_inicio;

/**
 *
 * @author Ramiro Ferreira
 */
public class GestionProduccion {

    private M_gestionProduccion modelo;
    private V_gestionProduccion vista;
    private C_gestionProduccion controlador;

    public GestionProduccion(C_inicio c_inicio) {
        this.modelo = new M_gestionProduccion();
        this.vista = new V_gestionProduccion(c_inicio.vista);
        this.controlador = new C_gestionProduccion(modelo, vista, c_inicio);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
