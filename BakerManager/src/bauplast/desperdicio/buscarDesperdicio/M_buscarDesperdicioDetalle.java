/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast.desperdicio.buscarDesperdicio;

import DB.DB_Produccion;
import DB.DB_manager;
import Entities.Estado;
import ModeloTabla.RolloProducidoTableModel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_buscarDesperdicioDetalle {

    private RolloProducidoTableModel tm;

    public M_buscarDesperdicioDetalle() {
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

    public void consultarRollos(String descripcion, String buscarPor, String ordenarPor, String clasificarPor, String estado,
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
        this.tm.setList(DB_Produccion.consultarFilmDisponible(descripcion, buscarPor, ordenarPor, clasificarPor, estado, porFecha, calendarInicio.getTime(), calendarFinal.getTime()));
    }
}
