/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Facturacion;

import DB.DB_Cliente;
import DB.DB_Ingreso;
import DB.DB_manager;
import Entities.E_facturaCabecera;
import Entities.E_facturaCabeceraFX;
import Entities.E_tipoOperacion;
import Entities.M_cliente;
import Entities.M_facturaDetalle;
import MenuPrincipal.DatosUsuario;
import ModeloTabla.SeleccionVentaCabecera;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ramiro
 */
public class M_facturacion {

    private int entidad;
    private String inicio, fin;
    private String condVenta;
    private boolean agregarTodos;

    M_facturacion(int idCliente, String fechaInicio, String fechaFin, String condVenta) {
        this.entidad = idCliente;
        this.inicio = fechaInicio;
        this.fin = fechaFin;
        this.condVenta = condVenta;
        this.agregarTodos = false;
    }

    public List<E_facturaCabecera> obtenerVentasCabecera() {
        return DB_Ingreso.obtenerVentasCabeceras(entidad, inicio, fin, condVenta);
    }

    public List<M_facturaDetalle> obtenerVentasDetalle(ArrayList<SeleccionVentaCabecera> ventaCabecera) {
        return DB_Ingreso.obtenerVentaDetalles(ventaCabecera);
    }

    public boolean isAgregarTodos() {
        return agregarTodos;
    }

    public void setAgregarTodos(boolean agregarTodos) {
        this.agregarTodos = agregarTodos;
    }

    public int getNroFactura() {
        int nroFactura = DB_Ingreso.obtenerUltimoNroFactura() + 1;
        int nroFacturacion = DB_Ingreso.obtenerUltimoNroFacturacion() + 1;
        if (nroFactura >= nroFacturacion) {
            return nroFactura;
        } else {
            return nroFacturacion;
        }
    }

    public boolean facturar(ArrayList<E_facturaCabecera> facalist, int nroFactura, int idTipoOperacion) {
        int idFuncionario = DatosUsuario.getRol_usuario().getFuncionario().getId_funcionario();
        DB_Ingreso.facturarVentas(facalist, idFuncionario, entidad,nroFactura, idTipoOperacion);
        return true;
    }

    public M_cliente obtenerCliente() {
        return DB_Cliente.obtenerDatosClienteID(entidad);
    }

    public E_tipoOperacion obtenerTipoOperacion() {
        E_tipoOperacion tiop = DB_manager.obtenerTipoOperaccion(condVenta);
        return tiop;
    }
}
