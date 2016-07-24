/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas;

/**
 *
 * @author Ramiro Ferreira
 */
public class CrearVentas {

    M_crearVentas modelo;
    V_crearVentas vista;
    C_crearVentas controlador;

    public CrearVentas(C_gestionVentas gestionVentas) {
        this.vista = new V_crearVentas(gestionVentas.c_inicio.vista);
        this.modelo = new M_crearVentas();
        this.controlador = new C_crearVentas(this.vista, this.modelo, gestionVentas);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
