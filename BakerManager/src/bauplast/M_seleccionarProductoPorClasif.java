/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast;

import DB.DB_Producto;
import DB.DB_manager;
import Entities.E_productoClasificacion;
import Entities.Estado;
import Entities.ProductoCategoria;
import ModeloTabla.SeleccionarProductoRolloTableModel;
import java.util.ArrayList;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_seleccionarProductoPorClasif {

    private SeleccionarProductoRolloTableModel tm;
    private ProductoCategoria pc;

    public M_seleccionarProductoPorClasif() {
        this.tm = new SeleccionarProductoRolloTableModel();
        this.pc = new ProductoCategoria();
    }

    public SeleccionarProductoRolloTableModel getTm() {
        return tm;
    }

    public void setTm(SeleccionarProductoRolloTableModel tm) {
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
