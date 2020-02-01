/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros;

import DB.DB_Ingreso;
import DB.DB_manager;
import DB.ResultSetTableModel;
import Entities.E_tipoOperacion;
import Entities.M_cliente;
import Entities.M_facturaCabecera;
import Entities.M_funcionario;
import ModeloTabla.FacturaCabeceraTableModel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_gestionCobroPago {

    private M_funcionario funcionario;
    private M_cliente cliente;
    private M_facturaCabecera facturaCabecera;
    private FacturaCabeceraTableModel tm;

    public M_gestionCobroPago() {
        this.funcionario = new M_funcionario();
        this.funcionario.setId_funcionario(-1);
        this.cliente = new M_cliente();
        this.cliente.setIdCliente(-1);
        this.facturaCabecera = new M_facturaCabecera();
        this.tm = new FacturaCabeceraTableModel();
    }

    public M_gestionCobroPago(M_funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public M_funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(M_funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public String obtenerNombreFuncionario() {
        String alias = this.getFuncionario().getAlias();
        String nombre = this.getFuncionario().getNombre();
        String apellido = this.getFuncionario().getApellido();
        return alias + "-(" + nombre + " " + apellido + ")";
    }

    public String obtenerNombreCliente() {
        String razonSocial = this.getCliente().getEntidad();
        String nombreFantasia = this.getCliente().getNombre();
        return razonSocial + "-(" + nombreFantasia + ")";
    }

    public void borrarDatos() {
        this.funcionario = new M_funcionario();
        this.funcionario.setId_funcionario(-1);
        this.cliente = new M_cliente();
        this.cliente.setIdCliente(-1);
    }

    ResultSetTableModel consultarCajas(int idFuncionario, String fecha_inicio, String fecha_fin) {
        //TODO
        return null;
    }

    /**
     * @return the cliente
     */
    public M_cliente getCliente() {
        return cliente;
    }

    /**
     * @param cliente the cliente to set
     */
    public void setCliente(M_cliente cliente) {
        this.cliente = cliente;
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

    public List<M_facturaCabecera> obtenerCobro(M_cliente cliente, M_funcionario funcionario, Date fechaInicio, Date fechaFin, E_tipoOperacion condCompra, int nroFactura) {
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
        return DB_Ingreso.obtenerCobro(cliente.getIdCliente(), funcionario.getId_funcionario(), calendarInicio.getTime(), calendarFinal.getTime(), condCompra.getId(), nroFactura, true);
    }

    public List<M_facturaCabecera> obtenerCobroPendiente(M_cliente cliente, E_tipoOperacion condCompra) {
        return DB_Ingreso.obtenerCobro(cliente.getIdCliente(), -1, null, null, condCompra.getId(), -1, false);
    }

    /**
     * @return the tm
     */
    public FacturaCabeceraTableModel getTm() {
        return tm;
    }

    /**
     * @param tm the tm to set
     */
    public void setTm(FacturaCabeceraTableModel tm) {
        this.tm = tm;
    }

}
