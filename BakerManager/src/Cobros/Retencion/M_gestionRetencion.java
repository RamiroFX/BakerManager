/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros.Retencion;

import DB.DB_Cobro;
import DB.DB_manager;
import Entities.E_retencionVenta;
import Entities.Estado;
import ModeloTabla.RetencionVentaTableModel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_gestionRetencion {

    private E_retencionVenta cabecera;
    private RetencionVentaTableModel tmCabecera;

    public M_gestionRetencion() {
        this.cabecera = new E_retencionVenta();
        this.cabecera.getFuncionario().setId_funcionario(-1);
        this.cabecera.getVenta().getCliente().setIdCliente(-1);
        this.tmCabecera = new RetencionVentaTableModel();
    }

    public E_retencionVenta getCabecera() {
        return cabecera;
    }

    public void setCabecera(E_retencionVenta cabecera) {
        this.cabecera = cabecera;
    }

    public RetencionVentaTableModel getTm() {
        return tmCabecera;
    }

    public void setTm(RetencionVentaTableModel tm) {
        this.tmCabecera = tm;
    }

    public void borrarDatos() {
        this.cabecera.getFuncionario().setNombre("");
        this.cabecera.getFuncionario().setId_funcionario(-1);
        this.cabecera.getVenta().getCliente().setEntidad("");
        this.cabecera.getVenta().getCliente().setIdCliente(-1);
    }

    String obtenerNombreFuncionario() {
        String alias = this.getCabecera().getFuncionario().getAlias();
        String nombre = this.getCabecera().getFuncionario().getNombre();
        String apellido = this.getCabecera().getFuncionario().getApellido();
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

    public List<E_retencionVenta> obtenerRetenciones(Date fechaInicio, Date fechaFinal, int nroRetencion, Estado estado, boolean conFecha) {
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
        int idCliente = getCabecera().getVenta().getCliente().getIdCliente();
        int idFuncionario = getCabecera().getFuncionario().getId_funcionario();
        return DB_Cobro.obtenerRetenciones(idCliente, idFuncionario, nroRetencion,
                calendarInicio.getTime(), calendarFinal.getTime(), idEstado, conFecha);

    }

    public ArrayList<Estado> obtenerEstados() {
        ArrayList<Estado> estados = DB_manager.obtenerEstados();
        estados.add(new Estado(-1, "Todos"));
        return estados;
    }

    public void anularRetencion(int idRetencion, int idEstado, boolean recuperarNroRetencion) {
        DB_Cobro.anularRetencion(idRetencion, Estado.INACTIVO, recuperarNroRetencion);
    }
}
