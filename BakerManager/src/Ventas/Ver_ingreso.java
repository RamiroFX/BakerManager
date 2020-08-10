/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas;

import bakermanager.C_inicio;
import javax.swing.JDialog;

/**
 *
 * @author Ramiro Ferreira
 */
public class Ver_ingreso {

    public V_crearVentaRapida vista;
    public C_ver_ingreso controlador;

    public Ver_ingreso(C_inicio c_inicio, Integer idIngresoCabecera, boolean esModoCreacion) {
        this.vista = new V_crearVentaRapida(c_inicio.vista, esModoCreacion);
        this.controlador = new C_ver_ingreso(idIngresoCabecera, this.vista);
    }

    public Ver_ingreso(JDialog c_inicio, Integer idIngresoCabecera) {
        this.vista = new V_crearVentaRapida(c_inicio);
        this.controlador = new C_ver_ingreso(idIngresoCabecera, this.vista);
    }

    public Ver_ingreso(Integer nroFactura, JDialog c_inicio) {
        this.vista = new V_crearVentaRapida(c_inicio);
        this.controlador = new C_ver_ingreso(this.vista, nroFactura);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
