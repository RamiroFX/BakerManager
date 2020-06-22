/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Entities.E_cuentaCorrienteCabecera;

/**
 *
 * @author Ramiro Ferreira
 */
public class SeleccionCobroCabecera {

    private E_cuentaCorrienteCabecera cobro;
    Boolean estaSeleccionado;

    public SeleccionCobroCabecera() {
        this.cobro = new E_cuentaCorrienteCabecera();
        this.estaSeleccionado = true;
    }

    public SeleccionCobroCabecera(E_cuentaCorrienteCabecera cobro, boolean estaSeleccionado) {
        this.cobro = cobro;
        this.estaSeleccionado = estaSeleccionado;
    }

    public boolean isSeleccionado() {
        return estaSeleccionado;
    }

    public void setEstaSeleccionado(boolean estaSeleccionado) {
        this.estaSeleccionado = estaSeleccionado;
    }

    public void setCobro(E_cuentaCorrienteCabecera cobro) {
        this.cobro = cobro;
    }

    public E_cuentaCorrienteCabecera getCobro() {
        return cobro;
    }

}
