/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast.desperdicio;

import javax.swing.JFrame;

/**
 *
 * @author Ramiro Ferreira
 */
public class CrearDesperdicioRapido {
    
    private M_crearDesperdicioRapido modelo;
    private V_crearDesperdicioRapido vista;

    public CrearDesperdicioRapido(JFrame frame) {
        this.modelo = new M_crearDesperdicioRapido();
        this.vista = new V_crearDesperdicioRapido(frame);
        //this.controlador = new C_crearDesperdicioRapido(modelo, vista);
    }
    
    public void mostarVista(){
        //this.controlador.mostrarVista();
    }
}
