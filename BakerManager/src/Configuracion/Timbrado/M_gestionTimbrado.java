/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuracion.Timbrado;

import DB.DB_Preferencia;
import DB.DB_Timbrado;
import DB.DB_manager;
import Entities.E_Timbrado;
import Entities.Estado;
import ModeloTabla.TimbradoVentaTableModel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Ramiro Fereira
 */
public class M_gestionTimbrado {

    private E_Timbrado cabecera;
    private TimbradoVentaTableModel tmCabecera;
    private ArrayList tipoFechas;

    public M_gestionTimbrado() {
        this.cabecera = new E_Timbrado();
        this.cabecera.getCreador().setId(-1);
        this.tmCabecera = new TimbradoVentaTableModel();
        this.tipoFechas = new ArrayList();
        this.tipoFechas.add("Fecha vencimiento");
        this.tipoFechas.add("Fecha creación");
    }

    public E_Timbrado getCabecera() {
        return cabecera;
    }

    public void setCabecera(E_Timbrado cabecera) {
        this.cabecera = cabecera;
    }

    public TimbradoVentaTableModel getTm() {
        return tmCabecera;
    }

    public void setTm(TimbradoVentaTableModel tm) {
        this.tmCabecera = tm;
    }

    public void borrarDatos() {
        this.cabecera.getCreador().setNombre("");
        this.cabecera.getCreador().setId(-1);
    }

    String obtenerNombreFuncionario() {
        String alias = this.getCabecera().getCreador().getAlias();
        String nombre = this.getCabecera().getCreador().getNombre();
        String apellido = this.getCabecera().getCreador().getApellido();
        return alias + "-(" + nombre + " " + apellido + ")";
    }

    public boolean validarFechas(Date f_inicio, Date f_final) {
        if (f_inicio != null && f_final != null) {
            int dateValue = f_inicio.compareTo(f_final);
            if (dateValue <= 0) {
                return true;
            }
        }
        return false;
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
        int idFuncionario = getCabecera().getCreador().getId_funcionario();
        return DB_Timbrado.obtenerTimbrados(idFuncionario, nroTimbrado, calendarInicio.getTime(), calendarFinal.getTime(), tipoFecha, idEstado, conFecha);
    }

    public ArrayList<Estado> obtenerEstados() {
        ArrayList<Estado> estados = DB_manager.obtenerEstados();
        estados.add(new Estado(-1, "Todos"));
        return estados;
    }

    public void anularTimbrado(int idTimbrado, int idEstado, boolean recuperarNroRetencion) {
        DB_Timbrado.anularTimbrado(idTimbrado, Estado.INACTIVO, recuperarNroRetencion);
    }

    /**
     * @return the tipoFechas
     */
    public ArrayList getTipoFechas() {
        return tipoFechas;
    }

    /**
     * @param tipoFechas the tipoFechas to set
     */
    public void setTipoFechas(ArrayList tipoFechas) {
        this.tipoFechas = tipoFechas;
    }

    public void establecerTimbradoPredeterminado(int idTimbradoVenta) {
        DB_Preferencia.establecerTimbradoVentaPredeterminado(idTimbradoVenta);
    }
}
