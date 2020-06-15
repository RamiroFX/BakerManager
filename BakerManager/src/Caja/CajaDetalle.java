/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Caja;

import javax.swing.JDialog;

/**
 *
 * @author Ramiro Ferreira
 */
public class CajaDetalle {

    private V_cajaDetalle vista;
    private M_cajaDetalle modelo;
    private C_cajaDetalle controlador;

    public CajaDetalle(JDialog owner) {
        this.vista = new V_cajaDetalle(owner);
        this.modelo = new M_cajaDetalle();
        this.controlador = new C_cajaDetalle(vista, modelo);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
