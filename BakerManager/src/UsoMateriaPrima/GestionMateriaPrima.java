/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UsoMateriaPrima;

import bakermanager.C_inicio;

/**
 *
 * @author Ramiro Ferreira
 */
public class GestionMateriaPrima {

    private M_gestionMateriaPrima modelo;
    private V_gestionMateriaPrima vista;
    private C_gestionMateriaPrima controlador;

    public GestionMateriaPrima(C_inicio c_inicio) {
        this.modelo = new M_gestionMateriaPrima();
        this.vista = new V_gestionMateriaPrima(c_inicio.vista);
        this.controlador = new C_gestionMateriaPrima(modelo, vista, c_inicio);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
