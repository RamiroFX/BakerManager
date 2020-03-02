/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast;

import DB.DB_Producto;
import DB.DB_manager;
import Entities.Estado;
import ModeloTabla.SeleccionarProductoRolloTableModel;
import java.util.ArrayList;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_seleccionarRollo {

    SeleccionarProductoRolloTableModel tm;

    public M_seleccionarRollo() {
        this.tm = new SeleccionarProductoRolloTableModel();
        inicializarModelo();
    }

    private void inicializarModelo() {
        Estado estado = new Estado(Estado.ACTIVO, "Activo");
        consultarRollos("", estado, "ID");
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
        this.tm.setList(DB_Producto.consultarRollos(descripcion, estado, ordenarPor));
    }
}
