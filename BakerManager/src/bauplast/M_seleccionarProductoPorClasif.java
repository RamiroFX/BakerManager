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
    private E_productoClasificacion pc;

    public M_seleccionarProductoPorClasif() {
        this.tm = new SeleccionarProductoRolloTableModel();
        this.pc = new E_productoClasificacion();
    }

    public SeleccionarProductoRolloTableModel getTm() {
        return tm;
    }

    public void setTm(SeleccionarProductoRolloTableModel tm) {
        this.tm = tm;
    }

    public void setPc(E_productoClasificacion pc) {
        this.pc = pc;
    }

    public ArrayList<Estado> obtenerEstado() {
        return DB_manager.obtenerEstados();
    }

    public ArrayList<ProductoCategoria> obtenerCategorias() {
        //return DB_manager.obtenerCategorias();
        return new ArrayList<>(DB_Producto.obtenerProductoCategoria());
    }

    public void consultarRollos(String descripcion, Estado estado, String buscarPor, ProductoCategoria categoria) {
        pc.setId(categoria.getId());
        this.tm.setList(DB_Producto.consultarProductoPorClasificacion(descripcion, estado, buscarPor, pc));
    }
}
