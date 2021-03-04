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
public class PrevisionStock {
    
    private M_previsionStock modelo;
    private V_previsionStock vista;
    private C_previsionStock controlador;

    public PrevisionStock(JDialog dialog) {
        this.modelo = new M_previsionStock();
        this.vista = new V_previsionStock(dialog);
        this.controlador = new C_previsionStock(this.modelo, this.vista);
    }
    
    public void mostrarVista(){
        this.controlador.mostrarVista();;
    }
    
}
