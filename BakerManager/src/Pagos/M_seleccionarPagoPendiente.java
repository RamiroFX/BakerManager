/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pagos;

import DB.DB_Pago;
import DB.DB_manager;
import Entities.E_tipoOperacion;
import Entities.M_egreso_cabecera;
import ModeloTabla.EgresoSinPagoTableModel;
import java.util.ArrayList;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_seleccionarPagoPendiente {

    private M_egreso_cabecera facturaCabecera;
    private EgresoSinPagoTableModel tm;

    public M_seleccionarPagoPendiente(int idProveedor) {
        this.facturaCabecera = new M_egreso_cabecera();
        this.facturaCabecera.getFuncionario().setId_funcionario(-1);
        this.facturaCabecera.getProveedor().setId(idProveedor);
        this.tm = new EgresoSinPagoTableModel();
    }

    public String obtenerNombreFuncionario() {
        String alias = this.facturaCabecera.getFuncionario().getAlias();
        String nombre = this.facturaCabecera.getFuncionario().getNombre();
        String apellido = this.facturaCabecera.getFuncionario().getApellido();
        return alias + "-(" + nombre + " " + apellido + ")";
    }

    public String obtenerNombreCliente() {
        String razonSocial = this.facturaCabecera.getProveedor().getEntidad();
        String nombreFantasia = this.facturaCabecera.getProveedor().getNombre();
        return razonSocial + "-(" + nombreFantasia + ")";
    }

    public void borrarDatos() {
        this.facturaCabecera.getFuncionario().setId_funcionario(-1);
        this.facturaCabecera.getProveedor().setId(-1);
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
     * @return the tm
     */
    public EgresoSinPagoTableModel getTableModel() {
        return tm;
    }

    /**
     * @param tm the tm to set
     */
    public void setTableModel(EgresoSinPagoTableModel tm) {
        this.tm = tm;
    }

    public void consultarFacturasPendiente() {
        int idProveedor = this.facturaCabecera.getProveedor().getId();
        getTableModel().setList(DB_Pago.consultarFacturasPendiente(idProveedor));
    }

}
