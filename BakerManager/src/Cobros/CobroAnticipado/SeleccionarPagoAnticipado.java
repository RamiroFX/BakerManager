/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros.CobroAnticipado;

import Interface.RecibirCtaCteCabeceraCallback;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 *
 * @author Ramiro Ferreira
 */
public class SeleccionarPagoAnticipado {

    private M_seleccionarPagoAnticipado modelo;
    private V_seleccionarPagoAnticipado vista;
    private C_seleccionarPagoAnticipado controlador;

    public SeleccionarPagoAnticipado(JDialog dialog, int idCliente, RecibirCtaCteCabeceraCallback callback) {
        this.modelo = new M_seleccionarPagoAnticipado(idCliente);
        this.vista = new V_seleccionarPagoAnticipado(dialog);
        this.controlador = new C_seleccionarPagoAnticipado(modelo, vista, callback);
    }

    public SeleccionarPagoAnticipado(JFrame frame, int idCliente, RecibirCtaCteCabeceraCallback callback) {
        this.modelo = new M_seleccionarPagoAnticipado(idCliente);
        this.vista = new V_seleccionarPagoAnticipado(frame);
        this.controlador = new C_seleccionarPagoAnticipado(modelo, vista, callback);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
