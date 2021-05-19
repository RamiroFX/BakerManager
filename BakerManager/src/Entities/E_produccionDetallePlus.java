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
public class E_produccionDetallePlus extends E_produccionDetalle {

    private double cantidadVendida;
    private double balanceAnterior;

    public E_produccionDetallePlus(double cantidadVendida) {
        this.cantidadVendida = cantidadVendida;
    }

    public E_produccionDetallePlus() {
    }

    public double getCantidadVendida() {
        return cantidadVendida;
    }

    public void setCantidadVendida(double cantidadVendida) {
        this.cantidadVendida = cantidadVendida;
    }

    public double getBalanceAnterior() {
        return balanceAnterior;
    }

    public void setBalanceAnterior(double balanceAnterior) {
        this.balanceAnterior = balanceAnterior;
    }

}
