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
public class CrearNotaCredito {

    private M_crearNotaCredito modelo;
    private V_crearNotaCredito vista;
    private C_crearNotaCredito controlador;

    public CrearNotaCredito(C_inicio inicio) {
        this.modelo = new M_crearNotaCredito();
        this.vista = new V_crearNotaCredito(inicio.vista);
        this.controlador = new C_crearNotaCredito(modelo, vista, inicio);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }

}
