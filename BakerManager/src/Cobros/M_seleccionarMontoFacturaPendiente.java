/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros;

import Entities.E_cuentaCorrienteDetalle;
import Entities.E_facturaSinPago;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_seleccionarMontoFacturaPendiente {

    private E_cuentaCorrienteDetalle corrienteDetalle;
    private E_facturaSinPago e_facturaSinPago;

    public M_seleccionarMontoFacturaPendiente() {
        this.corrienteDetalle = new E_cuentaCorrienteDetalle();
        this.e_facturaSinPago = new E_facturaSinPago();
    }

    public E_cuentaCorrienteDetalle getCorrienteDetalle() {
        return corrienteDetalle;
    }

    public void setCorrienteDetalle(E_cuentaCorrienteDetalle corrienteDetalle) {
        this.corrienteDetalle = corrienteDetalle;
    }

    public E_facturaSinPago getE_facturaSinPago() {
        return e_facturaSinPago;
    }

    public void setE_facturaSinPago(E_facturaSinPago e_facturaSinPago) {
        this.e_facturaSinPago = e_facturaSinPago;
    }

}
