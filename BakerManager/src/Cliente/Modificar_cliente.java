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

    V_crear_cliente vista;
    C_modificar_cliente controlador;

    public Modificar_cliente(C_gestion_cliente gestionCliente) {
        vista = new V_crear_cliente(gestionCliente.c_inicio.vista, false);
        controlador = new C_modificar_cliente(gestionCliente, gestionCliente.getCliente());
    }

    public void mostrarVista() {
        controlador.mostrarVista();
    }
}
