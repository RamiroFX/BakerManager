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
import ModeloTabla.SeleccionarProductoRolloTableModel;
import java.util.ArrayList;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_seleccionarFilm {

    private SeleccionarProductoRolloTableModel tm;

    public M_seleccionarFilm() {
        this.tm = new SeleccionarProductoRolloTableModel();
    }

    public SeleccionarProductoRolloTableModel getTm() {
        return tm;
    }

    public void setTm(SeleccionarProductoRolloTableModel tm) {
        this.tm = tm;
    }


    public ArrayList<Estado> obtenerEstado() {
        return DB_manager.obtenerEstados();
    }

    public void consultarRollos(String descripcion, Estado estado, String ordenarPor) {
//        this.tm.setList(DB_Producto.consultarProductoPorClasificacion(descripcion, estado, ordenarPor, pc));
    }
}
