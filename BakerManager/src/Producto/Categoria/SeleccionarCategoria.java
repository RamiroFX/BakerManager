/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto.Categoria;

import Interface.RecibirProductoCategoriaCallback;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 *
 * @author Ramiro Ferreira
 */
public class SeleccionarCategoria {

    private M_seleccionarCategoria modelo;
    private V_seleccionarCategoria vista;
    private C_seleccionarCategoria controlador;

    public SeleccionarCategoria(JFrame frame) {
        this.modelo = new M_seleccionarCategoria();
        this.vista = new V_seleccionarCategoria(frame);
        this.controlador = new C_seleccionarCategoria(this.modelo, this.vista);
    }

    public SeleccionarCategoria(JDialog dialog) {
        this.modelo = new M_seleccionarCategoria();
        this.vista = new V_seleccionarCategoria(dialog);
        this.controlador = new C_seleccionarCategoria(this.modelo, this.vista);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }

    public void setCallback(RecibirProductoCategoriaCallback callback) {
        this.modelo.setCallback(callback);
    }
}
