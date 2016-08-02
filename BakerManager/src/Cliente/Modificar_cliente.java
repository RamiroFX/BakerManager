/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

/**
 *
 * @author Ramiro Ferreira
 */
public class Modificar_cliente {

    private M_modificar_cliente modelo;
    private V_crear_cliente vista;
    private C_modificar_cliente controlador;

    public Modificar_cliente(C_gestion_cliente gestionCliente, int idCliente) {
        this.vista = new V_crear_cliente(gestionCliente.c_inicio.vista, false);
        this.modelo = new M_modificar_cliente(idCliente);
        this.controlador = new C_modificar_cliente(this.modelo, this.vista, gestionCliente);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
