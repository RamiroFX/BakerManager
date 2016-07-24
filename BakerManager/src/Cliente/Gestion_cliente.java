/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import bakermanager.C_inicio;

/**
 *
 * @author Ramiro Ferreira
 */
public class Gestion_cliente {

    V_gestion_cliente vista;
    C_gestion_cliente controlador;

    public Gestion_cliente(C_inicio inicio) {
        this.vista = new V_gestion_cliente();
        this.controlador = new C_gestion_cliente(inicio);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
