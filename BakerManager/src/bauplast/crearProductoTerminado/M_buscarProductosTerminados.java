/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast.crearProductoTerminado;

import DB.DB_Produccion;
import DB.DB_manager;
import Entities.Estado;
import ModeloTabla.ProduccionTerminadosTableModel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_buscarProductosTerminados {

    private ProduccionTerminadosTableModel tm;

    public M_buscarProductosTerminados() {
        this.tm = new ProduccionTerminadosTableModel();
    }

    public ProduccionTerminadosTableModel getTm() {
        return tm;
    }

    public void setTm(ProduccionTerminadosTableModel tm) {
        this.tm = tm;
    }

    public ArrayList<Estado> obtenerEstado() {
        return DB_manager.obtenerEstados();
    }

    public void consultarProductosTerminados(String descripcion, String buscarPor, String ordenarPor, String clasificarPor, String categoria,
            boolean porFecha, Date fechaInicio, Date fechaFinal) {
        Calendar calendarInicio = Calendar.getInstance();
        calendarInicio.setTime(fechaInicio);
        calendarInicio.set(Calendar.HOUR_OF_DAY, 0);
        calendarInicio.set(Calendar.MINUTE, 0);
        calendarInicio.set(Calendar.SECOND, 0);
        calendarInicio.set(Calendar.MILLISECOND, 0);
        Calendar calendarFinal = Calendar.getInstance();
        calendarFinal.setTime(fechaFinal);
        calendarFinal.set(Calendar.HOUR_OF_DAY, 23);
        calendarFinal.set(Calendar.MINUTE, 59);
        calendarFinal.set(Calendar.SECOND, 59);
        calendarFinal.set(Calendar.MILLISECOND, 999);
        this.tm.setList(DB_Produccion.consultarProductosTerminadosDetalle(descripcion, buscarPor, ordenarPor, clasificarPor, categoria, porFecha, calendarInicio.getTime(), calendarFinal.getTime()));
    }
}
