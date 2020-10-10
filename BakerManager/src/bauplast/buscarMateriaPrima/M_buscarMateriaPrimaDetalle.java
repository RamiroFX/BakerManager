/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bauplast.buscarMateriaPrima;

import DB.DB_Produccion;
import DB.DB_manager;
import Entities.Estado;
import ModeloTabla.MateriaPrimaBajaTableModel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_buscarMateriaPrimaDetalle {

    private MateriaPrimaBajaTableModel bajaMateriaPrimaTM;

    public M_buscarMateriaPrimaDetalle() {
        this.bajaMateriaPrimaTM = new MateriaPrimaBajaTableModel();
    }

    public MateriaPrimaBajaTableModel getTm() {
        return bajaMateriaPrimaTM;
    }

    public void setTm(MateriaPrimaBajaTableModel tm) {
        this.bajaMateriaPrimaTM = tm;
    }

    public ArrayList<Estado> obtenerEstado() {
        return DB_manager.obtenerEstados();
    }

    public void consultarMateriaPrima(String descripcion, String buscarPor, String ordenarPor, String clasificarPor, String estado,
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
        this.bajaMateriaPrimaTM.setList(DB_Produccion.consultarMateriaPrimaBajaDetalle(descripcion, buscarPor, ordenarPor, clasificarPor, porFecha, calendarInicio.getTime(), calendarFinal.getTime()));
    }
}
