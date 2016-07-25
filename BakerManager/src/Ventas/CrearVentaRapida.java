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
    public CrearVentaRapida(C0_gestionVentas gestionVentas) {
        this.modelo = new M_crearVentaRapida();
        this.vista = new V_crearVentaRapida(gestionVentas.c_inicio.vista);
        this.controlador = new C_crearVentaRapida(modelo, vista, gestionVentas);
    }

    void mostrarVista() {
        this.controlador.mostrarVista();
    }
    
}
