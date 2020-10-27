/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface;

import Entities.M_producto;

/**
 *
 * @author Ramiro
 */
public interface RecibirProductoCallback {

    public void recibirProducto(double cantidad, double precio, double descuento, M_producto producto, String observacion);

    public void modificarProducto(int posicion, double cantidad, double precio, double descuento, M_producto producto, String observacion);

}
