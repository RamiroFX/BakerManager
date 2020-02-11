/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros;

import Entities.E_cuentaCorrienteCabecera;
import javax.swing.JFrame;

/**
 *
 * @author Ramiro Ferreira
 */
public class VerCobro {

    private M_verCobro modelo;
    private V_crearCobro vista;
    private C_verCobro controlador;

    public VerCobro(JFrame frame) {
        this.modelo = new M_verCobro();
        this.vista = new V_crearCobro(frame);
        this.controlador = new C_verCobro(this.modelo, this.vista);
    }

    public void setCtaCteCabecera(E_cuentaCorrienteCabecera cabecera) {
        this.modelo.setCabecera(cabecera);
        this.modelo.actualizarDetalle(cabecera.getId());
    }

    public void mostrarVista() {
        this.controlador.mostrarVista();
    }

}
