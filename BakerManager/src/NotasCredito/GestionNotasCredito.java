/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NotasCredito;

import bakermanager.C_inicio;

/**
 *
 * @author Ramiro Ferreira
 */
public class GestionNotasCredito {

    private M_gestionNotasCredito modelo;
    private V_gestionNotasCredito vista;
    private C_gestionNotasCredito controlador;

    public GestionNotasCredito(C_inicio c_inicio) {
        this.modelo = new M_gestionNotasCredito();
        this.vista = new V_gestionNotasCredito(c_inicio.vista);
        this.controlador = new C_gestionNotasCredito(modelo, vista, c_inicio);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }

}
