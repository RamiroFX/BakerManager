/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Entities.E_reciboPagoCabecera;

/**
 *
 * @author Ramiro Ferreira
 */
public class SeleccionPagoCabecera {

    private E_reciboPagoCabecera pago;
    private Boolean estaSeleccionado;

    public SeleccionPagoCabecera() {
        this.pago = new E_reciboPagoCabecera();
        this.estaSeleccionado = true;
    }

    public SeleccionPagoCabecera(E_reciboPagoCabecera pago, boolean estaSeleccionado) {
        this.pago = pago;
        this.estaSeleccionado = estaSeleccionado;
    }

    public boolean isSeleccionado() {
        return estaSeleccionado;
    }

    public void setEstaSeleccionado(boolean estaSeleccionado) {
        this.estaSeleccionado = estaSeleccionado;
    }

    public void setPago(E_reciboPagoCabecera pago) {
        this.pago = pago;
    }

    public E_reciboPagoCabecera getPago() {
        return pago;
    }

}
