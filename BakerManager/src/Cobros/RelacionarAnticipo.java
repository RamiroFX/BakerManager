/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros;

import Cobros.CobroAnticipado.C_relacionarAnticipo;
import Cobros.CobroAnticipado.M_relacionarAnticipo;
import Cobros.CobroAnticipado.V_relacionarAnticipo;
import bakermanager.C_inicio;
import javax.swing.JFrame;

/**
 *
 * @author Ramiro Ferreira
 */
public class RelacionarAnticipo {
    
    private M_relacionarAnticipo modelo;
    private V_relacionarAnticipo vista;
    private C_relacionarAnticipo controlador;

    public RelacionarAnticipo(C_inicio inicio) {
        this.modelo = new M_relacionarAnticipo();
        this.vista = new V_relacionarAnticipo(inicio.vista);
        this.controlador = new C_relacionarAnticipo(modelo, vista, inicio);
    }
    
    public void mostrarVista(){
        this.controlador.mostrarVista();
    }
    
}
