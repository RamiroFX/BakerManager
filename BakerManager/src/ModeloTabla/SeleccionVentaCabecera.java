/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Entities.E_facturaCabecera;
import Entities.E_facturaCabeceraFX;
import Entities.M_facturaCabecera;

/**
 *
 * @author Ramiro Ferreira
 */
public class SeleccionVentaCabecera {

    private E_facturaCabecera facturaCabecera;
    Boolean estaSeleccionado;

    public SeleccionVentaCabecera() {
        this.facturaCabecera = new E_facturaCabecera();
        this.estaSeleccionado = true;
    }

    public SeleccionVentaCabecera(E_facturaCabecera facturaCabecera, boolean estaSeleccionado) {
        this.facturaCabecera = facturaCabecera;
        this.estaSeleccionado = estaSeleccionado;
    }

    public boolean isEstaSeleccionado() {
        return estaSeleccionado;
    }

    public void setEstaSeleccionado(boolean estaSeleccionado) {
        this.estaSeleccionado = estaSeleccionado;
    }

    /**
     * @return the facturaCabecera
     */
    public E_facturaCabecera getFacturaCabecera() {
        return facturaCabecera;
    }

    /**
     * @param facturaCabecera the facturaCabecera to set
     */
    public void setFacturaCabecera(E_facturaCabecera facturaCabecera) {
        this.facturaCabecera = facturaCabecera;
    }

}
