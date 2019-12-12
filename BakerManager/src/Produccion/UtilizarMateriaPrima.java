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
public class UtilizarMateriaPrima {

    private V_utilizarMateriaPrima vista;
    private M_utilizarMateriaPrima modelo;
    private C_utilizarMateriaPrima controlador;

    public UtilizarMateriaPrima(C_inicio c_inicio) {
        this.modelo = new M_utilizarMateriaPrima();
        this.vista = new V_utilizarMateriaPrima(c_inicio.vista);
        this.controlador = new C_utilizarMateriaPrima(modelo, vista);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
