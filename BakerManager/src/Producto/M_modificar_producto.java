/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto;

import DB.DB_Producto;
import DB.DB_manager;
import Entities.M_producto;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro
 */
public class M_modificar_producto {

    public M_producto producto;

    public M_modificar_producto(int idProducto) {
        this.producto = DB_Producto.obtenerDatosProductoID(idProducto);

    }

    public Vector obtenerImpuesto() {
        return DB_manager.obtenerImpuesto();
    }

    public Vector obtenerRubro() {
        return DB_manager.obtenerCategoria();
    }

    public Vector obtenerMarca() {
        return DB_manager.obtenerMarca();
    }

    public Vector obtenerEstado() {
        return DB_manager.obtenerEstado();
    }

    public boolean actualizarProducto(M_producto producto) {
        if (producto.getCodBarra() != null) {
            if (DB_Producto.existeCodigo(producto.getCodBarra())) {
                if (!this.producto.getCodBarra().equals(producto.getCodBarra())) {
                    JOptionPane.showMessageDialog(null, "El código seleccionado se encuentra en uso. Escoga otro.", "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        }
        int id_categoria = DB_manager.obtenerIdProductoCategoria(producto.getCategoria().toLowerCase());
        int id_marca = DB_manager.obtenerIdMarca(producto.getMarca().toLowerCase());
        int id_impuesto = DB_manager.obtenerIdImpuesto(producto.getImpuesto());
        int id_estado = DB_manager.obtenerIdEstado(producto.getEstado().toLowerCase());
        producto.setIdCategoria(id_categoria);
        producto.setIdEstado(id_estado);
        producto.setIdImpuesto(id_impuesto);
        producto.setIdMarca(id_marca);
        producto.setId(this.producto.getId());
        return DB_Producto.modificarProducto(producto) == 1;
    }
}
