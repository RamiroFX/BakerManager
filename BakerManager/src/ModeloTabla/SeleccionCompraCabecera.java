/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Entities.M_egreso_cabecera;

/**
 *
 * @author Ramiro Ferreira
 */
public class SeleccionCompraCabecera {

    private M_egreso_cabecera egresoCabecera;
    Boolean estaSeleccionado;

    public SeleccionCompraCabecera() {
        this.egresoCabecera = new M_egreso_cabecera();
        this.estaSeleccionado = true;
    }

    public SeleccionCompraCabecera(M_egreso_cabecera egresoCabecera, boolean estaSeleccionado) {
        this.egresoCabecera = egresoCabecera;
        this.estaSeleccionado = estaSeleccionado;
    }

    public boolean isEstaSeleccionado() {
        return estaSeleccionado;
    }

    public void setEstaSeleccionado(boolean estaSeleccionado) {
        this.estaSeleccionado = estaSeleccionado;
    }

    public M_egreso_cabecera getFacturaCabecera() {
        return egresoCabecera;
    }

    public void setFacturaCabecera(M_egreso_cabecera egresoCabecera) {
        this.egresoCabecera = egresoCabecera;
    }

}
