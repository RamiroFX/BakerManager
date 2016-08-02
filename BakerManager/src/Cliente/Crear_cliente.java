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

    private M_crear_cliente modelo;
    private V_crear_cliente vista;
    private C_crear_cliente controlador;

    public Crear_cliente(C_gestion_cliente gestion_cliente) {
        this.modelo = new M_crear_cliente();
        this.vista = new V_crear_cliente(gestion_cliente.c_inicio.vista, true);
        this.controlador = new C_crear_cliente(this.modelo, this.vista, gestion_cliente);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
