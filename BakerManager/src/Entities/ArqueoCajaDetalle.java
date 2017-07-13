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

    int idArqueoCajaDetalle, idCaja, idMoneda, cantidad;

    public ArqueoCajaDetalle() {
    }

    public ArqueoCajaDetalle(int idArqueoCajaDetalle, int idCaja, int idMoneda, int cantidad) {
        this.idArqueoCajaDetalle = idArqueoCajaDetalle;
        this.idCaja = idCaja;
        this.idMoneda = idMoneda;
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

    public void setIdMoneda(int idMoneda) {
        this.idMoneda = idMoneda;
    }

    public int getIdMoneda() {
        return idMoneda;
    }

}
