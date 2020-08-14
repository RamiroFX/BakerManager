/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros.Retencion;

import Entities.E_retencionVenta;
import javax.swing.JFrame;

/**
 *
 * @author Ramiro Ferreira
 */
public class VerRetencion {

    private M_verRetencion modelo;
    private V_crearRetencion vista;
    private C_verRetencion controlador;

    public VerRetencion(JFrame frame) {
        this.modelo = new M_verRetencion();
        this.vista = new V_crearRetencion(frame);
        this.controlador = new C_verRetencion(modelo, vista);
    }

    public void cargarDatos(E_retencionVenta cab) {
        this.controlador.cargarDatos(cab);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }

}
