/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto.Categoria;

import DB.DB_Producto;
import Interface.RecibirProductoCategoriaCallback;
import ModeloTabla.ProductoCategoriaTableModel;
import ModeloTabla.ProductoSubCategoriaTableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_seleccionarCategoria {

    private ProductoCategoriaTableModel productoCategoriaTm;
    private ProductoSubCategoriaTableModel productoSubCategoriaTm;
    private RecibirProductoCategoriaCallback callback;

    public M_seleccionarCategoria() {
        this.productoCategoriaTm = new ProductoCategoriaTableModel();
        this.productoSubCategoriaTm = new ProductoSubCategoriaTableModel();
    }

    public void setCallback(RecibirProductoCategoriaCallback callback) {
        this.callback = callback;
    }

    public RecibirProductoCategoriaCallback getCallback() {
        return callback;
    }

    public ProductoCategoriaTableModel getProductoCategoriaTm() {
        return productoCategoriaTm;
    }

    public ProductoSubCategoriaTableModel getProductoSubCategoriaTm() {
        return productoSubCategoriaTm;
    }

    public void actualizarProductoCategoria() {
        this.productoCategoriaTm.setList(DB_Producto.obtenerProductoCategoria());
    }

    public void actualizarProductoSubCategoria() {
        this.productoSubCategoriaTm.setList(DB_Producto.obtenerProductoSubCategoria());
    }

    public void actualizarProductoSubCategoria(int idCategoria) {
        this.productoSubCategoriaTm.setList(DB_Producto.obtenerProductoSubCategoria(idCategoria));
    }
}
