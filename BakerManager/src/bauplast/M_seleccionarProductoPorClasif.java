/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast;

import DB.DB_Producto;
import DB.DB_manager;
import Entities.Estado;
import Entities.ProductoCategoria;
import ModeloTabla.SeleccionarProductoTableModel;
import java.util.ArrayList;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_seleccionarProductoPorClasif {

    private SeleccionarProductoTableModel tm;
    private ProductoCategoria pc;

    public M_seleccionarProductoPorClasif(int tipoSeleccionProducto) {
        this.tm = new SeleccionarProductoTableModel(tipoSeleccionProducto);
        this.pc = new ProductoCategoria();
    }

    public SeleccionarProductoTableModel getTm() {
        return tm;
    }

    public void setTm(SeleccionarProductoTableModel tm) {
        this.tm = tm;
    }

    public void setPc(ProductoCategoria pc) {
        this.pc = pc;
    }

    public ProductoCategoria getPc() {
        return pc;
    }

    public ArrayList<Estado> obtenerEstado() {
        return DB_manager.obtenerEstados();
    }

    public ArrayList<ProductoCategoria> obtenerCategorias() {
        return new ArrayList<>(DB_Producto.obtenerProductoCategoria());
    }

    public void consultarRollos(String descripcion, Estado estado, String buscarPor, ProductoCategoria categoria) {
        this.tm.setList(DB_Producto.consultarProductoPorClasificacion(descripcion, estado, buscarPor, categoria));
    }
}
