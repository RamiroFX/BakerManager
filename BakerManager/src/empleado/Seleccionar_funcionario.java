/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Empleado;

import Interface.RecibirEmpleadoCallback;
import bakermanager.V_inicio;

/**
 *
 * @author Ramiro Ferreira
 */
public class Seleccionar_funcionario {

    private V_seleccionar_funcionario vista;
    private C_seleccionar_funcionario controlador;

    public Seleccionar_funcionario(V_inicio v_inicio) {
        this.vista = new V_seleccionar_funcionario(v_inicio);
        this.controlador = new C_seleccionar_funcionario(vista);
    }

    public void setCallback(RecibirEmpleadoCallback recb) {
        this.controlador.setCallback(recb);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
