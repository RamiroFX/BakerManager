/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas;

/**
 *
 * @author Ramiro Ferreira
 */
public class CrearVentaRapida {

    C_crearVentaRapida controlador;
    M_crearVentaRapida modelo;
    V_crearVentaRapida vista;
    public CrearVentaRapida(C_gestionVentas gestionVentas) {
        this.vista = new V_crearVentaRapida(gestionVentas.c_inicio.vista, true);
        this.controlador = new C_crearVentaRapida(vista, gestionVentas);
    }

    void mostrarVista() {
        this.controlador.mostrarVista();
    }
    
}
