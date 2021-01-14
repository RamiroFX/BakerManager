/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros.CobroAnticipado;

import javax.swing.JDialog;

/**
 *
 * @author Ramiro Ferreira
 */
public class SeleccionarPagoAnticipado {
    
    private M_seleccionarPagoAnticipado modelo;
    private V_seleccionarPagoAnticipado vista;
    private C_seleccionarPagoAnticipado controlador;

    public SeleccionarPagoAnticipado(JDialog dialog) {
        this.modelo = new M_seleccionarPagoAnticipado();
        this.vista = new V_seleccionarPagoAnticipado(dialog);
        this.controlador = new C_seleccionarPagoAnticipado(modelo, vista);
    }
    
    public void mostrarVista(){
        this.controlador.mostrarVista();
    }
}
