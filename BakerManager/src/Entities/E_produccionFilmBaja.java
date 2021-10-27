/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.util.Date;

/**
 *
 * @author Ramiro Ferreira
 */
public class E_produccionFilmBaja {

    private int id;
    private E_produccionCabecera produccionCabecera;
    private Double pesoUtilizado;
    private Date fechaUtilizacion;
    private M_producto producto;

    public E_produccionFilmBaja() {
        this.producto = new M_producto();
        this.produccionCabecera = new E_produccionCabecera();
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public void setProducto(M_producto producto) {
        this.producto = producto;
    }

    public M_producto getProducto() {
        return producto;
    }
    
    public Double getPesoUtilizado() {
        return pesoUtilizado;
    }
    
    public void setPesoUtilizado(Double pesoUtilizado) {
        this.pesoUtilizado = pesoUtilizado;
    }

    public Date getFechaUtilizacion() {
        return fechaUtilizacion;
    }

    public void setFechaUtilizacion(Date fechaUtilizacion) {
        this.fechaUtilizacion = fechaUtilizacion;
    }

    public E_produccionCabecera getProduccionCabecera() {
        return produccionCabecera;
    }

    public void setProduccionCabecera(E_produccionCabecera produccionCabecera) {
        this.produccionCabecera = produccionCabecera;
    }    

}
