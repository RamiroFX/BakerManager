/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cobros_Pagos;

import DB.DB_Ingreso;
import DB.DB_manager;
import DB.ResultSetTableModel;
import Entities.E_tipoOperacion;
import Entities.M_cliente;
import Entities.M_facturaCabecera;
import Entities.M_funcionario;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.table.TableModel;

/**
 *
 * @author Ramiro Ferreira
 */
public class M_gestionCobroPago {

    private M_funcionario funcionario;
    private M_cliente cliente;
    private M_facturaCabecera facturaCabecera;

    public M_gestionCobroPago() {
        this.funcionario = new M_funcionario();
        this.cliente = new M_cliente();
        this.facturaCabecera = new M_facturaCabecera();
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
        this.funcionario = null;
        this.cliente = null;
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

    public ResultSetTableModel obtenerCobro(M_cliente cliente, M_funcionario funcionario, Date fechaInicio, Date fechaFinal, E_tipoOperacion condCompra, String nroFactura) {
        String fechaInicioAux = "";
        String fechaFinalAux = "";
        try {
            java.util.Date dateInicio = fechaInicio;
            fechaInicioAux = new Timestamp(dateInicio.getTime()).toString().substring(0, 11);
            fechaInicioAux = fechaInicioAux + "00:00:00.000";
        } catch (Exception e) {
            fechaInicioAux = "Todos";
        }
        try {
            java.util.Date dateFinal = fechaFinal;
            fechaFinalAux = new Timestamp(dateFinal.getTime()).toString().substring(0, 11);
            fechaFinalAux = fechaFinalAux + "23:59:59.000";
        } catch (Exception e) {
            fechaFinalAux = "Todos";
        }
        return DB_Ingreso.obtenerCobro(cliente, funcionario, fechaInicioAux, fechaFinalAux, condCompra, nroFactura);
    }

    public ResultSetTableModel obtenerCobroPendiente(M_cliente cliente, E_tipoOperacion condCompra) {
        String fechaInicioAux = "Todos";
        String fechaFinalAux = "Todos";
        return DB_Ingreso.obtenerCobro(cliente, null, fechaInicioAux, fechaFinalAux, condCompra, "");
    }

}
