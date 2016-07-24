/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Egresos;

import bakermanager.C_inicio;

/**
 *
 * @author Usuario
 */
public class Ver_Egresos {
    M_Egresos modelo;
    V_Ver_Egresos vista;
    C_ver_egreso controlador;

    public Ver_Egresos(C_inicio c_inicio, int IdEgresoCabecera) {
        modelo = new M_Egresos();
        vista = new V_Ver_Egresos(c_inicio.vista, true);
        controlador = new C_ver_egreso(IdEgresoCabecera,vista, modelo);
    }
    
    public void mostrarVista(){
        this.controlador.mostrarVista();
    }
}
