/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros.Retencion;

import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 *
 * @author Ramiro Ferreira
 */
public class CrearRetencionVenta {

    M_crearRetencion modelo;
    V_crearRetencion vista;
    C_crearRetencion controlador;

    public CrearRetencionVenta(JFrame frame) {
        this.modelo = new M_crearRetencion();
        this.vista = new V_crearRetencion(frame);
        this.controlador = new C_crearRetencion(modelo, vista);
    }

    public CrearRetencionVenta(JDialog dialog) {
        this.modelo = new M_crearRetencion();
        this.vista = new V_crearRetencion(dialog);
        this.controlador = new C_crearRetencion(modelo, vista);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
