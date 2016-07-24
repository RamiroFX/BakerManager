/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Pedido;

/**
 *
 * @author Ramiro Ferreira
 */
public class CrearPedido {

    private M_crearPedido modelo;
    private V_crearPedido vista;
    private C_crearPedido controlador;
    private C_gestionPedido gestionPedido;

    public CrearPedido(C_gestionPedido gestionPedido) {
        this.gestionPedido = gestionPedido;
        this.modelo = new M_crearPedido();
        this.modelo.getPedido().setFuncionario(gestionPedido.c_inicio.modelo.getRol_usuario().getFuncionario());
        this.vista = new V_crearPedido(gestionPedido.c_inicio.vista);
        this.controlador = new C_crearPedido(this.modelo, this.vista, this.gestionPedido);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
