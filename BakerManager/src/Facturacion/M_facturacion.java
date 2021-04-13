/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Facturacion;

import DB.DB_Cliente;
import DB.DB_Ingreso;
import DB.DB_Preferencia;
import DB.DB_Timbrado;
import DB.DB_manager;
import Entities.E_Timbrado;
import Entities.E_facturaCabecera;
import Entities.E_facturaDetalle;
import Entities.E_tipoOperacion;
import Entities.M_cliente;
import MenuPrincipal.DatosUsuario;
import ModeloTabla.FacturaDetalleTableModel;
import ModeloTabla.SeleccionVentaCabecera;
import ModeloTabla.SeleccionVentaCabeceraTableModel;
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
    private SeleccionVentaCabeceraTableModel tm;
    private FacturaDetalleTableModel tmd;

    public M_facturacion(int idCliente, String fechaInicio, String fechaFin, String condVenta) {
        this.entidad = idCliente;
        this.inicio = fechaInicio;
        this.fin = fechaFin;
        this.condVenta = condVenta;
        this.agregarTodos = false;
        this.tm = new SeleccionVentaCabeceraTableModel();
        this.tmd = new FacturaDetalleTableModel();
    }

    public void setTm(SeleccionVentaCabeceraTableModel tm) {
        this.tm = tm;
    }

    public SeleccionVentaCabeceraTableModel getTm() {
        return tm;
    }

    public void setTmd(FacturaDetalleTableModel tmd) {
        this.tmd = tmd;
    }

    public FacturaDetalleTableModel getTmd() {
        return tmd;
    }

    public List<E_facturaCabecera> obtenerVentasCabecera() {
        return DB_Ingreso.obtenerVentasCabeceras(entidad, inicio, fin, condVenta);
    }

    public List<E_facturaDetalle> obtenerVentasDetalle(ArrayList<SeleccionVentaCabecera> ventaCabecera) {
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

    public boolean facturar(ArrayList<E_facturaCabecera> facalist, int idTimbrado, int nroFactura, int idTipoOperacion) {
        int idFuncionario = DatosUsuario.getRol_usuario().getFuncionario().getIdFuncionario();
        DB_Ingreso.facturarVentas(facalist, idFuncionario, entidad, nroFactura, idTipoOperacion);
        return true;
    }

    public M_cliente obtenerCliente() {
        return DB_Cliente.obtenerDatosClienteID(entidad);
    }

    public E_tipoOperacion obtenerTipoOperacion() {
        E_tipoOperacion tiop = DB_manager.obtenerTipoOperaccion(condVenta);
        return tiop;
    }

    public E_Timbrado obtenerTimbradoPredeterminado() {
        int idTimbrado = DB_Preferencia.obtenerPreferenciaGeneral().getIdTimbradoVenta();
        return DB_Timbrado.obtenerTimbrado(idTimbrado);
    }

    public String verificarTimbrados() {
        String mensaje = "OK";
        int idTimbrado = -1;
        if (tm.getList().isEmpty()) {
            mensaje = "No hay facturas de ventas seleccionadas";
            return mensaje;
        }
        SeleccionVentaCabecera svc = tm.getList().get(0);
        idTimbrado = svc.getFacturaCabecera().getTimbrado().getId();
        for (SeleccionVentaCabecera unaCabecera : this.tm.getList()) {
            int otroTimbrado = unaCabecera.getFacturaCabecera().getTimbrado().getId();
            if (idTimbrado != otroTimbrado) {
                mensaje = "La factura con ID: " + svc.getFacturaCabecera().getIdFacturaCabecera() + " \n"
                        + "tiene diferente timbrado con la venta con ID: " + unaCabecera.getFacturaCabecera().getIdFacturaCabecera() + " \n"
                        + "La operación asignará el timbrado y el nro de factura a todas las ventas";
                return mensaje;
            }
        }
        return mensaje;
    }
}
