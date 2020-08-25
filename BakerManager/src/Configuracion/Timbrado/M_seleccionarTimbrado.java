/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuracion.Timbrado;

import DB.DB_Timbrado;
import DB.DB_manager;
import Entities.E_Timbrado;
import Entities.Estado;
import ModeloTabla.SeleccionTimbradoVentaTableModel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_seleccionarTimbrado {

    SeleccionTimbradoVentaTableModel tm;
    private ArrayList tipoFechas;

    public M_seleccionarTimbrado() {
        this.tm = new SeleccionTimbradoVentaTableModel();
        this.tipoFechas = new ArrayList();
        this.tipoFechas.add("Fecha vencimiento");
        this.tipoFechas.add("Fecha creación");
    }

    public SeleccionTimbradoVentaTableModel getTm() {
        return tm;
    }

    public ArrayList getTipoFechas() {
        return tipoFechas;
    }

    public List<E_Timbrado> obtenerTimbradoVentas(Date fechaInicio, Date fechaFinal,
            int nroTimbrado, Estado estado, boolean conFecha, String tipoFecha) {
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
        int idEstado = estado.getId();
        int idFuncionario = -1;
        return DB_Timbrado.obtenerTimbrados(idFuncionario, nroTimbrado, calendarInicio.getTime(), calendarFinal.getTime(), tipoFecha, idEstado, conFecha);
    }

    public ArrayList<Estado> obtenerEstados() {
        ArrayList<Estado> estados = DB_manager.obtenerEstados();
        estados.add(new Estado(-1, "Todos"));
        return estados;
    }

}
