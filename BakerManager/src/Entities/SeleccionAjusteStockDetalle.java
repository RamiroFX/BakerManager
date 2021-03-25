/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

/**
 *
 * @author Ramiro Ferreira
 */
public class SeleccionAjusteStockDetalle extends E_ajusteStockDetalle {

    boolean estaSeleccionado;

    public SeleccionAjusteStockDetalle() {
        super();
        this.estaSeleccionado = true;
    }

    public SeleccionAjusteStockDetalle(boolean estaSeleccionado) {
        super();
        this.estaSeleccionado = estaSeleccionado;
    }

    public boolean isEstaSeleccionado() {
        return estaSeleccionado;
    }

    public void setEstaSeleccionado(boolean estaSeleccionado) {
        this.estaSeleccionado = estaSeleccionado;
    }

}
