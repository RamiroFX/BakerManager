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
public class PrevisionTemporal {
    
    private M_previsionTemporal modelo;
    private V_previsionTemporal vista;
    private C_previsionTemporal controlador;

    public PrevisionTemporal(JDialog dialog) {
        this.modelo = new M_previsionTemporal();
        this.vista = new V_previsionTemporal(dialog);
        this.controlador = new C_previsionTemporal(this.modelo, this.vista);
    }
    
    public void mostrarVista(){
        this.controlador.mostrarVista();;
    }
    
}
