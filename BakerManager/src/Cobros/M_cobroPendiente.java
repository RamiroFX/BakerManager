/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros;

import DB.DB_Cobro;
import DB.DB_Ingreso;
import DB.DB_manager;
import Entities.E_facturaSinPago;
import Entities.E_movimientoContable;
import Entities.E_tipoOperacion;
import Entities.Estado;
import Entities.M_cliente;
import Entities.M_facturaCabecera;
import Entities.M_funcionario;
import Interface.InterfaceFacturaDetalle;
import ModeloTabla.FacturaCabeceraTableModel;
import ModeloTabla.FacturaDetalleTableModel;
import ModeloTabla.FacturaSinPagoTableModel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_cobroPendiente {

    private M_facturaCabecera facturaCabecera;
    private FacturaSinPagoTableModel tm;
    private FacturaDetalleTableModel tmDetalle;
    private InterfaceFacturaDetalle interfaceFacturaDetalle;

    public M_cobroPendiente() {
        this.facturaCabecera = new M_facturaCabecera();
        this.facturaCabecera.getFuncionario().setId(-1);
        this.facturaCabecera.getCliente().setIdCliente(-1);
        this.tm = new FacturaSinPagoTableModel();
        this.tmDetalle = new FacturaDetalleTableModel(interfaceFacturaDetalle);
    }

    public String obtenerNombreFuncionario() {
        String alias = this.getFacturaCabecera().getFuncionario().getAlias();
        String nombre = this.getFacturaCabecera().getFuncionario().getNombre();
        String apellido = this.getFacturaCabecera().getFuncionario().getApellido();
        return alias + "-(" + nombre + " " + apellido + ")";
    }

    public String obtenerNombreCliente() {
        String razonSocial = this.getFacturaCabecera().getCliente().getEntidad();
        String nombreFantasia = this.getFacturaCabecera().getCliente().getNombre();
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
    public M_facturaCabecera getFacturaCabecera() {
        return facturaCabecera;
    }

    /**
     * @param facturaCabecera the facturaCabecera to set
     */
    public void setFacturaCabecera(M_facturaCabecera facturaCabecera) {
        this.facturaCabecera = facturaCabecera;
    }

    /**
     * @return the tm
     */
    public FacturaSinPagoTableModel getTm() {
        return tm;
    }

    /**
     * @param tm the tm to set
     */
    public void setTm(FacturaSinPagoTableModel tm) {
        this.tm = tm;
    }

    /**
     * @return the tmDetalle
     */
    public FacturaDetalleTableModel getTmDetalle() {
        return tmDetalle;
    }

    /**
     * @param tmDetalle the tmDetalle to set
     */
    public void setTmDetalle(FacturaDetalleTableModel tmDetalle) {
        this.tmDetalle = tmDetalle;
    }

    public void limpiarDetalle() {
        this.getTmDetalle().vaciarLista();
    }

    public void borrarDatos() {
        this.getFacturaCabecera().getFuncionario().setId(-1);
        this.getFacturaCabecera().getCliente().setIdCliente(-1);
    }

    public void actualizarDetalle(int idFacturaCabecera) {
        this.getTmDetalle().setFacturaDetalleList(DB_Ingreso.obtenerVentaDetalles(idFacturaCabecera));
    }

    public void vaciarDetalle() {
        this.getTmDetalle().vaciarLista();
    }

    public List<E_movimientoContable> obtenerCobroPendiente(M_cliente cliente, Date fechaInicio, Date fechaFin, int nroFactura, boolean conFecha) {
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
        return DB_Cobro.consultarPagosPendiente(fechaInicio, fechaFin, cliente.getIdCliente(), nroFactura, conFecha);
    }
}
