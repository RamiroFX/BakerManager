/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas;

import Ventas.VentaPorFecha.V_crearVentaPorFecha;
import javax.swing.JFrame;

/**
 *
 * @author Ramiro Ferreira
 */
public class CrearVentaPorFecha {

    M_crearVentaRapida modelo;
    V_crearVentaPorFecha vista;
    C_crearVentaRapida controlador;

    public CrearVentaPorFecha(JFrame frame) {
        this.modelo = new M_crearVentaRapida();
        this.vista = new V_crearVentaPorFecha(frame, true);
        this.controlador = new C_crearVentaRapida(modelo, vista);
    }

    public void mostrarVista() {
        controlador.mostrarVista();
    }
}
