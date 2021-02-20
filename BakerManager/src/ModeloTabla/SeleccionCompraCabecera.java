/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Entities.M_egresoCabecera;

/**
 *
 * @author Ramiro Ferreira
 */
public class SeleccionCompraCabecera {

    private M_egresoCabecera egresoCabecera;
    Boolean estaSeleccionado;

    public SeleccionCompraCabecera() {
        this.egresoCabecera = new M_egresoCabecera();
        this.estaSeleccionado = true;
    }

    public SeleccionCompraCabecera(M_egresoCabecera egresoCabecera, boolean estaSeleccionado) {
        this.egresoCabecera = egresoCabecera;
        this.estaSeleccionado = estaSeleccionado;
    }

    public boolean isEstaSeleccionado() {
        return estaSeleccionado;
    }

    public void setEstaSeleccionado(boolean estaSeleccionado) {
        this.estaSeleccionado = estaSeleccionado;
    }

    public M_egresoCabecera getFacturaCabecera() {
        return egresoCabecera;
    }

    public void setFacturaCabecera(M_egresoCabecera egresoCabecera) {
        this.egresoCabecera = egresoCabecera;
    }

}
