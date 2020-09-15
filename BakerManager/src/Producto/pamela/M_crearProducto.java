/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto.pamela;

import DB.DB_Producto;
import DB.DB_manager;
import Entities.E_Marca;
import Entities.E_impuesto;
import Entities.Estado;
import Entities.M_producto;
import Entities.ProductoCategoria;
import ModeloTabla.ClienteProductoTableModel;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_crearProducto {

    private ClienteProductoTableModel tableModel;
    //private M_producto producto;
    private ProductoCategoria productoCategoria;

    public M_crearProducto() {
        //this.producto = new M_producto();
        this.productoCategoria = new ProductoCategoria();
        this.productoCategoria.setId(-1);
        tableModel = new ClienteProductoTableModel();
    }

    public ProductoCategoria getProductoCategoria() {
        return productoCategoria;
    }

    public void setProductoCategoria(ProductoCategoria productoCategoria) {
        this.productoCategoria = productoCategoria;
    }

    public ClienteProductoTableModel getTableModel() {
        return tableModel;
    }

    public List<E_impuesto> obtenerImpuesto() {
        return DB_manager.obtenerImpuestos();
    }

    public List<E_Marca> obtenerMarca() {
        return DB_manager.obtenerMarcas();

    }

    public Vector obtenerRubro() {
        return DB_manager.obtenerCategoria();

    }

    public int obtenerUltimoIdProducto() {
        return DB_Producto.obtenerUltimoIdProducto();
    }

    public boolean existeProducto(String descripcion) {
        if (DB_Producto.existeProducto(descripcion)) {
            return true;
        }
        return false;
    }

    public boolean existeCodigo(String codigo) {
        if (DB_Producto.existeCodigo(codigo)) {
            return true;
        }
        return false;
    }

    public void crearProducto(M_producto producto) {
        producto.setIdCategoria(productoCategoria.getId());
        producto.setIdEstado(Estado.ACTIVO);
        DB_Producto.insertarProductoConClientes(producto, tableModel.getList());
    }

}
