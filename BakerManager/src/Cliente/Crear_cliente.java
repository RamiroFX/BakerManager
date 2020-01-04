/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import Interface.InterfaceNotificarCambio;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 *
 * @author Ramiro Ferreira
 */
public class Crear_cliente {

    private M_crear_cliente modelo;
    private V_crear_cliente vista;
    private C_crear_cliente controlador;

    public Crear_cliente(JFrame frame) {
        this.modelo = new M_crear_cliente();
        this.vista = new V_crear_cliente(frame, true);
        this.controlador = new C_crear_cliente(this.modelo, this.vista);
    }

    public Crear_cliente(JDialog dialogo) {
        this.modelo = new M_crear_cliente();
        this.vista = new V_crear_cliente(dialogo, true);
        this.controlador = new C_crear_cliente(this.modelo, this.vista);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }

    public void setInterfaceNotificarCambio(InterfaceNotificarCambio interfaceNotificarCambio) {
        this.controlador.setInterfaceNotificarCambio(interfaceNotificarCambio);
    }
}
