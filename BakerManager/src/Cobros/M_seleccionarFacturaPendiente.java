/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros;

import DB.DB_manager;
import Entities.E_tipoOperacion;
import Entities.M_cliente;
import Entities.M_facturaCabecera;
import Entities.M_funcionario;
import ModeloTabla.FacturaCabeceraTableModel;
import java.util.ArrayList;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_seleccionarFacturaPendiente {

    private M_facturaCabecera facturaCabecera;
    private FacturaCabeceraTableModel tm;

    public M_seleccionarFacturaPendiente() {
        this.facturaCabecera = new M_facturaCabecera();
        this.facturaCabecera.getFuncionario().setId_funcionario(-1);
        this.facturaCabecera.getCliente().setIdCliente(-1);
        this.facturaCabecera = new M_facturaCabecera();
        this.tm = new FacturaCabeceraTableModel();
    }

    public String obtenerNombreFuncionario() {
        String alias = this.facturaCabecera.getFuncionario().getAlias();
        String nombre = this.facturaCabecera.getCliente().getNombre();
        String apellido = this.facturaCabecera.getFuncionario().getApellido();
        return alias + "-(" + nombre + " " + apellido + ")";
    }

    public String obtenerNombreCliente() {
        String razonSocial = this.facturaCabecera.getCliente().getEntidad();
        String nombreFantasia = this.facturaCabecera.getCliente().getNombre();
        return razonSocial + "-(" + nombreFantasia + ")";
    }

    public void borrarDatos() {
        this.facturaCabecera.getFuncionario().setId_funcionario(-1);
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

}
