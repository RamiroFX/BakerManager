/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pagos;

import DB.DB_Egreso;
import DB.DB_Pago;
import DB.DB_manager;
import Entities.E_egresoSinPago;
import Entities.E_tipoOperacion;
import Entities.M_egresoCabecera;
import Entities.M_proveedor;
import ModeloTabla.EgresoDetalleTableModel;
import ModeloTabla.EgresoSinPagoTableModel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_pagoPendiente {

    private M_egresoCabecera facturaCabecera;
    private EgresoSinPagoTableModel tm;
    private EgresoDetalleTableModel tmDetalle;

    public M_pagoPendiente() {
        this.facturaCabecera = new M_egresoCabecera();
        this.facturaCabecera.getFuncionario().setIdFuncionario(-1);
        this.facturaCabecera.getProveedor().setId(-1);
        this.tm = new EgresoSinPagoTableModel();
        this.tmDetalle = new EgresoDetalleTableModel();
    }

    public String obtenerNombreFuncionario() {
        String alias = this.getCabecera().getFuncionario().getAlias();
        String nombre = this.getCabecera().getFuncionario().getNombre();
        String apellido = this.getCabecera().getFuncionario().getApellido();
        return alias + "-(" + nombre + " " + apellido + ")";
    }

    public String obtenerNombreProveedor() {
        String razonSocial = this.getCabecera().getProveedor().getEntidad();
        String nombreFantasia = this.getCabecera().getProveedor().getNombre();
        return razonSocial + "-(" + nombreFantasia + ")";
    }

    /**
     *
     * Devuelve la lista de tipo de operaciones sin incluir el tipo contado y
     * agrega el tipo de operacion todos para seleccionar todos los tipo de
     * operacion a credito
     *
     * @return tipoOperacionesList
     */
    public ArrayList<E_tipoOperacion> obtenerTipoOperacion() {
        E_tipoOperacion tiop = new E_tipoOperacion();
        tiop.setId(-1);
        tiop.setDescripcion("Todos");
        tiop.setDuracion(0);
        ArrayList<E_tipoOperacion> tiopList = DB_manager.obtenerTipoOperaciones();
        ArrayList<E_tipoOperacion> tipoOperacionesList = new ArrayList<>();
        tipoOperacionesList.add(tiop);
        for (int i = 1; i < tiopList.size(); i++) {
            E_tipoOperacion get = tiopList.get(i);
            tipoOperacionesList.add(get);
        }
        return tipoOperacionesList;
    }

    /**
     * @return the facturaCabecera
     */
    public M_egresoCabecera getCabecera() {
        return facturaCabecera;
    }

    /**
     * @param cabecera the facturaCabecera to set
     */
    public void setCabecera(M_egresoCabecera cabecera) {
        this.facturaCabecera = cabecera;
    }

    /**
     * @return the tm
     */
    public EgresoSinPagoTableModel getTm() {
        return tm;
    }

    /**
     * @param tm the tm to set
     */
    public void setTm(EgresoSinPagoTableModel tm) {
        this.tm = tm;
    }

    /**
     * @return the tmDetalle
     */
    public EgresoDetalleTableModel getTmDetalle() {
        return tmDetalle;
    }

    /**
     * @param tmDetalle the tmDetalle to set
     */
    public void setTmDetalle(EgresoDetalleTableModel tmDetalle) {
        this.tmDetalle = tmDetalle;
    }

    public void limpiarDetalle() {
        this.getTmDetalle().vaciarLista();
    }

    public void borrarDatos() {
        this.getCabecera().getFuncionario().setIdFuncionario(-1);
        this.getCabecera().getProveedor().setId(-1);
    }

    public void actualizarDetalle(int idFactura) {
        this.getTmDetalle().setList(DB_Egreso.obtenerEgresoDetalles(idFactura));
    }

    public List<E_egresoSinPago> obtenerCobroPendiente(M_proveedor proveedor, Date fechaInicio, Date fechaFin, int nroFactura, boolean conFecha) {
        Calendar calendarInicio = Calendar.getInstance();
        calendarInicio.setTime(fechaInicio);
        calendarInicio.set(Calendar.HOUR_OF_DAY, 0);
        calendarInicio.set(Calendar.MINUTE, 0);
        calendarInicio.set(Calendar.SECOND, 0);
        calendarInicio.set(Calendar.MILLISECOND, 0);
        Calendar calendarFinal = Calendar.getInstance();
        calendarFinal.setTime(fechaFin);
        calendarFinal.set(Calendar.HOUR_OF_DAY, 23);
        calendarFinal.set(Calendar.MINUTE, 59);
        calendarFinal.set(Calendar.SECOND, 59);
        calendarFinal.set(Calendar.MILLISECOND, 999);
        return DB_Pago.consultarPagosPendiente(fechaInicio, fechaFin, proveedor.getId(), nroFactura, conFecha);
    }
}
