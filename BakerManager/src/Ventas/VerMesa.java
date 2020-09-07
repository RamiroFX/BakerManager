/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas;

import Ventas.Mesas.M_verMesa;
import Ventas.Mesas.C_verMesa;

/**
 *
 * @author Ramiro Ferreira
 */
public class VerMesa {

    public M_verMesa modelo;
    public V_crearVentaRapida vista;
    public C_verMesa controlador;
    public C_crearVentas crearVentas;

    public VerMesa(C_crearVentas crearVentas, int idMesa) {
        this.crearVentas = crearVentas;
        this.modelo = new M_verMesa(idMesa);
        this.vista = new V_crearVentaRapida(crearVentas.gestionVentas.c_inicio.vista, true);
        this.controlador = new C_verMesa(this.modelo, this.vista, this.crearVentas);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
