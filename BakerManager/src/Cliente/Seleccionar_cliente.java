/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import Interface.RecibirClienteCallback;
import bakermanager.V_inicio;
import javax.swing.JDialog;

/**
 *
 * @author Ramiro Ferreira
 */
public class Seleccionar_cliente {

    V_seleccionar_cliente vista;
    C_seleccionar_cliente controlador;

    public Seleccionar_cliente(V_inicio v_inicio) {
        this.vista = new V_seleccionar_cliente(v_inicio);
        this.controlador = new C_seleccionar_cliente(vista);
    }

    public Seleccionar_cliente(JDialog v_inicio) {
        this.vista = new V_seleccionar_cliente(v_inicio);
        this.controlador = new C_seleccionar_cliente(vista);
    }

    public void setCallback(RecibirClienteCallback rccb) {
        this.controlador.setCallback(rccb);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
