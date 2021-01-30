/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Egresos;

import bakermanager.C_inicio;
import javax.swing.JDialog;

/**
 *
 * @author Usuario
 */
public class Ver_Egresos {

    M_Egresos modelo;
    V_crearEgresoPorFecha vista;
    C_ver_egreso controlador;

    public Ver_Egresos(C_inicio c_inicio, int IdEgresoCabecera) {
        modelo = new M_Egresos();
        vista = new V_crearEgresoPorFecha(c_inicio.vista);
        controlador = new C_ver_egreso(IdEgresoCabecera, vista, modelo);
    }

    public Ver_Egresos(JDialog jdialog, int IdEgresoCabecera) {
        modelo = new M_Egresos();
        vista = new V_crearEgresoPorFecha(jdialog);
        controlador = new C_ver_egreso(IdEgresoCabecera, vista, modelo);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
