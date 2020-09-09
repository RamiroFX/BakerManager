/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Empleado;

import Interface.RecibirEmpleadoCallback;
import bakermanager.V_inicio;
import javax.swing.JDialog;

/**
 *
 * @author Ramiro Ferreira
 */
public class SeleccionarFuncionario {

    private V_seleccionar_funcionario vista;
    private C_seleccionar_funcionario controlador;

    public SeleccionarFuncionario(V_inicio v_inicio) {
        this.vista = new V_seleccionar_funcionario(v_inicio);
        this.controlador = new C_seleccionar_funcionario(vista);
    }
    public SeleccionarFuncionario(JDialog dialog) {
        this.vista = new V_seleccionar_funcionario(dialog);
        this.controlador = new C_seleccionar_funcionario(vista);
    }

    public void setCallback(RecibirEmpleadoCallback recb) {
        this.controlador.setCallback(recb);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
