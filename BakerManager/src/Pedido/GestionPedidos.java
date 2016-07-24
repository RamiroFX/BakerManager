/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Pedido;

import bakermanager.C_inicio;

/**
 *
 * @author Ramiro Ferreira
 */
public class GestionPedidos {

    private M_gestionPedido modelo;
    private V_gestionPedido vista;
    private C_gestionPedido controlador;

    public GestionPedidos(C_inicio c_inicio) {
        this.modelo = new M_gestionPedido();
        this.vista = new V_gestionPedido(c_inicio.vista);
        this.controlador = new C_gestionPedido(modelo, vista, c_inicio);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
