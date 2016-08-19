/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto;

import DB.DB_Producto;
import DB.DB_Proveedor;
import DB.DB_manager;
import Entities.M_producto;
import Entities.M_proveedor;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramiro
 */
class M_crear_producto {

    public M_producto producto;
    M_proveedor proveedor;

    public M_crear_producto() {

        this.producto = new M_producto();
        this.proveedor = new M_proveedor();
    }

    public Vector obtenerImpuesto() {
        return DB_manager.obtenerImpuesto();

    }

    public Vector obtenerMarca() {
        return DB_manager.obtenerMarca();

    }

    public Vector obtenerRubro() {
        return DB_manager.obtenerCategoria();

    }

    public boolean crearProducto(M_producto producto, boolean tieneProveedor) {
        if (DB_Producto.existeProducto(producto.getDescripcion())) {
            javax.swing.JOptionPane.showMessageDialog(null, "El nombre del producto se encuentra en uso. Verifique el nombre del producto", "Parametros incorrectos",
                    javax.swing.JOptionPane.OK_OPTION);
            return false;
        }
        if (tieneProveedor) {
            try {
                if (null == proveedor) {
                    JOptionPane.showMessageDialog(null, "Seleccione un proveedor", "Atención", JOptionPane.PLAIN_MESSAGE);
                    return false;
                } else if (proveedor.getId() < 0) {
                    JOptionPane.showMessageDialog(null, "Seleccione un proveedor", "Atención", JOptionPane.PLAIN_MESSAGE);
                    return false;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Seleccione un proveedor", "Atención", JOptionPane.PLAIN_MESSAGE);
                return false;
            }
        }
        int idCategoria = DB_manager.obtenerIdProductoCategoria(producto.getCategoria());
        int idMarca = DB_manager.obtenerIdMarca(producto.getMarca());
        int idImpuesto = DB_manager.obtenerIdImpuesto(producto.getImpuesto().toString());
        producto.setIdCategoria(idCategoria);
        producto.setIdEstado(1);//Activo
        producto.setIdImpuesto(idImpuesto);
        producto.setIdMarca(idMarca);
        long id_producto = -1;
        if (!tieneProveedor) {
            id_producto = DB_Producto.insertarProducto(producto);
            DB_Producto.insertarCodigoProducto(id_producto);
            return true;
        }
        if (tieneProveedor) {
            id_producto = DB_Producto.insertarProducto(producto);
            DB_Producto.insertarCodigoProducto(id_producto);
            DB_Proveedor.insertarProveedorProducto(proveedor.getId(), (int) id_producto);
            return true;
        }
        return false;
    }
}
