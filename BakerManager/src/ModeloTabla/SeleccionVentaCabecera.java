/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Entities.E_facturaCabeceraFX;
import Entities.M_facturaCabecera;

/**
 *
 * @author Ramiro Ferreira
 */
public class SeleccionVentaCabecera {

    private E_facturaCabeceraFX facturaCabecera;
    Boolean estaSeleccionado;

    public SeleccionVentaCabecera() {
        this.facturaCabecera = new E_facturaCabeceraFX();
        this.estaSeleccionado = true;
    }

    public SeleccionVentaCabecera(E_facturaCabeceraFX facturaCabecera, boolean estaSeleccionado) {
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
    public E_facturaCabeceraFX getFacturaCabecera() {
        return facturaCabecera;
    }

    /**
     * @param facturaCabecera the facturaCabecera to set
     */
    public void setFacturaCabecera(E_facturaCabeceraFX facturaCabecera) {
        this.facturaCabecera = facturaCabecera;
    }

}
