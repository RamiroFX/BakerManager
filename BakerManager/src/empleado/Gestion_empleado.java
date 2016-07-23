/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package empleado;

import bakermanager.C_inicio;

/**
 *
 * @author Ramiro
 */
public class Gestion_empleado {

    private M_gestion_usuario modelo;
    private V_gestion_usuario vista;
    private C_gestion_usuario controlador;

    public Gestion_empleado(C_inicio C_inicio) {
        this.modelo = new M_gestion_usuario();
        this.vista = new V_gestion_usuario();
        this.controlador = new C_gestion_usuario(this.modelo, this.vista, C_inicio);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
