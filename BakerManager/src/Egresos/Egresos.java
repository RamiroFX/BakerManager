/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Egresos;

import MenuPrincipal.C_MenuPrincipal;
import bakermanager.C_inicio;

/**
 *
 * @author Usuario
 */
public class Egresos {

    M_Egresos modelo;
    V_crear_egreso vista;
    C_crear_egreso controlador;

    public Egresos(C_inicio c_inicio) {
        modelo = new M_Egresos();
        vista = new V_crear_egreso(c_inicio.vista, true);
        controlador = new C_crear_egreso(vista, modelo);
    }

    public Egresos(C_MenuPrincipal menuPrincipal) {
        modelo = new M_Egresos();
        vista = new V_crear_egreso(null, true);
        controlador = new C_crear_egreso(menuPrincipal, vista, modelo);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
