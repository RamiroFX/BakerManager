/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast.crearProductoTerminado;

import DB.DB_Produccion;
import DB.DB_manager;
import Entities.Estado;
import ModeloTabla.RolloProducidoTableModel;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_seleccionarFilm {

    private RolloProducidoTableModel tm;

    public M_seleccionarFilm() {
        this.tm = new RolloProducidoTableModel();
    }

    public RolloProducidoTableModel getTm() {
        return tm;
    }

    public void setTm(RolloProducidoTableModel tm) {
        this.tm = tm;
    }

    public ArrayList<Estado> obtenerEstado() {
        return DB_manager.obtenerEstados();
    }

    public void consultarRollos(String descripcion, String buscarPor, String ordenarPor, String clasificarPor, String estado, boolean porFecha, Date fechaInicio, Date fechaFin) {
        this.tm.setList(DB_Produccion.consultarFilmDisponible(descripcion, buscarPor, ordenarPor, clasificarPor, estado, porFecha, fechaInicio, fechaFin));
    }
}
