/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas;

/**
 *
 * @author Ramiro Ferreira
 */
public class Ver_mesa {

    public M_verMesa modelo;
    public V_crearVentaRapida vista;
    public C_verMesa controlador;
    public C_crearVentas crearVentas;

    public Ver_mesa(C_crearVentas crearVentas, int idMesa) {
        this.crearVentas = crearVentas;
        this.modelo = new M_verMesa(idMesa);
        this.vista = new V_crearVentaRapida(crearVentas.gestionVentas.c_inicio.vista);
        this.controlador = new C_verMesa(this.modelo, this.vista, this.crearVentas);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
