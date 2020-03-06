/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pagos;

import Entities.E_reciboPagoCabecera;
import javax.swing.JFrame;

/**
 *
 * @author Ramiro Ferreira
 */
public class VerPago {

    private M_verPago modelo;
    private V_crearPago vista;
    private C_verPago controlador;

    public VerPago(JFrame frame) {
        this.modelo = new M_verPago();
        this.vista = new V_crearPago(frame);
        this.controlador = new C_verPago(this.modelo, this.vista);
    }

    public void setReciboPagoCabecera(E_reciboPagoCabecera cabecera) {
        this.modelo.setCabecera(cabecera);
        this.modelo.actualizarDetalle(cabecera.getId());
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }

}
