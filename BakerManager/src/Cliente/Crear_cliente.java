/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

/**
 *
 * @author Ramiro Ferreira
 */
public class Crear_cliente {

    V_crear_cliente vista;
    C_crear_cliente controlador;

    public Crear_cliente(C_gestion_cliente gestion_cliente) {
        vista = new V_crear_cliente(gestion_cliente.c_inicio.vista, true);
        controlador = new C_crear_cliente(gestion_cliente);
    }

    public void mostrarVista() {
        controlador.mostrarVista();
    }
}