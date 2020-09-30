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
public class E_produccionDesperdicioDetalle {

    private int id;
    private M_producto producto;
    private E_produccionTipoBaja tipoBaja;
    private double cantidad;
    private String observacion;

    public E_produccionDesperdicioDetalle() {
        this.producto = new M_producto();
        this.tipoBaja = new E_produccionTipoBaja();
    }

    public E_produccionDesperdicioDetalle(E_produccionDetalle unTerminado) {
        this.producto = unTerminado.getProducto();
        this.tipoBaja = new E_produccionTipoBaja();
        this.cantidad = unTerminado.getCantidad();
    }

    public E_produccionDesperdicioDetalle(E_produccionFilm unRollo) {
        this.producto = unRollo.getProducto();
        this.tipoBaja = new E_produccionTipoBaja();
        this.cantidad = unRollo.getPeso();
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the producto
     */
    public M_producto getProducto() {
        return producto;
    }

    /**
     * @param producto the producto to set
     */
    public void setProducto(M_producto producto) {
        this.producto = producto;
    }

    /**
     * @return the tipoBaja
     */
    public E_produccionTipoBaja getTipoBaja() {
        return tipoBaja;
    }

    /**
     * @param tipoBaja the tipoBaja to set
     */
    public void setTipoBaja(E_produccionTipoBaja tipoBaja) {
        this.tipoBaja = tipoBaja;
    }

    /**
     * @return the cantidad
     */
    public double getCantidad() {
        return cantidad;
    }

    /**
     * @param cantidad the cantidad to set
     */
    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * @return the observacion
     */
    public String getObservacion() {
        return observacion;
    }

    /**
     * @param observacion the observacion to set
     */
    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

}