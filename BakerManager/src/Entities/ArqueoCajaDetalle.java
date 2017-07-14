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
public class ArqueoCajaDetalle {

    private int idArqueoCajaDetalle, idCaja, cantidad,idTipo;
    private Moneda moneda;

    public ArqueoCajaDetalle() {
        this.moneda = new Moneda();
    }

    public ArqueoCajaDetalle(int idArqueoCajaDetalle, int idCaja, Moneda moneda, int cantidad) {
        this.idArqueoCajaDetalle = idArqueoCajaDetalle;
        this.idCaja = idCaja;
        this.moneda = moneda;
        this.cantidad = cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setIdArqueoCajaDetalle(int idArqueoCajaDetalle) {
        this.idArqueoCajaDetalle = idArqueoCajaDetalle;
    }

    public int getIdArqueoCajaDetalle() {
        return idArqueoCajaDetalle;
    }

    public void setIdCaja(int idCaja) {
        this.idCaja = idCaja;
    }

    public int getIdCaja() {
        return idCaja;
    }

    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }

    public Moneda getMoneda() {
        return moneda;
    }

    public int getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(int idTipo) {
        this.idTipo = idTipo;
    }

}
