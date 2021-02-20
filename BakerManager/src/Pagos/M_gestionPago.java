/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pagos;

import DB.DB_Pago;
import DB.DB_manager;
import Entities.E_reciboPagoCabecera;
import Entities.E_tipoOperacion;
import Entities.Estado;
import Entities.M_egresoCabecera;
import Entities.M_funcionario;
import Entities.M_proveedor;
import ModeloTabla.ReciboPagoCabeceraTableModel;
import ModeloTabla.ReciboPagoDetalleTableModel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_gestionPago {

    private M_funcionario funcionario;
    private M_proveedor proveedor;
    private M_egresoCabecera facturaCabecera;
    private ReciboPagoCabeceraTableModel tmCabecera;
    private ReciboPagoDetalleTableModel tmDetalle;

    public M_gestionPago() {
        this.funcionario = new M_funcionario();
        this.funcionario.setId(-1);
        this.proveedor = new M_proveedor();
        this.proveedor.setId(-1);
        this.facturaCabecera = new M_egresoCabecera();
        this.tmCabecera = new ReciboPagoCabeceraTableModel();
        this.tmDetalle = new ReciboPagoDetalleTableModel();
    }

    public M_gestionPago(M_funcionario funcionario) {
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

    public String obtenerNombreProveedor() {
        String razonSocial = this.getProveedor().getEntidad();
        String nombreFantasia = this.getProveedor().getNombre();
        return razonSocial + "-(" + nombreFantasia + ")";
    }

    public void borrarDatos() {
        this.funcionario = new M_funcionario();
        this.funcionario.setId(-1);
        this.proveedor = new M_proveedor();
        this.proveedor.setId(-1);
    }

    public void setProveedor(M_proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public M_proveedor getProveedor() {
        return proveedor;
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

    public ArrayList<Estado> obtenerEstados() {
        ArrayList<Estado> estados = DB_manager.obtenerEstados();
        estados.add(new Estado(-1, "Todos"));
        return estados;
    }

    public M_egresoCabecera getFacturaCabecera() {
        return facturaCabecera;
    }

    public void setFacturaCabecera(M_egresoCabecera facturaCabecera) {
        this.facturaCabecera = facturaCabecera;
    }

    public List<E_reciboPagoCabecera> obtenerPago(M_proveedor proveedor, M_funcionario funcionario, Date fechaInicio, Date fechaFin, Estado estado, int nroFactura) {
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
        return DB_Pago.obtenerPago(proveedor.getId(), funcionario.getId_funcionario(), calendarInicio.getTime(), calendarFinal.getTime(), nroFactura, estado.getId());
    }

    /**
     * @return the tm
     */
    public ReciboPagoCabeceraTableModel getTm() {
        return tmCabecera;
    }

    /**
     * @param tm the tm to set
     */
    public void setTm(ReciboPagoCabeceraTableModel tm) {
        this.tmCabecera = tm;
    }

    public ReciboPagoDetalleTableModel getTmDetalle() {
        return tmDetalle;
    }

    public void setTmDetalle(ReciboPagoDetalleTableModel tmDetalle) {
        this.tmDetalle = tmDetalle;
    }

    public void actualizarDetalle(Integer idCabecera) {
        getTmDetalle().setList(DB_Pago.obtenerPagoDetalle(idCabecera));
    }

    public void limpiarDetalle() {
        getTmDetalle().vaciarLista();
    }

    public Estado getCobroEstado(int fila) {
        return getTm().getList().get(fila).getEstado();
    }

    public void anularPago(Integer idCabecera) {
        DB_Pago.anularPago(idCabecera, true);
    }
}
