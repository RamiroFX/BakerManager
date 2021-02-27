/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto.AjusteStock;

import javax.swing.JFrame;

/**
 *
 * @author Ramiro Ferreira
 */
public class GestionAjusteStock {
    
    M_gestionAjusteStock modelo;
    V_gestionAjusteStock vista;
    C_gestionAjusteStock controlador;

    public GestionAjusteStock(JFrame frame) {
        this.modelo  = new M_gestionAjusteStock();
        this.vista  = new V_gestionAjusteStock(frame);
        this.controlador  = new C_gestionAjusteStock(modelo, vista);
    }
    
    

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
    
}
