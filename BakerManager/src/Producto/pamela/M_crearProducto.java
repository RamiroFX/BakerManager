/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Producto.pamela;

import DB.DB_Producto;
import DB.DB_manager;
import Entities.E_impuesto;
import ModeloTabla.ClienteProductoTableModel;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_crearProducto {

    private ClienteProductoTableModel tableModel;

    public M_crearProducto() {
        tableModel = new ClienteProductoTableModel();
    }

    public ClienteProductoTableModel getTableModel() {
        return tableModel;
    }

    public List<E_impuesto> obtenerImpuesto() {
        return DB_manager.obtenerImpuestos();
    }

    public Vector obtenerMarca() {
        return DB_manager.obtenerMarca();

    }

    public Vector obtenerRubro() {
        return DB_manager.obtenerCategoria();

    }

    public int obtenerUltimoIdProducto() {
        return DB_Producto.obtenerUltimoIdProducto();
    }
}
