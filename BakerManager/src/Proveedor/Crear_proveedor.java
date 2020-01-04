/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Proveedor;

import Interface.InterfaceNotificarCambio;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 *
 * @author Ramiro
 */
public class Crear_proveedor {

    private M_crear_proveedor modelo;
    private V_crear_proveedor vista;
    private C_crear_proveedor controlador;

    public Crear_proveedor(JFrame frame) {
        this.modelo = new M_crear_proveedor();
        this.vista = new V_crear_proveedor(frame, true);
        this.controlador = new C_crear_proveedor(this.modelo, this.vista);
    }

    public Crear_proveedor(JDialog dialog) {
        this.modelo = new M_crear_proveedor();
        this.vista = new V_crear_proveedor(dialog, true);
        this.controlador = new C_crear_proveedor(this.modelo, this.vista);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }

    public void setInterface(InterfaceNotificarCambio interfaceNotificarCambio) {
        this.controlador.setInterfaceNotificarCambio(interfaceNotificarCambio);
    }

}
