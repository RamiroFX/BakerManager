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
public class SeleccionarCliente {

    V_seleccionar_cliente vista;
    C_seleccionar_cliente controlador;

    public SeleccionarCliente(V_inicio v_inicio) {
        this.vista = new V_seleccionar_cliente(v_inicio);
        this.controlador = new C_seleccionar_cliente(vista);
    }

    public SeleccionarCliente(JDialog v_inicio) {
        this.vista = new V_seleccionar_cliente(v_inicio);
        this.controlador = new C_seleccionar_cliente(vista);
    }

    public void setCallback(RecibirClienteCallback rccb) {
        this.controlador.setCallback(rccb);
    }
    
    //PARA SELECCIONAR VARIOS CLIENTES SIN QUE SE CIERRE DESPUES DE CADA SELECCION
    public void establecerSiempreVisible(){
        this.controlador.establecerSiempreVisible();
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
