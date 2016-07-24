/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Pedido;

/**
 *
 * @author Ramiro Ferreira
 */
public class VerPedido {

    private V_crearPedido vista;
    private M_verPedido modelo;
    private C_verPedido controlador;

    public VerPedido(C_gestionPedido controlador, int idPedido) {
        this.vista = new V_crearPedido(controlador.c_inicio.vista);
        this.modelo = new M_verPedido(idPedido);
        this.controlador = new C_verPedido(vista, modelo, controlador);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
