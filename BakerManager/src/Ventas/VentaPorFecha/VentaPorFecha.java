/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventas.VentaPorFecha;

import Ventas.M_crearVentaRapida;
import javax.swing.JFrame;

/**
 *
 * @author Ramiro Ferreira
 */
public class VentaPorFecha {

    private V_crearVentaPorFecha vista;
    private M_crearVentaRapida modelo;
    private C_crearVentaPorFecha controlador;

    public VentaPorFecha(JFrame frame) {
        this.modelo = new M_crearVentaRapida();
        this.vista = new V_crearVentaPorFecha(frame, true);
        this.controlador = new C_crearVentaPorFecha(modelo, vista);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }

}
