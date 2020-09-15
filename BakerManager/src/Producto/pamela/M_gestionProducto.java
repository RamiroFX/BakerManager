/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto.pamela;

import DB.DB_Producto;
import Entities.M_producto;
import Entities.M_proveedor;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_gestionProducto {

    private M_producto producto;
    private M_proveedor proveedor;

    public M_gestionProducto() {
        this.producto = new M_producto();
        this.proveedor = new M_proveedor();
    }

    public M_producto getProducto() {
        return producto;
    }

    public void setProducto(M_producto producto) {
        this.producto = producto;
    }

    public M_proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(M_proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public void establecerProducto(int idProducto) {
        this.producto = DB_Producto.obtenerDatosProductoID(idProducto);
    }
}
