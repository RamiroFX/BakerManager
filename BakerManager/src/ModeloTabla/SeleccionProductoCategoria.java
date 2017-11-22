/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloTabla;

import Entities.ProductoCategoria;

/**
 *
 * @author Ramiro Ferreira
 */
public class SeleccionProductoCategoria {

    ProductoCategoria productoCategoria;
    Boolean estaSeleccionado;

    public SeleccionProductoCategoria() {
        this.productoCategoria = new ProductoCategoria();
        this.estaSeleccionado = true;
    }

    public SeleccionProductoCategoria(ProductoCategoria productoCategoria, boolean estaSeleccionado) {
        this.productoCategoria = productoCategoria;
        this.estaSeleccionado = estaSeleccionado;
    }

    public ProductoCategoria getProductoCategoria() {
        return productoCategoria;
    }

    public void setProductoCategoria(ProductoCategoria productoCategoria) {
        this.productoCategoria = productoCategoria;
    }

    public boolean isEstaSeleccionado() {
        return estaSeleccionado;
    }

    public void setEstaSeleccionado(boolean estaSeleccionado) {
        this.estaSeleccionado = estaSeleccionado;
    }

}
