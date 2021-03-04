/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto.AjusteStock;

import javax.swing.JDialog;

/**
 *
 * @author Ramiro Ferreira
 */
public class CrearAjuste {

    private M_crearAjuste modelo;
    private V_crearAjuste vista;
    private C_crearAjuste controlador;

    public CrearAjuste(JDialog dialog) {
        this.modelo = new M_crearAjuste();
        this.vista = new V_crearAjuste(dialog);
        this.controlador = new C_crearAjuste(this.modelo, this.vista);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
