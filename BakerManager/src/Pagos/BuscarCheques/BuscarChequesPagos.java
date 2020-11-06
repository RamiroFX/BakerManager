/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pagos.BuscarCheques;

import javax.swing.JFrame;

/**
 *
 * @author Ramiro Ferreira
 */
public class BuscarChequesPagos {

    private M_buscarChequesPagos modelo;
    private V_buscarChequesPagos vista;
    private C_buscarChequesPagos controlador;

    public BuscarChequesPagos(JFrame frame) {
        this.modelo = new M_buscarChequesPagos();
        this.vista = new V_buscarChequesPagos(frame);
        this.controlador = new C_buscarChequesPagos(modelo, vista);
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }
}
