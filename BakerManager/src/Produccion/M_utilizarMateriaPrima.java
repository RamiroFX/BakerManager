/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Produccion;

import Entities.E_produccionDetalle;
import Entities.M_producto;
import ModeloTabla.ProduccionDetalleTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
class M_utilizarMateriaPrima {

    ProduccionDetalleTableModel tm;

    public M_utilizarMateriaPrima() {
        this.tm = new ProduccionDetalleTableModel();
    }

    public void setTm(ProduccionDetalleTableModel tm) {
        this.tm = tm;
    }

    public ProduccionDetalleTableModel getTm() {
        return tm;
    }

    public void agregarDetalle(double cantidad, M_producto producto) {
        E_produccionDetalle produccion = new E_produccionDetalle();
        produccion.setCantidad(cantidad);
        produccion.setProducto(producto);
        getTm().agregarDetalle(produccion);
    }

    public void modificarDetalle(int index, double cantidad) {
        getTm().modificarCantidadDetalle(index, cantidad);
    }

    public void removerDetalle(int index) {
        getTm().quitarDetalle(index);
    }

}
