/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros;

import DB.DB_Cobro;
import DB.DB_manager;
import Entities.E_tipoOperacion;
import Entities.M_facturaCabecera;
import ModeloTabla.FacturaSinPagoTableModel;
import java.util.ArrayList;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_seleccionarFacturaPendiente {

    private M_facturaCabecera facturaCabecera;
    private FacturaSinPagoTableModel tm;
    private int tipo;

    public M_seleccionarFacturaPendiente(int idCliente, int tipo) {
        this.facturaCabecera = new M_facturaCabecera();
        this.facturaCabecera.getFuncionario().setId(-1);
        this.facturaCabecera.getCliente().setIdCliente(idCliente);
        this.tm = new FacturaSinPagoTableModel();
        this.tipo = tipo;
    }

    public String obtenerNombreFuncionario() {
        String alias = this.facturaCabecera.getFuncionario().getAlias();
        String nombre = this.facturaCabecera.getFuncionario().getNombre();
        String apellido = this.facturaCabecera.getFuncionario().getApellido();
        return alias + "-(" + nombre + " " + apellido + ")";
    }

    public String obtenerNombreCliente() {
        String razonSocial = this.facturaCabecera.getCliente().getEntidad();
        String nombreFantasia = this.facturaCabecera.getCliente().getNombre();
        return razonSocial + "-(" + nombreFantasia + ")";
    }

    public void borrarDatos() {
        this.facturaCabecera.getFuncionario().setId(-1);
        this.facturaCabecera.getCliente().setIdCliente(-1);
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
    public FacturaSinPagoTableModel getTableModel() {
        return tm;
    }

    /**
     * @param tm the tm to set
     */
    public void setTableModel(FacturaSinPagoTableModel tm) {
        this.tm = tm;
    }

    public void consultarFacturasPendiente() {
        int idCliente = this.facturaCabecera.getCliente().getIdCliente();
        getTableModel().setList(DB_Cobro.consultarPagosPendiente(null, null, idCliente, -1, false));
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

}
