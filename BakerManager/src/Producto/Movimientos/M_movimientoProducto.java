/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto.Movimientos;

import DB.DB_Producto;
import Entities.M_producto;
import ModeloTabla.MovimientoProduccionTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_movimientoProducto {

    MovimientoProduccionTableModel cabeceraTableModel;

    public M_movimientoProducto() {
        cabeceraTableModel = new MovimientoProduccionTableModel();
    }

    public MovimientoProduccionTableModel obtenerTableModel() {
        return cabeceraTableModel;
    }

    public void obtenerMovimientos(int idProducto) {
        this.cabeceraTableModel.setList(DB_Producto.obtenerMovimientos(idProducto));
    }

    public M_producto obtenerProducto(int idProducto) {
        return DB_Producto.obtenerDatosProductoID(idProducto);
    }
}
