/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Produccion;

import Entities.E_produccionDetalle;
import Entities.M_producto;
import ModeloTabla.ProduccionTableModel;

/**
 *
 * @author Ramiro
 */
public class M_crearProduccion {

    ProduccionTableModel tm;

    public M_crearProduccion() {
        this.tm = new ProduccionTableModel();
    }

    public void setTm(ProduccionTableModel tm) {
        this.tm = tm;
    }

    public ProduccionTableModel getTm() {
        return tm;
    }

    public void agregarDetalle(double cantidad,M_producto producto) {
        E_produccionDetalle produccion = new E_produccionDetalle();
        produccion.setCantidad(cantidad);
        produccion.setProducto(producto);
        getTm().agregarDetalle(produccion);
    }

}
